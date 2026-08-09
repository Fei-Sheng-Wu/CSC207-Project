package use_case.wardrobe_adder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class WardrobeAdderInteractorTest {
    @Test
    public void testAddItemAddsItemToWardrobe() {
        final FakeWardrobeDataAccessObject repository = new FakeWardrobeDataAccessObject();
        final FakeWardrobeAdderPresenter presenter = new FakeWardrobeAdderPresenter();
        final WardrobeAdderInteractor interactor = new WardrobeAdderInteractor(repository, presenter);

        final AbstractWear item = new InnerTopwear(UUID.randomUUID());
        item.setName("Blue Shirt");

        interactor.addItem(new WardrobeAdderInputData(item));

        assertEquals(1, repository.wardrobe.getItems().size());
        assertSame(item, repository.wardrobe.getItems().get(0));
    }

    @Test
    public void testAddItemSavesWardrobe() {
        final FakeWardrobeDataAccessObject repository = new FakeWardrobeDataAccessObject();
        final FakeWardrobeAdderPresenter presenter = new FakeWardrobeAdderPresenter();
        final WardrobeAdderInteractor interactor = new WardrobeAdderInteractor(repository, presenter);

        final AbstractWear item = new InnerTopwear(UUID.randomUUID());

        interactor.addItem(new WardrobeAdderInputData(item));

        assertTrue(repository.saveWardrobeCalled);
        assertSame(repository.wardrobe, repository.savedWardrobe);
    }

    @Test
    public void testAddItemCallsSuccessView() {
        final FakeWardrobeDataAccessObject repository = new FakeWardrobeDataAccessObject();
        final FakeWardrobeAdderPresenter presenter = new FakeWardrobeAdderPresenter();
        final WardrobeAdderInteractor interactor = new WardrobeAdderInteractor(repository, presenter);

        final AbstractWear item = new InnerTopwear(UUID.randomUUID());

        interactor.addItem(new WardrobeAdderInputData(item));

        assertTrue(presenter.prepareSuccessViewCalled);
        assertSame(item, presenter.addedItem);
    }

    private static final class FakeWardrobeDataAccessObject implements WardrobeDataAccessInterface {
        private final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());
        private boolean saveWardrobeCalled;
        private Wardrobe savedWardrobe;

        @Override
        public Wardrobe fetchWardrobe() {
            return wardrobe;
        }

        @Override
        public void saveWardrobe(Wardrobe wardrobe) {
            this.saveWardrobeCalled = true;
            this.savedWardrobe = wardrobe;
        }
    }

    private static final class FakeWardrobeAdderPresenter implements WardrobeAdderOutputBoundary {
        private boolean prepareSuccessViewCalled;
        private AbstractWear addedItem;
        private boolean prepareFailViewCalled;
        private String failMessage;

        @Override
        public void prepareSuccessView(AbstractWear wear) {
            this.prepareSuccessViewCalled = true;
            this.addedItem = wear;
        }

        @Override
        public void prepareFailView(String message) {
            this.prepareFailViewCalled = true;
            this.failMessage = message;
        }
    }
}
