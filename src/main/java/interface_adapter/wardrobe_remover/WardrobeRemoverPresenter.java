package interface_adapter.wardrobe_remover;

import java.util.function.Consumer;

import use_case.wardrobe_remover.WardrobeRemoverOutputBoundary;

/**
 * Presenter for the wardrobe remover use case.
 */
public class WardrobeRemoverPresenter implements WardrobeRemoverOutputBoundary {
    private final Consumer<String> messageDisplayer;

    public WardrobeRemoverPresenter(Consumer<String> messageDisplayer) {
        this.messageDisplayer = messageDisplayer;
    }

    @Override
    public void prepareSuccessView() {
        messageDisplayer.accept("Clothing item removed successfully.");
    }

    @Override
    public void prepareFailView() {
        messageDisplayer.accept("Could not remove clothing item. Please check that the UUID exists.");
    }
}
