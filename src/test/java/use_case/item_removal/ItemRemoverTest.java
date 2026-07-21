package use_case.item_removal;

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
 * Tests for the item removal use case.
 */
class ItemRemoverTest {
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
        final FakeItemActionPresenter presenter = new FakeItemActionPresenter();

        final ItemRemovalInputBoundary remover = new ItemRemover(repository, presenter);
        remover.removeItem(new ItemActionRequest(itemToRemove));

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertTrue(repository.saveCalled);
        assertEquals(0, repository.wardrobe.getItems().size());
        assertEquals("Clothing item removed successfully.", presenter.response.getMessage());
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
        final FakeItemActionPresenter presenter = new FakeItemActionPresenter();

        final ItemRemovalInputBoundary remover = new ItemRemover(repository, presenter);
        remover.removeItem(new ItemActionRequest(missingItem));

        assertFalse(presenter.successCalled);
        assertTrue(presenter.failCalled);
        assertFalse(repository.saveCalled);
        assertEquals(1, repository.wardrobe.getItems().size());
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
