package interface_adapter.wardrobe_remover;

import java.util.UUID;

import entity.AbstractWear;
import entity.WearFactory;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInputData;

/**
 * Controller for removing wardrobe items.
 */
public class WardrobeRemoverController {
    private final WardrobeRemoverInputBoundary interactor;

    public WardrobeRemoverController(WardrobeRemoverInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the remove item use case.
     *
     * @param uuidText the UUID text
     * @param type the clothing item type
     */
    public void removeItem(String uuidText, String type) {
        final UUID uuid = UUID.fromString(uuidText.trim());
        final AbstractWear item = WearFactory.constructWear(type, uuid);

        interactor.removeItem(new WardrobeRemoverInputData(item));
    }
}
