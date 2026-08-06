package interface_adapter.wardrobe_updater;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private static final double RATIO_PERCENTAGE = 100.0;

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
     * @param item               the clothing item
     * @param type               the type of the clothing item
     * @param name               the name of the clothing item
     * @param brand              the brand of the clothing item
     * @param color              the color of the clothing item
     * @param style              the style of the clothing item
     * @param condition          the condition of the clothing item
     * @param purchaseDateYear   the purchase date year of the clothing item
     * @param purchaseDateMonth  the purchase date month of the clothing item
     * @param purchaseDateDay    the purchase date day of the clothing item
     * @param fondnessPercentage the fondness percentage of the clothing item
     * @param tags               the tags of the clothing item
     */
    public void updateItem(
        AbstractWear item,
        String type,
        String name,
        String brand,
        String color,
        String style,
        String condition,
        Integer purchaseDateYear,
        Integer purchaseDateMonth,
        Integer purchaseDateDay,
        int fondnessPercentage,
        String tags
    ) {
        AbstractWear updated = item;
        if (!type.equals(item.getClass().getSimpleName())) {
            updated = WearFactory.constructWear(type, item.getUuid());
        }

        updated.setName(name.strip());
        updated.setBrand(brand.strip());

        if (color.isBlank()) {
            updated.setColor(null);
        } else {
            updated.setColor(WearColor.valueOf(color.strip().toUpperCase()));
        }
        if (style.isBlank()) {
            updated.setStyle(null);
        } else {
            updated.setStyle(WearStyle.valueOf(style.strip().toUpperCase()));
        }
        if (condition.isBlank()) {
            updated.setCondition(null);
        } else {
            updated.setCondition(WearCondition.valueOf(condition.strip().toUpperCase()));
        }

        if (purchaseDateYear == null || purchaseDateMonth == null || purchaseDateDay == null) {
            updated.setPurchaseDate(null);
        } else {
            updated.setPurchaseDate(LocalDate.of(purchaseDateYear, purchaseDateMonth, purchaseDateDay));
        }

        updated.setFondness(fondnessPercentage / RATIO_PERCENTAGE);

        final List<String> tagsList = new ArrayList<>();
        for (String tag : tags.split(",")) {
            tagsList.add(tag.strip());
        }
        updated.setTags(tagsList);

        interactor.updateItem(new WardrobeUpdaterInputData(updated));
    }
}
