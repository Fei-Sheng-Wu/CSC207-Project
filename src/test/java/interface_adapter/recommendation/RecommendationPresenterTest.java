package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.Bottomwear;
import entity.Footwear;
import entity.InnerTopwear;
import entity.Outfit;
import use_case.recommendation.RecommendationOutputData;

/**
 * Tests for the recommendation presenter and the state it writes into the view model.
 */
class RecommendationPresenterTest {
    @Test
    void successPutsTheOutfitAndReasonOnTheViewModel() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final RecommendationPresenter presenter = new RecommendationPresenter(viewModel);
        final Outfit outfit = outfit();

        presenter.prepareSuccessView(new RecommendationOutputData(outfit, "Because it is warm."));

        assertSame(outfit, viewModel.getOutfit());
        assertEquals("Because it is warm.", viewModel.getReason());
        assertTrue(viewModel.getErrorMessage().isEmpty());
    }

    @Test
    void failurePutsTheMessageOnTheViewModelAndClearsAnyOutfit() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final RecommendationPresenter presenter = new RecommendationPresenter(viewModel);

        presenter.prepareSuccessView(new RecommendationOutputData(outfit(), "Because it is warm."));
        presenter.prepareFailView("No outfit is suitable.");

        assertNull(viewModel.getOutfit());
        assertTrue(viewModel.getReason().isEmpty());
        assertEquals("No outfit is suitable.", viewModel.getErrorMessage());
    }

    @Test
    void notifiesListenersOnBothOutcomes() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final RecommendationPresenter presenter = new RecommendationPresenter(viewModel);
        final List<String> firedProperties = new ArrayList<>();
        viewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                firedProperties.add(event.getPropertyName());
            }
        });

        presenter.prepareSuccessView(new RecommendationOutputData(outfit(), "Because it is warm."));
        presenter.prepareFailView("No outfit is suitable.");

        assertEquals(
                List.of(
                        RecommendationViewModel.PROPERTY_RECOMMENDATION,
                        RecommendationViewModel.PROPERTY_ERROR_MESSAGE
                ),
                firedProperties
        );
    }

    private static Outfit outfit() {
        return new Outfit(
                new InnerTopwear(UUID.randomUUID()),
                null,
                new Bottomwear(UUID.randomUUID()),
                new Footwear(UUID.randomUUID()),
                null,
                List.of()
        );
    }
}
