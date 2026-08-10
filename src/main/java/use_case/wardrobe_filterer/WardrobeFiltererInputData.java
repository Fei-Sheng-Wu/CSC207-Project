package use_case.wardrobe_filterer;

public class WardrobeFiltererInputData {
    private final String name;
    private final String category;
    private final String condition;
    private final int purchaseMonth;
    private final String tag;

    public WardrobeFiltererInputData(
        String name,
        String category,
        String condition,
        int purchaseMonth,
        String tag
    ) {
        this.name = name;
        this.category = category;
        this.condition = condition;
        this.purchaseMonth = purchaseMonth;
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public String getName() {
        return name;
    }

    public int getPurchaseMonth() {
        return purchaseMonth;
    }

    public String getTag() {
        return tag;
    }
}
