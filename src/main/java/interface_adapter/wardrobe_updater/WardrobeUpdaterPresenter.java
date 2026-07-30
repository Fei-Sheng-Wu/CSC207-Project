package interface_adapter.wardrobe_updater;

import java.util.function.Consumer;

import use_case.wardrobe_updater.WardrobeUpdaterOutputBoundary;

/**
 * Presenter for the wardrobe updater use case.
 */
public class WardrobeUpdaterPresenter implements WardrobeUpdaterOutputBoundary {
    private final Consumer<String> messageDisplayer;

    public WardrobeUpdaterPresenter(Consumer<String> messageDisplayer) {
        this.messageDisplayer = messageDisplayer;
    }

    @Override
    public void prepareSuccessView() {
        messageDisplayer.accept("Clothing item updated successfully.");
    }

    @Override
    public void prepareFailView() {
        messageDisplayer.accept("Could not update clothing item. Please check that the UUID exists.");
    }
}
