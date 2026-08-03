package use_case.recommendation_context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Footwear;
import entity.Headwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Outfit;
import entity.WearCondition;
import entity.WeatherSuitability;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;
import use_case.settings.SettingsDataAccessInterface;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Use case interactor for deterministic, context-based outfit recommendations.
 *
 * <p>A recommendation is found in two stages. Each wardrobe slot is first narrowed on its own to
 * the few garments that best suit the context, using the criteria a single garment can be judged
 * on; only those survivors are then combined into whole outfits and scored. Searching every
 * combination directly is not affordable, because the accessories alone contribute a subset for
 * every garment the user owns, and no amount of weather filtering reduces that: filtering shrinks
 * how many garments there are, while the cost grows with how many ways they can be worn together.
 * Narrowing first bounds the second stage no matter how large the wardrobe becomes.
 */
public final class ContextBasedRecommendationInteractor implements ContextBasedRecommendationInputBoundary {
    private static final String MISSING_REQUIRED_ITEMS =
            "A recommendation requires at least one inner topwear, bottomwear, and footwear.";
    private static final String NO_SUITABLE_OUTFIT =
            "No outfit in the wardrobe is suitable for the current context.";
    private static final String CONTEXT_UNAVAILABLE =
            "Today's weather and events could not be retrieved, so no outfit can be recommended.";

    /**
     * How many garments per slot survive narrowing and reach the second stage.
     *
     * <p>One would be enough to find the best outfit on the criteria that add up per garment, as
     * the best choice for a slot is then independent of the others. Keeping a few spares leaves
     * room for the criteria that judge a finished outfit to prefer a different combination.
     */
    private static final int CANDIDATES_PER_SLOT = 3;

    private final WardrobeDataAccessInterface wardrobe;
    private final SettingsDataAccessInterface settingsProvider;
    private final EventDataAccessInterface eventProvider;
    private final WeatherDataAccessInterface weatherProvider;
    private final RecommendationOutputBoundary outputBoundary;
    private final List<OutfitAnalyzer> analyzers;
    private final SlotNarrower narrower;

    /**
     * Constructs an interactor that scores outfits with the default analyzers.
     *
     * @param wardrobe        the wardrobe repository
     * @param settingsProvider the repository of the user's location settings
     * @param eventProvider   the repository of the events happening today
     * @param weatherProvider the weather repository
     * @param outputBoundary  the presenter of the result
     */
    public ContextBasedRecommendationInteractor(
        WardrobeDataAccessInterface wardrobe,
        SettingsDataAccessInterface settingsProvider,
        EventDataAccessInterface eventProvider,
        WeatherDataAccessInterface weatherProvider,
        RecommendationOutputBoundary outputBoundary
    ) {
        this(wardrobe, settingsProvider, eventProvider, weatherProvider, outputBoundary,
            OutfitAnalyzers.standard());
    }

    /**
     * Constructs an interactor that scores outfits with the supplied analyzers.
     *
     * <p>The interactor never decides which criteria apply; it is told. A test can therefore
     * score outfits with a single analyzer, and a new criterion can be introduced without
     * touching this class or any existing analyzer.
     *
     * @param wardrobe        the wardrobe repository
     * @param settingsProvider the repository of the user's location settings
     * @param eventProvider   the repository of the events happening today
     * @param weatherProvider the weather repository
     * @param outputBoundary  the presenter of the result
     * @param analyzers       the criteria to score outfits against
     */
    public ContextBasedRecommendationInteractor(
        WardrobeDataAccessInterface wardrobe,
        SettingsDataAccessInterface settingsProvider,
        EventDataAccessInterface eventProvider,
        WeatherDataAccessInterface weatherProvider,
        RecommendationOutputBoundary outputBoundary,
        List<OutfitAnalyzer> analyzers
    ) {
        this(wardrobe, settingsProvider, eventProvider, weatherProvider, outputBoundary, analyzers,
            OutfitAnalyzers.standardItems());
    }

