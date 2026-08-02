package use_case.wardrobe_remover;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.Wardrobe;
import org.junit.jupiter.api.Test;
import use_case.wardrobe.WardrobeDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the wardrobe remover use case.
 */
class WardrobeRemoverInteractorTest {
    @Test
    void removeExistingItemSucceeds() {
        final UUID itemId = UUID.randomUUID();

        final AbstractWear storedItem = new InnerTopwear(itemId);
        storedItem.setName("Stored Shirt");

        final AbstractWear itemToRemove = new InnerTopwear(itemId);
        itemToRemove.setName("Stored Shirt");

        final FakeWardrobeRepository repository = new FakeWardrobeRepository(
                new Wardrobe(new ArrayList<>(List.of(storedItem)))
        );
        final FakeWardrobeRemoverPresenter presenter = new FakeWardrobeRemoverPresenter();

        final WardrobeRemoverInputBoundary interactor = new WardrobeRemoverInteractor(repository, presenter);
        interactor.removeItem(new WardrobeRemoverInputData(itemToRemove));

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertTrue(repository.saveCalled);
        assertEquals(0, repository.wardrobe.getItems().size());
    }

    @Test
    void removeMissingItemFails() {
        final AbstractWear existingItem = new InnerTopwear(UUID.randomUUID());
        existingItem.setName("Existing Shirt");

        final AbstractWear missingItem = new InnerTopwear(UUID.randomUUID());
        missingItem.setName("Missing Shirt");

        final FakeWardrobeRepository repository = new FakeWardrobeRepository(
                new Wardrobe(new ArrayList<>(List.of(existingItem)))
        );
        final FakeWardrobeRemoverPresenter presenter = new FakeWardrobeRemoverPresenter();

        final WardrobeRemoverInputBoundary interactor = new WardrobeRemoverInteractor(repository, presenter);
        interactor.removeItem(new WardrobeRemoverInputData(missingItem));

        assertFalse(presenter.successCalled);
        assertTrue(presenter.failCalled);
        assertFalse(repository.saveCalled);
        assertEquals(1, repository.wardrobe.getItems().size());
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
        public void saveWardrobe(Wardrobe wardrobe) {
            saveCalled = true;
        }
    }

    private static class FakeWardrobeRemoverPresenter implements WardrobeRemoverOutputBoundary {
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
