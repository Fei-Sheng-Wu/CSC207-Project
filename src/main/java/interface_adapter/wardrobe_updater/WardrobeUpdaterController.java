package interface_adapter.wardrobe_updater;

import java.util.UUID;

import entity.AbstractWear;
import entity.WearColor;
import entity.WearCondition;
import entity.WearFactory;
import entity.WearStyle;
import use_case.wardrobe_updater.WardrobeUpdaterInputBoundary;
import use_case.wardrobe_updater.WardrobeUpdaterInputData;

/**
 * Controller for updating wardrobe items.
 */
public class WardrobeUpdaterController {
    private final WardrobeUpdaterInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeUpdaterController(WardrobeUpdaterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the update item use case.
     *
     * @param uuidText      the UUID text
     * @param type          the clothing item type
     * @param name          the clothing item name
     * @param brand         the clothing item brand
     * @param colorText     the clothing item color
     * @param styleText     the clothing item style
     * @param conditionText the clothing item condition
     */
    public void updateItem(
        String uuidText,
        String type,
        String name,
        String brand,
        String colorText,
        String styleText,
        String conditionText
    ) {
        final UUID uuid = UUID.fromString(uuidText.trim());
        final AbstractWear item = WearFactory.constructWear(type, uuid);

        item.setName(name.trim());
        item.setBrand(brand.trim());

        if (!colorText.isBlank()) {
            item.setColor(WearColor.valueOf(colorText.trim().toUpperCase()));
        }
        if (!styleText.isBlank()) {
            item.setStyle(WearStyle.valueOf(styleText.trim().toUpperCase()));
        }
        if (!conditionText.isBlank()) {
            item.setCondition(WearCondition.valueOf(conditionText.trim().toUpperCase()));
        }

        // @TODO: more fields regarding clothing item properties

        interactor.updateItem(new WardrobeUpdaterInputData(item));
    }
}
