package interface_adapter.wardrobe_adder;

import java.util.UUID;

import entity.AbstractWear;
import entity.WearFactory;
import use_case.wardrobe_adder.WardrobeAdderInputBoundary;
import use_case.wardrobe_adder.WardrobeAdderInputData;

/**
 * Controller for adding wardrobe items.
 */
public class WardrobeAdderController {
    private final WardrobeAdderInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeAdderController(WardrobeAdderInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the add item use case.
     *
     * @param uuidText the UUID text
     * @param type     the clothing item type
     */
    public void addItem(String uuidText, String type) {
        final UUID uuid = UUID.fromString(uuidText.trim());
        final AbstractWear item = WearFactory.constructWear(type, uuid);

        interactor.addItem(new WardrobeAdderInputData(item));
    }
}