    /**
     * Constructs an interactor that narrows and scores with the supplied analyzers.
     *
     * @param wardrobe        the wardrobe repository
     * @param settingsProvider the repository of the user's location settings
     * @param eventProvider   the repository of the events happening today
     * @param weatherProvider the weather repository
     * @param outputBoundary  the presenter of the result
     * @param analyzers       the criteria to score whole outfits against
     * @param itemAnalyzers   the criteria to narrow each wardrobe slot with
     */
    public ContextBasedRecommendationInteractor(
        WardrobeDataAccessInterface wardrobe,
        SettingsDataAccessInterface settingsProvider,
        EventDataAccessInterface eventProvider,
        WeatherDataAccessInterface weatherProvider,
        RecommendationOutputBoundary outputBoundary,
        List<OutfitAnalyzer> analyzers,
        List<ItemAnalyzer> itemAnalyzers
    ) {
        this.wardrobe = wardrobe;
        this.settingsProvider = settingsProvider;
        this.eventProvider = eventProvider;
        this.weatherProvider = weatherProvider;
        this.outputBoundary = outputBoundary;
        this.analyzers = List.copyOf(analyzers);
        this.narrower = new SlotNarrower(itemAnalyzers, CANDIDATES_PER_SLOT);
    }

    @Override
    public void recommend(ContextBasedRecommendationInputData inputData) {
        final RecommendationContext context;
        try {
            context = new RecommendationContext(
                    weatherProvider.getCurrentByLocation(settingsProvider.getLocationCityOrDefault()),
                    eventProvider.getEvents(settingsProvider.getLocationCountryCodeOrDefault()),
                    inputData.getPreferredColors(),
                    inputData.getPreferredStyles()
            );
        } catch (ContextUnavailableException ex) {
            // Nothing about the wardrobe can be judged without today's context, and a repository
            // failing is not something the caller should have to handle for us.
            outputBoundary.prepareFailView(CONTEXT_UNAVAILABLE);
            return;
        }

        final CandidatePools candidatePools = buildCandidatePools(context);
        if (candidatePools.isMissingRequiredItems()) {
            outputBoundary.prepareFailView(MISSING_REQUIRED_ITEMS);
            return;
        }
        if (!candidatePools.canBuildOutfit()) {
            outputBoundary.prepareFailView(NO_SUITABLE_OUTFIT);
            return;
        }

        final BestOutfit bestOutfit = findBestOutfit(candidatePools, context, new Random(inputData.getSeed()));
        if (bestOutfit.outfit == null) {
            outputBoundary.prepareFailView(NO_SUITABLE_OUTFIT);
            return;
        }

        outputBoundary.prepareSuccessView(new RecommendationOutputData(
                bestOutfit.outfit,
                buildReason(bestOutfit.analysis)
        ));
    }

    private CandidatePools buildCandidatePools(RecommendationContext context) {
        final List<InnerTopwear> innerTopwears = itemsOfType(InnerTopwear.class);
        final List<Bottomwear> bottomwears = itemsOfType(Bottomwear.class);
        final List<Footwear> footwears = itemsOfType(Footwear.class);
        if (innerTopwears.isEmpty() || bottomwears.isEmpty() || footwears.isEmpty()) {
            return CandidatePools.missingRequiredItems();
        }

        final double temperature = context.getWeather().getTemperature();
        final double precipitation = context.getWeather().getPrecipitation();

        final List<Bottomwear> eligibleBottomwears = new ArrayList<>();
        for (Bottomwear bottomwear : bottomwears) {
            if (!WeatherSuitability.requiresLongBottomwear(temperature) || bottomwear.isLong()) {
                eligibleBottomwears.add(bottomwear);
            }
        }

        final List<Footwear> eligibleFootwears = new ArrayList<>();
        for (Footwear footwear : footwears) {
            if (!WeatherSuitability.requiresWaterproofFootwear(precipitation) || footwear.isWaterproof()) {
                eligibleFootwears.add(footwear);
            }
        }

        return new CandidatePools(
                narrower.narrow(innerTopwears, context),
                eligibleOuterTopwears(context),
                narrower.narrow(eligibleBottomwears, context),
                narrower.narrow(eligibleFootwears, context),
                optionalItems(narrower.narrow(itemsOfType(Headwear.class), context)),
                accessoryPools(context),
                false
        );
    }

    /**
     * Sorts the accessories into those worth wearing outright and those worth deciding on.
     *
     * <p>Unlike every other slot, an outfit holds any number of accessories, so wearing one never
     * displaces another. An accessory that suits the context can therefore only improve an outfit
     * on the criteria that add up, and needs no deciding. What remains are the accessories that
     * suit nothing in particular, which only the criteria judging a finished outfit can rule on.
     *
     * @param context the current recommendation context
     * @return the accessories to wear and the accessories to decide between
     */
    private AccessoryPools accessoryPools(RecommendationContext context) {
        final List<Accessory> worn = new ArrayList<>();
        final List<Accessory> undecided = new ArrayList<>();
        for (Accessory accessory : itemsOfType(Accessory.class)) {
            if (narrower.contributes(accessory, context)) {
                worn.add(accessory);
            }
            else {
                undecided.add(accessory);
            }
        }

        return new AccessoryPools(List.copyOf(worn), narrower.narrow(undecided, context));
    }

