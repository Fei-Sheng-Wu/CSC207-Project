package interface_adapter.wardrobe_remover;

import entity.AbstractWear;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInputData;

/**
 * Controller for removing wardrobe items.
 */
public class WardrobeRemoverController {
    private final WardrobeRemoverInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeRemoverController(WardrobeRemoverInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the remove item use case.
     *
     * @param item the clothing item
     */
    public void removeItem(AbstractWear item) {
        interactor.removeItem(new WardrobeRemoverInputData(item));
    }
}
