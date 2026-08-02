package use_case.recommendation_context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Footwear;
import entity.Headwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Outfit;
import entity.WearCondition;
import entity.Weather;
import entity.WeatherSuitability;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;
import use_case.settings.SettingsDataAccessInterface;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Use case interactor for deterministic, context-based outfit recommendations.
 */
public final class ContextBasedRecommendationInteractor implements ContextBasedRecommendationInputBoundary {
    private static final String MISSING_REQUIRED_ITEMS =
            "A recommendation requires at least one inner topwear, bottomwear, and footwear.";
    private static final String NO_SUITABLE_OUTFIT =
            "No outfit in the wardrobe is suitable for the current context.";

    private final WardrobeDataAccessInterface wardrobe;
    private final SettingsDataAccessInterface settingsProvider;
    private final EventDataAccessInterface eventProvider;
    private final WeatherDataAccessInterface weatherProvider;
    private final RecommendationOutputBoundary outputBoundary;
    private final List<OutfitAnalyzer> analyzers;

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
        this.wardrobe = wardrobe;
        this.settingsProvider = settingsProvider;
        this.eventProvider = eventProvider;
        this.weatherProvider = weatherProvider;
        this.outputBoundary = outputBoundary;
        this.analyzers = List.copyOf(analyzers);
    }

    @Override
    public void recommend(ContextBasedRecommendationInputData inputData) {
        final RecommendationContext context = new RecommendationContext(
                weatherProvider.getCurrentByLocation(settingsProvider.getLocationCityOrDefault()),
                eventProvider.getEvents(settingsProvider.getLocationCountryCodeOrDefault()),
                inputData.getPreferredColors(),
                inputData.getPreferredStyles()
        );
        final CandidatePools candidatePools = buildCandidatePools(context);
        if (candidatePools.isMissingRequiredItems()) {
            outputBoundary.prepareFailView(MISSING_REQUIRED_ITEMS);
            return;
        }
        if (!candidatePools.canBuildOutfit()) {
            outputBoundary.prepareFailView(NO_SUITABLE_OUTFIT);
            return;
        }

        final List<AnalyzedOutfit> bestOutfits = findBestOutfits(candidatePools, context);
        if (bestOutfits.isEmpty()) {
            outputBoundary.prepareFailView(NO_SUITABLE_OUTFIT);
            return;
        }

        bestOutfits.sort(Comparator.comparing(analyzed -> signature(analyzed.outfit)));
        final Random random = new Random(inputData.getSeed());
        final AnalyzedOutfit selected = bestOutfits.get(random.nextInt(bestOutfits.size()));
        outputBoundary.prepareSuccessView(new RecommendationOutputData(
                selected.outfit,
                buildReason(selected.analysis)
        ));
    }

    private CandidatePools buildCandidatePools(RecommendationContext context) {
        final List<InnerTopwear> innerTopwears = itemsOfType(InnerTopwear.class);
        final List<Bottomwear> bottomwears = itemsOfType(Bottomwear.class);
        final List<Footwear> footwears = itemsOfType(Footwear.class);
        if (innerTopwears.isEmpty() || bottomwears.isEmpty() || footwears.isEmpty()) {
            return CandidatePools.missingRequiredItems();
        }

        final Weather weather = context.getWeather();

        final List<Bottomwear> eligibleBottomwears = new ArrayList<>();
        for (Bottomwear bottomwear : bottomwears) {
            if (!WeatherSuitability.requiresLongBottomwear(weather.getTemperature()) || bottomwear.isLong()) {
                eligibleBottomwears.add(bottomwear);
            }
        }

        final List<Footwear> eligibleFootwears = new ArrayList<>();
        for (Footwear footwear : footwears) {
            if (!WeatherSuitability.requiresWaterproofFootwear(weather.getPrecipitation())
                    || footwear.isWaterproof()) {
                eligibleFootwears.add(footwear);
            }
        }

        final List<OuterTopwear> eligibleOuterTopwears = eligibleOuterTopwears(weather);
        final List<Headwear> headwears = optionalItemsOfType(Headwear.class);
        final List<Accessory> accessories = itemsOfType(Accessory.class);

        return new CandidatePools(
                innerTopwears,
                eligibleOuterTopwears,
                eligibleBottomwears,
                eligibleFootwears,
                headwears,
                accessories,
                false
        );
    }

    private List<OuterTopwear> eligibleOuterTopwears(Weather weather) {
        final List<OuterTopwear> outerTopwears = itemsOfType(OuterTopwear.class);
        if (!WeatherSuitability.requiresOuterTopwear(weather.getTemperature())) {
            return optionalItems(outerTopwears);
        }

        final List<OuterTopwear> eligible = new ArrayList<>();
        for (OuterTopwear outerTopwear : outerTopwears) {
            if (!WeatherSuitability.requiresThickOuterTopwear(weather.getTemperature())
                    || outerTopwear.isThick()) {
                eligible.add(outerTopwear);
            }
        }
        return eligible;
    }

    private List<AnalyzedOutfit> findBestOutfits(CandidatePools pools,
                                                  RecommendationContext context) {
        final BestOutfits bestOutfits = new BestOutfits();

        for (InnerTopwear inner : pools.innerTopwears) {
            for (OuterTopwear outer : pools.outerTopwears) {
                analyzeBottomAndFootwearChoices(
                        inner, outer, pools, context, bestOutfits
                );
            }
        }
        return bestOutfits.outfits;
    }

    private void analyzeBottomAndFootwearChoices(InnerTopwear inner,
                                                 OuterTopwear outer,
                                                 CandidatePools pools,
                                                 RecommendationContext context,
                                                 BestOutfits bestOutfits) {
        for (Bottomwear bottom : pools.bottomwears) {
            for (Footwear footwear : pools.footwears) {
                analyzeHeadwearChoices(
                        inner, outer, bottom, footwear, pools, context, bestOutfits
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
                                        BestOutfits bestOutfits) {
        for (Headwear headwear : pools.headwears) {
            final OutfitBase base = new OutfitBase(
                    inner, outer, bottom, footwear, headwear
            );
            analyzeAccessoryChoices(
                    base,
                    pools.accessories,
                    0,
                    new ArrayList<>(),
                    context,
                    bestOutfits
            );
        }
    }

    private void analyzeAccessoryChoices(OutfitBase base,
                                         List<Accessory> accessories,
                                         int index,
                                         List<Accessory> selectedAccessories,
                                         RecommendationContext context,
                                         BestOutfits bestOutfits) {
        if (index == accessories.size()) {
            final Outfit candidate = base.toOutfit(selectedAccessories);
            final OutfitAnalysis analysis = analyze(candidate, context);
            if (analysis.isAcceptable()) {
                bestOutfits.consider(candidate, analysis);
            }
            return;
        }

        analyzeAccessoryChoices(
                base, accessories, index + 1, selectedAccessories, context, bestOutfits
        );
        selectedAccessories.add(accessories.get(index));
        analyzeAccessoryChoices(
                base, accessories, index + 1, selectedAccessories, context, bestOutfits
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

    private <T extends AbstractWear> List<T> optionalItemsOfType(Class<T> itemType) {
        return optionalItems(itemsOfType(itemType));
    }

    private static <T extends AbstractWear> List<T> optionalItems(List<T> items) {
        final List<T> optionalItems = new ArrayList<>();
        optionalItems.add(null);
        optionalItems.addAll(items);
        return optionalItems;
    }

    private static String signature(Outfit outfit) {
        final List<String> identifiers = new ArrayList<>();
        identifiers.add(outfit.getTopwearInner().getUuid().toString());
        identifiers.add(identifier(outfit.getTopwearOuter()));
        identifiers.add(outfit.getBottomwear().getUuid().toString());
        identifiers.add(outfit.getFootwear().getUuid().toString());
        identifiers.add(identifier(outfit.getHeadwear()));
        outfit.getAccessories().stream()
                .map(AbstractWear::getUuid)
                .map(UUID::toString)
                .sorted()
                .forEach(identifiers::add);
        return String.join("|", identifiers);
    }

    private static String identifier(AbstractWear item) {
        String result = "";
        if (item != null) {
            result = item.getUuid().toString();
        }
        return result;
    }

    private static final class AnalyzedOutfit {
        private final Outfit outfit;
        private final OutfitAnalysis analysis;

        private AnalyzedOutfit(Outfit outfit, OutfitAnalysis analysis) {
            this.outfit = outfit;
            this.analysis = analysis;
        }
    }

    private static final class BestOutfits {
        private final List<AnalyzedOutfit> outfits = new ArrayList<>();
        private OutfitAnalysis bestAnalysis;

        private void consider(Outfit outfit, OutfitAnalysis analysis) {
            int comparison = 1;
            if (bestAnalysis != null) {
                comparison = compare(analysis, bestAnalysis);
            }
            if (comparison > 0) {
                outfits.clear();
                outfits.add(new AnalyzedOutfit(outfit, analysis));
                bestAnalysis = analysis;
            }
            else if (comparison == 0) {
                outfits.add(new AnalyzedOutfit(outfit, analysis));
            }
        }
    }

    private static final class OutfitBase {
        private final InnerTopwear innerTopwear;
        private final OuterTopwear outerTopwear;
        private final Bottomwear bottomwear;
        private final Footwear footwear;
        private final Headwear headwear;

        private OutfitBase(InnerTopwear innerTopwear,
                           OuterTopwear outerTopwear,
                           Bottomwear bottomwear,
                           Footwear footwear,
                           Headwear headwear) {
            this.innerTopwear = innerTopwear;
            this.outerTopwear = outerTopwear;
            this.bottomwear = bottomwear;
            this.footwear = footwear;
            this.headwear = headwear;
        }

        private Outfit toOutfit(List<Accessory> accessories) {
            return new Outfit(
                    innerTopwear,
                    outerTopwear,
                    bottomwear,
                    footwear,
                    headwear,
                    List.copyOf(accessories)
            );
        }
    }

    private static final class CandidatePools {
        private final List<InnerTopwear> innerTopwears;
        private final List<OuterTopwear> outerTopwears;
        private final List<Bottomwear> bottomwears;
        private final List<Footwear> footwears;
        private final List<Headwear> headwears;
        private final List<Accessory> accessories;
        private final boolean missingRequiredItems;

        private CandidatePools(List<InnerTopwear> innerTopwears,
                               List<OuterTopwear> outerTopwears,
                               List<Bottomwear> bottomwears,
                               List<Footwear> footwears,
                               List<Headwear> headwears,
                               List<Accessory> accessories,
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
                    List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), true
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
