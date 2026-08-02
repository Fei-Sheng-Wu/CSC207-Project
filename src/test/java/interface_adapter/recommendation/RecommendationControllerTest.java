package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import interface_adapter.recommendation_context.ContextBasedRecommendationController;
import org.junit.jupiter.api.Test;

import entity.WearColor;
import entity.WearStyle;
import use_case.recommendation_context.ContextBasedRecommendationInputBoundary;
import use_case.recommendation_context.ContextBasedRecommendationInputData;

/**
 * Tests for the recommendation controller.
 */
class RecommendationControllerTest {
    @Test
    void packagesPreferencesIntoInputDataAndCallsTheBoundary() {
        final CapturingInputBoundary inputBoundary = new CapturingInputBoundary();
        final ContextBasedRecommendationController controller =
                new ContextBasedRecommendationController(inputBoundary, new Random(1));

        controller.recommend(List.of(WearColor.RED), List.of(WearStyle.FORMAL));

        assertNotNull(inputBoundary.received);
        assertEquals(List.of(WearColor.RED), inputBoundary.received.getPreferredColors());
        assertEquals(List.of(WearStyle.FORMAL), inputBoundary.received.getPreferredStyles());
    }

    @Test
    void acceptsEmptyPreferences() {
        final CapturingInputBoundary inputBoundary = new CapturingInputBoundary();
        final ContextBasedRecommendationController controller =
                new ContextBasedRecommendationController(inputBoundary, new Random(1));

        controller.recommend(List.of(), List.of());

        assertNotNull(inputBoundary.received);
        assertTrue(inputBoundary.received.getPreferredColors().isEmpty());
        assertTrue(inputBoundary.received.getPreferredStyles().isEmpty());
    }

    @Test
    void drawsTheTieBreakingSeedFromTheInjectedRandom() {
        final CapturingInputBoundary first = new CapturingInputBoundary();
        final CapturingInputBoundary second = new CapturingInputBoundary();

        new ContextBasedRecommendationController(first, new Random(42)).recommend(List.of(), List.of());
        new ContextBasedRecommendationController(second, new Random(42)).recommend(List.of(), List.of());

        assertEquals(first.received.getSeed(), second.received.getSeed());
        assertEquals(new Random(42).nextInt(), first.received.getSeed());
    }

    private static final class CapturingInputBoundary
            implements ContextBasedRecommendationInputBoundary {
        private ContextBasedRecommendationInputData received;

        @Override
        public void recommend(ContextBasedRecommendationInputData inputData) {
            received = inputData;
        }
    }
}
