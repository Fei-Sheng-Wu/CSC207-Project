package use_case.context_based_recommendation;

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
import entity.Wardrobe;
import entity.WearCondition;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationResponse;

/**
 * Use case interactor for deterministic, context-based outfit recommendations.
 */
public final class ContextBasedRecommendationProcessor implements ContextBasedRecommendationInputBoundary {
    private static final String MISSING_REQUIRED_ITEMS =
            "A recommendation requires at least one inner topwear, bottomwear, and footwear.";
    private static final String NO_SUITABLE_OUTFIT =
            "No outfit in the wardrobe is suitable for the current context.";

    private final Wardrobe wardrobe;
    private final ContextProvider contextProvider;
    private final RecommendationOutputBoundary outputBoundary;
    private final List<OutfitAnalyzer> analyzers;

    public ContextBasedRecommendationProcessor(Wardrobe wardrobe,
                                               ContextProvider contextProvider,
                                               RecommendationOutputBoundary outputBoundary,
                                               List<OutfitAnalyzer> analyzers) {
        this.wardrobe = wardrobe;
        this.contextProvider = contextProvider;
        this.outputBoundary = outputBoundary;
        this.analyzers = List.copyOf(analyzers);
    }

    @Override
    public void recommend(ContextBasedRecommendationRequest request) {
        final RecommendationContext context = new RecommendationContext(
                contextProvider.getCurrentWeather(),
                contextProvider.getCurrentEvents(),
                request.getPreferredColors(),
                request.getPreferredStyles()
        );
        final List<Outfit> candidates = buildCandidates();
        if (candidates.isEmpty()) {
            outputBoundary.prepareFailView(MISSING_REQUIRED_ITEMS);
            return;
        }

        final List<AnalyzedOutfit> bestOutfits = findBestOutfits(candidates, context);
        if (bestOutfits.isEmpty()) {
            outputBoundary.prepareFailView(NO_SUITABLE_OUTFIT);
            return;
        }

        bestOutfits.sort(Comparator.comparing(analyzed -> signature(analyzed.outfit)));
        final Random random = new Random(request.getSeed());
        final AnalyzedOutfit selected = bestOutfits.get(random.nextInt(bestOutfits.size()));
        outputBoundary.prepareSuccessView(new RecommendationResponse(
                selected.outfit,
                buildReason(selected.analysis)
        ));
    }

    private List<Outfit> buildCandidates() {
        final List<InnerTopwear> innerTopwears = itemsOfType(InnerTopwear.class);
        final List<Bottomwear> bottomwears = itemsOfType(Bottomwear.class);
        final List<Footwear> footwears = itemsOfType(Footwear.class);
        if (innerTopwears.isEmpty() || bottomwears.isEmpty() || footwears.isEmpty()) {
            return List.of();
        }

        final List<OuterTopwear> outerTopwears = optionalItemsOfType(OuterTopwear.class);
        final List<Headwear> headwears = optionalItemsOfType(Headwear.class);
        final List<List<Accessory>> accessorySets = accessorySubsets(itemsOfType(Accessory.class));
        final List<Outfit> candidates = new ArrayList<>();

        for (InnerTopwear inner : innerTopwears) {
            for (OuterTopwear outer : outerTopwears) {
                for (Bottomwear bottom : bottomwears) {
                    for (Footwear footwear : footwears) {
                        for (Headwear headwear : headwears) {
                            for (List<Accessory> accessories : accessorySets) {
                                candidates.add(new Outfit(
                                        inner,
                                        outer,
                                        bottom,
                                        footwear,
                                        headwear,
                                        accessories
                                ));
                            }
                        }
                    }
                }
            }
        }
        return candidates;
    }

    private List<AnalyzedOutfit> findBestOutfits(List<Outfit> candidates, RecommendationContext context) {
        final List<AnalyzedOutfit> bestOutfits = new ArrayList<>();
        OutfitAnalysis bestAnalysis = null;

        for (Outfit candidate : candidates) {
            final OutfitAnalysis analysis = analyze(candidate, context);
            if (!analysis.isAcceptable()) {
                continue;
            }

            final int comparison = bestAnalysis == null ? 1 : compare(analysis, bestAnalysis);
            if (comparison > 0) {
                bestOutfits.clear();
                bestOutfits.add(new AnalyzedOutfit(candidate, analysis));
                bestAnalysis = analysis;
            }
            else if (comparison == 0) {
                bestOutfits.add(new AnalyzedOutfit(candidate, analysis));
            }
        }
        return bestOutfits;
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
        return wardrobe.getItems().stream()
                .filter(item -> item.getCondition() != WearCondition.DAMAGED)
                .filter(itemType::isInstance)
                .map(itemType::cast)
                .sorted(Comparator.comparing(item -> item.getUuid().toString()))
                .toList();
    }

    private <T extends AbstractWear> List<T> optionalItemsOfType(Class<T> itemType) {
        final List<T> optionalItems = new ArrayList<>();
        optionalItems.add(null);
        optionalItems.addAll(itemsOfType(itemType));
        return optionalItems;
    }

    private static List<List<Accessory>> accessorySubsets(List<Accessory> accessories) {
        final List<List<Accessory>> subsets = new ArrayList<>();
        collectAccessorySubsets(accessories, 0, new ArrayList<>(), subsets);
        return subsets;
    }

    private static void collectAccessorySubsets(List<Accessory> accessories,
                                                int index,
                                                List<Accessory> current,
                                                List<List<Accessory>> subsets) {
        if (index == accessories.size()) {
            subsets.add(List.copyOf(current));
            return;
        }

        collectAccessorySubsets(accessories, index + 1, current, subsets);
        current.add(accessories.get(index));
        collectAccessorySubsets(accessories, index + 1, current, subsets);
        current.remove(current.size() - 1);
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
        return item == null ? "" : item.getUuid().toString();
    }

    private static final class AnalyzedOutfit {
        private final Outfit outfit;
        private final OutfitAnalysis analysis;

        private AnalyzedOutfit(Outfit outfit, OutfitAnalysis analysis) {
            this.outfit = outfit;
            this.analysis = analysis;
        }
    }
}
