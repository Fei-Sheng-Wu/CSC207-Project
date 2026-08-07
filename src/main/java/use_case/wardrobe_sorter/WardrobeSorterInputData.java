package use_case.wardrobe_sorter;

public class WardrobeSorterInputData {
    private final String sortBy;

    public WardrobeSorterInputData(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }
}
