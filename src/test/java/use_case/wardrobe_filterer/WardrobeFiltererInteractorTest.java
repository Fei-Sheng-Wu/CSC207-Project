package use_case.wardrobe_filterer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearCondition;
import entity.WearFactory;
import use_case.data_access.MockWardrobeRepository;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class WardrobeFiltererInteractorTest {

    private void wardrobeFiltererTestHelper(List<AbstractWear> inputItems,
                                            WardrobeFiltererInputData filteringCriteria,
                                            String... expectedNames) {
        final WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        final WardrobeFiltererInputBoundary interactor = new WardrobeFiltererInteractor(repository, outputData -> {
            final List<AbstractWear> filteredItems = outputData.getFilteredItems();

            assertEquals(expectedNames.length, filteredItems.size());
            for (int i = 0; i < expectedNames.length; i++) {
                assertEquals(expectedNames[i], filteredItems.get(i).getName());
            }
        });

        interactor.filterItems(filteringCriteria);
    }

    @Test
    void filterEmptyWardrobeTest() {
        final WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );
        wardrobeFiltererTestHelper(List.of(), filteringCriteria);
    }

    @Test
    void filterSingleItemByCategoryTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        item1.setName("Nike Cool Shoes");
        item1.setCondition(WearCondition.NEW);
        item1.setTags(List.of("running"));

        final WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );

        wardrobeFiltererTestHelper(List.of(item1), filteringCriteria, "Nike Cool Shoes");
    }

    @Test
    void filterNoMatchesTest() {
        final AbstractWear item1 = WearFactory.constructWear("bottomwear", UUID.randomUUID());
        item1.setName("Blue pants");
        item1.setBrand("Adidas");

        final WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );

        wardrobeFiltererTestHelper(List.of(item1), filteringCriteria);
    }

    @Test
    void filterMultipleItemsMatchesSubsetTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item3 = WearFactory.constructWear("bottomwear", UUID.randomUUID());

        item1.setName("Nike Jacket");
        item1.setBrand("Nike");
        item2.setName("Adidas T-Shirt");
        item2.setBrand("adidas");
        item3.setName("Blue pants");
        item3.setBrand("PUMA");

        // Filter for "innertopwear" should match item1 and item2, but exclude item3
        final WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );

        wardrobeFiltererTestHelper(List.of(item1, item2, item3), filteringCriteria,
            "Nike Jacket",
            "Adidas T-Shirt");
    }

    @Test
    void filterNameMatchBranchesTest() {
        final AbstractWear item = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        item.setName("Red Shirt");

        // Branch 1: Name is empty string (Should match and return the shirt)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, ""),
            "Red Shirt");

        // Branch 2: Substring match case-insensitive (Should match and return the shirt)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "red s", 0, ""),
            "Red Shirt");

        // Branch 3: No match (Should filter out the shirt and return nothing)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "blue jacket", 0, ""));
    }

    @Test
    void filterCategoryMatchBranchesTest() {
        final AbstractWear item = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        item.setName("Category Shirt");

        // Branch 1: "All Categories" (Should match and return the shirt)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, ""),
            "Category Shirt");

        // Branch 2: Specific category match (Should match and return the shirt)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                item.getClass().getSimpleName(), "All Conditions", "", 0, ""),
            "Category Shirt");

        // Branch 3: Category mismatch (Should filter out the shirt and return nothing)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                "footwear", "All Conditions", "", 0, ""));

        // Branch 4: Category is null (Should match)
        wardrobeFiltererTestHelper(List.of(item),
            new WardrobeFiltererInputData(
                null, "All Conditions", "", 0, ""),
            "Category Shirt");
    }

    @Test
    void filterConditionMatchBranchesTest() {
        final AbstractWear itemNew = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemNew.setName("New Shirt");
        itemNew.setCondition(WearCondition.NEW);

        final AbstractWear itemNullCond = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemNullCond.setName("Null Condition Shirt");
        itemNullCond.setCondition(null);

        // Branch 1: empty string
        wardrobeFiltererTestHelper(
            List.of(itemNew, itemNullCond),
            new WardrobeFiltererInputData(
                "All Categories", "", "", 0, ""
            ),
            "New Shirt", "Null Condition Shirt"
        );

        // Branch 2: "All Conditions"
        wardrobeFiltererTestHelper(
            List.of(itemNew, itemNullCond),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, ""
            ),
            "New Shirt", "Null Condition Shirt"
        );

        // Branch 3: Item condition is null, but criteria is specific
        wardrobeFiltererTestHelper(
            List.of(itemNullCond),
            new WardrobeFiltererInputData(
                "All Categories", "NEW", "", 0, ""
            )
        );

        // Branch 4: Exact condition match (case-insensitive)
        wardrobeFiltererTestHelper(
            List.of(itemNew),
            new WardrobeFiltererInputData(
                "All Categories", "new", "", 0, ""
            ),
            "New Shirt"
        );

        // Branch 5: Condition mismatch
        wardrobeFiltererTestHelper(
            List.of(itemNew),
            new WardrobeFiltererInputData(
                "All Categories", "USED", "", 0, ""
            )
        );
    }

    @Test
    void filterTagMatchBranchesTest() {
        final AbstractWear itemTagged = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemTagged.setName("Tagged Shirt");
        itemTagged.setTags(List.of("winter", "casual"));

        final AbstractWear itemNullTags = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemNullTags.setName("Null Tags Shirt");
        itemNullTags.setTags(null);

        final AbstractWear itemEmptyTags = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemEmptyTags.setName("Empty Tags Shirt");
        itemEmptyTags.setTags(new ArrayList<>());

        // Branch 1: Criteria tag is empty string
        wardrobeFiltererTestHelper(
            List.of(itemTagged),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, ""
            ),
            "Tagged Shirt"
        );

        // Branch 2: Item tags are null (should fail)
        wardrobeFiltererTestHelper(
            List.of(itemNullTags),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, "winter"
            )
        );

        // Branch 3: Item tags are empty (should fail)
        wardrobeFiltererTestHelper(
            List.of(itemEmptyTags),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, "winter"
            )
        );

        // Branch 4: Tag matches inside list (case-insensitive)
        wardrobeFiltererTestHelper(
            List.of(itemTagged),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, "WIN"
            ),
            "Tagged Shirt"
        );

        // Branch 5: Tag does not match any in list
        wardrobeFiltererTestHelper(
            List.of(itemTagged),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, "summer"
            )
        );
    }

    @Test
    void filterMonthMatchBranchesTest() {
        final AbstractWear itemWithDate = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemWithDate.setName("Aged Shirt");
        // Sets purchase date to 12 months ago to make age non-null
        itemWithDate.setPurchaseDate(java.time.LocalDate.now().minusMonths(12));

        final AbstractWear itemNoDate = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        itemNoDate.setName("No Date Shirt");
        // Forces age to be null (hits else branch)
        itemNoDate.setPurchaseDate(null);

        // Branch 1: purchaseMonth <= 0 (Skips the month check entirely)
        wardrobeFiltererTestHelper(
            List.of(itemWithDate),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 0, ""
            ),
            "Aged Shirt"
        );

        // Branch 2: purchaseMonth > 0, but purchaseDate is null
        wardrobeFiltererTestHelper(
            List.of(itemNoDate),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 5, ""
            )
        );

        // Branch 3: purchaseMonth > 0, age non-null, totalMonths < filterCriteria (False)
        wardrobeFiltererTestHelper(
            List.of(itemWithDate),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 24, ""
            )
        );

        // Branch 4: purchaseMonth > 0, age non-null, totalMonths >= filterCriteria (True)
        wardrobeFiltererTestHelper(
            List.of(itemWithDate),
            new WardrobeFiltererInputData(
                "All Categories", "All Conditions", "", 6, ""
            ),
            "Aged Shirt"
        );
    }
}
