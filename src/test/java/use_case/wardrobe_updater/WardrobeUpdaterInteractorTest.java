package use_case.wardrobe_updater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Tests for the wardrobe updater use case.
 */
class WardrobeUpdaterInteractorTest {
    @Test
    void updateExistingItemSucceeds() {
        final UUID itemId = UUID.randomUUID();

        final AbstractWear oldItem = new InnerTopwear(itemId);
        oldItem.setName("Old Shirt");

        final AbstractWear updatedItem = new InnerTopwear(itemId);
        updatedItem.setName("Updated Shirt");

        final FakeWardrobeRepository repository = new FakeWardrobeRepository(
                new Wardrobe(new ArrayList<>(List.of(oldItem)))
        );
        final FakeWardrobeUpdaterPresenter presenter = new FakeWardrobeUpdaterPresenter();

        final WardrobeUpdaterInputBoundary interactor = new WardrobeUpdaterInteractor(repository, presenter);
        interactor.updateItem(new WardrobeUpdaterInputData(updatedItem));

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertTrue(repository.saveCalled);
        assertEquals("Updated Shirt", repository.wardrobe.getItems().get(0).getName());
    }

    @Test
    void updateMissingItemFails() {
        final AbstractWear existingItem = new InnerTopwear(UUID.randomUUID());
        existingItem.setName("Existing Shirt");

        final AbstractWear missingItem = new InnerTopwear(UUID.randomUUID());
        missingItem.setName("Missing Shirt");

        final FakeWardrobeRepository repository = new FakeWardrobeRepository(
                new Wardrobe(new ArrayList<>(List.of(existingItem)))
        );
        final FakeWardrobeUpdaterPresenter presenter = new FakeWardrobeUpdaterPresenter();

        final WardrobeUpdaterInputBoundary interactor = new WardrobeUpdaterInteractor(repository, presenter);
        interactor.updateItem(new WardrobeUpdaterInputData(missingItem));

        assertFalse(presenter.successCalled);
        assertTrue(presenter.failCalled);
        assertFalse(repository.saveCalled);
        assertEquals("Existing Shirt", repository.wardrobe.getItems().get(0).getName());
    }

    private static class FakeWardrobeRepository implements WardrobeDataAccessInterface {
        private final Wardrobe wardrobe;
        private boolean saveCalled;

        FakeWardrobeRepository(Wardrobe wardrobe) {
            this.wardrobe = wardrobe;
        }

        @Override
        public Wardrobe fetchWardrobe() {
            return wardrobe;
        }

        @Override
        public void saveWardrobe(Wardrobe newWardrobe) {
            saveCalled = true;
        }
    }

    private static class FakeWardrobeUpdaterPresenter implements WardrobeUpdaterOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;

        @Override
        public void prepareSuccessView() {
            successCalled = true;
        }

        @Override
        public void prepareFailView(String message) {
            failCalled = true;
        }
    }
}