    private List<OuterTopwear> eligibleOuterTopwears(RecommendationContext context) {
        final double temperature = context.getWeather().getTemperature();
        final List<OuterTopwear> outerTopwears = itemsOfType(OuterTopwear.class);
        if (!WeatherSuitability.requiresOuterTopwear(temperature)) {
            return optionalItems(narrower.narrow(outerTopwears, context));
        }

        final List<OuterTopwear> eligible = new ArrayList<>();
        for (OuterTopwear outerTopwear : outerTopwears) {
            if (!WeatherSuitability.requiresThickOuterTopwear(temperature) || outerTopwear.isThick()) {
                eligible.add(outerTopwear);
            }
        }
        return narrower.narrow(eligible, context);
    }

    private BestOutfit findBestOutfit(CandidatePools pools,
                                      RecommendationContext context,
                                      Random random) {
        final BestOutfit bestOutfit = new BestOutfit(random);

        for (InnerTopwear inner : pools.innerTopwears) {
            for (OuterTopwear outer : pools.outerTopwears) {
                analyzeBottomAndFootwearChoices(
                        inner, outer, pools, context, bestOutfit
                );
            }
        }
        return bestOutfit;
    }

    private void analyzeBottomAndFootwearChoices(InnerTopwear inner,
                                                 OuterTopwear outer,
                                                 CandidatePools pools,
                                                 RecommendationContext context,
                                                 BestOutfit bestOutfit) {
        for (Bottomwear bottom : pools.bottomwears) {
            for (Footwear footwear : pools.footwears) {
                analyzeHeadwearChoices(
                        inner, outer, bottom, footwear, pools, context, bestOutfit
                );
            }
        }
    }

    private void analyzeHeadwearChoices(InnerTopwear inner,
                                        OuterTopwear outer,
                                        Bottomwear bottom,
                                        Footwear footwear,
                                        CandidatePools pools,
                                        RecommendationContext context,
                                        BestOutfit bestOutfit) {
        for (Headwear headwear : pools.headwears) {
            final OutfitBase base = new OutfitBase(
                    inner, outer, bottom, footwear, headwear, pools.accessories.worn
            );
            analyzeAccessoryChoices(
                    base,
                    pools.accessories.undecided,
                    0,
                    new ArrayList<>(),
                    context,
                    bestOutfit
            );
        }
    }

    private void analyzeAccessoryChoices(OutfitBase base,
                                         List<Accessory> accessories,
                                         int index,
                                         List<Accessory> selectedAccessories,
                                         RecommendationContext context,
                                         BestOutfit bestOutfit) {
        if (index == accessories.size()) {
            final Outfit candidate = base.toOutfit(selectedAccessories);
            final OutfitAnalysis analysis = analyze(candidate, context);
            if (analysis.isAcceptable()) {
                bestOutfit.consider(candidate, analysis);
            }
            return;
        }

        analyzeAccessoryChoices(
                base, accessories, index + 1, selectedAccessories, context, bestOutfit
        );
        selectedAccessories.add(accessories.get(index));
        analyzeAccessoryChoices(
                base, accessories, index + 1, selectedAccessories, context, bestOutfit
        );
        selectedAccessories.remove(selectedAccessories.size() - 1);
    }

