package use_case.wardrobe_filterer;

public class WardrobeFiltererInputData {
    private final String category;
    private final String condition;
    private final String name;
    private final int purchaseMonth;
    private final String tag;

    public WardrobeFiltererInputData(String category,
                                     String condition,
                                     String name,
                                     int purchaseMonth,
                                     String tag) {
        this.category = category;
        this.condition = condition;
        this.name = name;
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
