package use_case.tag_based_recommendation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Bottomwear;
import entity.Footwear;
import entity.InnerTopwear;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationResponse;

class TagBasedRecommendationProcessorTest {
    private static final UUID RED_SHIRT_ID =
        UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID BLUE_SHIRT_ID =
        UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID BOTTOMWEAR_ID =
        UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID FOOTWEAR_ID =
        UUID.fromString("00000000-0000-0000-0000-000000000004");

    @Test
    void recommendsHighestScoringRequiredItems() {
        final InnerTopwear matchingShirt = wear(
            new InnerTopwear(RED_SHIRT_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        final InnerTopwear nonMatchingShirt = wear(
            new InnerTopwear(BLUE_SHIRT_ID),
            WearColor.BLUE,
            WearStyle.FORMAL
        );

        final Bottomwear bottomwear = wear(
            new Bottomwear(BOTTOMWEAR_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        final Footwear footwear = wear(
            new Footwear(FOOTWEAR_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        final Wardrobe wardrobe = new Wardrobe(
            new ArrayList<>(List.of(
                nonMatchingShirt,
                matchingShirt,
                bottomwear,
                footwear
            ))
        );

        final CapturingOutputBoundary output =
            new CapturingOutputBoundary();

        final TagBasedRecommendationProcessor processor =
            new TagBasedRecommendationProcessor(wardrobe, output);

        processor.recommend(request(
            List.of(WearColor.RED),
            List.of(WearStyle.CASUAL),
            List.of()
        ));

        assertNull(output.errorMessage);
        assertNotNull(output.response);

        assertSame(
            matchingShirt,
            output.response.getOutfit().getTopwearInner()
        );

        assertSame(
            bottomwear,
            output.response.getOutfit().getBottomwear()
        );

        assertSame(
            footwear,
            output.response.getOutfit().getFootwear()
        );

        assertTrue(
            output.response.getReason().contains("preferences")
        );
    }

    @Test
    void reportsFailureWhenRequiredCategoryIsMissing() {
        final InnerTopwear shirt = wear(
            new InnerTopwear(RED_SHIRT_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        final Footwear footwear = wear(
            new Footwear(FOOTWEAR_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        // There is no bottomwear.
        final Wardrobe wardrobe = new Wardrobe(
            new ArrayList<>(List.of(shirt, footwear))
        );

        final CapturingOutputBoundary output =
            new CapturingOutputBoundary();

        final TagBasedRecommendationProcessor processor =
            new TagBasedRecommendationProcessor(wardrobe, output);

        processor.recommend(request(
            List.of(WearColor.RED),
            List.of(WearStyle.CASUAL),
            List.of()
        ));

        assertNull(output.response);

        assertTrue(
            output.errorMessage.contains(
                "inner topwear, bottomwear, and footwear"
            )
        );
    }

    @Test
    void reportsFailureWhenRequiredItemScoreIsTooLow() {
        final InnerTopwear shirt = wear(
            new InnerTopwear(RED_SHIRT_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        final Bottomwear bottomwear = wear(
            new Bottomwear(BOTTOMWEAR_ID),
            WearColor.RED,
            WearStyle.CASUAL
        );

        // This scores zero for a red and casual request.
        final Footwear footwear = wear(
            new Footwear(FOOTWEAR_ID),
            WearColor.BLUE,
            WearStyle.FORMAL
        );

        final Wardrobe wardrobe = new Wardrobe(
            new ArrayList<>(List.of(
                shirt,
                bottomwear,
                footwear
            ))
        );

        final CapturingOutputBoundary output =
            new CapturingOutputBoundary();

        final TagBasedRecommendationProcessor processor =
            new TagBasedRecommendationProcessor(wardrobe, output);

        processor.recommend(request(
            List.of(WearColor.RED),
            List.of(WearStyle.CASUAL),
            List.of()
        ));

        assertNull(output.response);

        assertTrue(
            output.errorMessage.contains(
                "sufficiently matches"
            )
        );
    }

    private static TagBasedRecommendationRequest request(
        List<WearColor> colors,
        List<WearStyle> styles,
        List<String> tags) {

        final TagBasedRecommendationRequest request =
            new TagBasedRecommendationRequest();

        request.setSeed(0);
        request.setPreferredColors(colors);
        request.setPreferredStyles(styles);
        request.setPreferredTags(tags);

        return request;
    }

    private static <T extends AbstractWear> T wear(
        T item,
        WearColor color,
        WearStyle style) {

        item.setColor(color);
        item.setStyle(style);
        item.setCondition(WearCondition.NEW);

        return item;
    }

    private static final class CapturingOutputBoundary
        implements RecommendationOutputBoundary {

        private RecommendationResponse response;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
            RecommendationResponse recommendationResponse) {

            this.response = recommendationResponse;
        }

        @Override
        public void prepareFailView(String message) {
            this.errorMessage = message;
        }
    }
}
