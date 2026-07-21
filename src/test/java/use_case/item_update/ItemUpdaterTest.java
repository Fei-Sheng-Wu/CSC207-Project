package use_case.item_update;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.Wardrobe;
import org.junit.jupiter.api.Test;
import use_case.item_action.ItemActionOutputBoundary;
import use_case.item_action.ItemActionRequest;
import use_case.item_action.ItemActionResponse;
import use_case.item_action.WardrobeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the item update use case.
 */
class ItemUpdaterTest {
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
        final FakeItemActionPresenter presenter = new FakeItemActionPresenter();

        final ItemUpdateInputBoundary updater = new ItemUpdater(repository, presenter);
        updater.updateItem(new ItemActionRequest(updatedItem));

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertTrue(repository.saveCalled);
        assertEquals("Updated Shirt", repository.wardrobe.getItems().get(0).getName());
        assertEquals("Clothing item updated successfully.", presenter.response.getMessage());
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
        final FakeItemActionPresenter presenter = new FakeItemActionPresenter();

        final ItemUpdateInputBoundary updater = new ItemUpdater(repository, presenter);
        updater.updateItem(new ItemActionRequest(missingItem));

        assertFalse(presenter.successCalled);
        assertTrue(presenter.failCalled);
        assertFalse(repository.saveCalled);
        assertEquals("Existing Shirt", repository.wardrobe.getItems().get(0).getName());
        assertEquals("Clothing item could not be found.", presenter.response.getMessage());
    }

    private static class FakeWardrobeRepository implements WardrobeRepository {
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

    private static class FakeItemActionPresenter implements ItemActionOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;
        private ItemActionResponse response;

        @Override
        public void prepareSuccessView(ItemActionResponse response) {
            successCalled = true;
            this.response = response;
        }

        @Override
        public void prepareFailView(ItemActionResponse response) {
            failCalled = true;
            this.response = response;
        }
    }
}