    private OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        OutfitAnalysis combined = OutfitAnalysis.neutral();
        for (OutfitAnalyzer analyzer : analyzers) {
            combined = combined.combine(analyzer.analyze(outfit, context));
            if (!combined.isAcceptable()) {
                break;
            }
        }
        return combined;
    }

    private static int compare(OutfitAnalysis first, OutfitAnalysis second) {
        int result = Integer.compare(first.getEventMatches(), second.getEventMatches());
        if (result == 0) {
            result = Integer.compare(first.getPreferenceMatches(), second.getPreferenceMatches());
        }
        if (result == 0) {
            result = Double.compare(first.getFondness(), second.getFondness());
        }
        return result;
    }

    private static String buildReason(OutfitAnalysis analysis) {
        if (analysis.getReasons().isEmpty()) {
            return "This is the highest-ranked valid outfit for the current context.";
        }
        return String.join(" ", analysis.getReasons());
    }

    private <T extends AbstractWear> List<T> itemsOfType(Class<T> itemType) {
        return wardrobe.fetchWardrobe().getItems().stream()
                .filter(item -> item.getCondition() != WearCondition.DAMAGED)
                .filter(itemType::isInstance)
                .map(itemType::cast)
                .sorted(Comparator.comparing(item -> item.getUuid().toString()))
                .toList();
    }

    private static <T extends AbstractWear> List<T> optionalItems(List<T> items) {
        final List<T> optionalItems = new ArrayList<>();
        optionalItems.add(null);
        optionalItems.addAll(items);
        return optionalItems;
    }

    /**
     * The best outfit seen so far, chosen uniformly at random among those that rank equally.
     *
     * <p>Equally ranked outfits are common, because two outfits that match the same number of
     * preferences are indistinguishable to the criteria. Holding every one of them until the
     * search ends costs memory proportional to the number of candidates, so each tie is instead
     * given its due chance of replacing the incumbent as it appears: the nth of n tied outfits is
     * kept with probability 1/n, which leaves all n equally likely while only ever storing one.
     */
    private static final class BestOutfit {
        private final Random random;

        private Outfit outfit;
        private OutfitAnalysis analysis;
        private int tieCount;

        private BestOutfit(Random random) {
            this.random = random;
        }

        private void consider(Outfit candidate, OutfitAnalysis candidateAnalysis) {
            int comparison = 1;
            if (analysis != null) {
                comparison = compare(candidateAnalysis, analysis);
            }

            if (comparison > 0) {
                outfit = candidate;
                analysis = candidateAnalysis;
                tieCount = 1;
            }
            else if (comparison == 0) {
                tieCount++;
                if (random.nextInt(tieCount) == 0) {
                    outfit = candidate;
                    analysis = candidateAnalysis;
                }
            }
        }
    }

    private static final class OutfitBase {
        private final InnerTopwear innerTopwear;
        private final OuterTopwear outerTopwear;
        private final Bottomwear bottomwear;
        private final Footwear footwear;
        private final Headwear headwear;
        private final List<Accessory> wornAccessories;

        private OutfitBase(InnerTopwear innerTopwear,
                           OuterTopwear outerTopwear,
                           Bottomwear bottomwear,
                           Footwear footwear,
                           Headwear headwear,
                           List<Accessory> wornAccessories) {
            this.innerTopwear = innerTopwear;
            this.outerTopwear = outerTopwear;
            this.bottomwear = bottomwear;
            this.footwear = footwear;
            this.headwear = headwear;
            this.wornAccessories = wornAccessories;
        }

        private Outfit toOutfit(List<Accessory> accessories) {
            final List<Accessory> combined = new ArrayList<>(wornAccessories);
            combined.addAll(accessories);
            return new Outfit(
                    innerTopwear,
                    outerTopwear,
                    bottomwear,
                    footwear,
                    headwear,
                    List.copyOf(combined)
            );
        }
    }

    private static final class AccessoryPools {
        private final List<Accessory> worn;
        private final List<Accessory> undecided;

        private AccessoryPools(List<Accessory> worn, List<Accessory> undecided) {
            this.worn = worn;
            this.undecided = undecided;
        }

        private static AccessoryPools none() {
            return new AccessoryPools(List.of(), List.of());
        }
    }

    private static final class CandidatePools {
        private final List<InnerTopwear> innerTopwears;
        private final List<OuterTopwear> outerTopwears;
        private final List<Bottomwear> bottomwears;
        private final List<Footwear> footwears;
        private final List<Headwear> headwears;
        private final AccessoryPools accessories;
        private final boolean missingRequiredItems;

        private CandidatePools(List<InnerTopwear> innerTopwears,
                               List<OuterTopwear> outerTopwears,
                               List<Bottomwear> bottomwears,
                               List<Footwear> footwears,
                               List<Headwear> headwears,
                               AccessoryPools accessories,
                               boolean missingRequiredItems) {
            this.innerTopwears = innerTopwears;
            this.outerTopwears = outerTopwears;
            this.bottomwears = bottomwears;
            this.footwears = footwears;
            this.headwears = headwears;
            this.accessories = accessories;
            this.missingRequiredItems = missingRequiredItems;
        }

        private static CandidatePools missingRequiredItems() {
            return new CandidatePools(
                    List.of(), List.of(), List.of(), List.of(), List.of(), AccessoryPools.none(), true
            );
        }

        private boolean isMissingRequiredItems() {
            return missingRequiredItems;
        }

        private boolean canBuildOutfit() {
            // Headwear and accessories are not checked: their pools always carry a null
            // sentinel for "wear nothing", so they can never be empty.
            return !innerTopwears.isEmpty()
                    && !outerTopwears.isEmpty()
                    && !bottomwears.isEmpty()
                    && !footwears.isEmpty();
        }
    }
}
