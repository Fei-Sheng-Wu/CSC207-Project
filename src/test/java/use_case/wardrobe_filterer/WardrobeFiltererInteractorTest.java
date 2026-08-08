package use_case.wardrobe_filterer;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearCondition;
import entity.WearFactory;
import org.junit.jupiter.api.Test;
import use_case.data_access.MockWardrobeRepository;
import use_case.wardrobe.WardrobeDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WardrobeFiltererInteractorTest {

    private void wardrobeFiltererTestHelper(List<AbstractWear> inputItems,
                                            WardrobeFiltererInputData filteringCriteria,
                                            String... expectedNames) {
        WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        WardrobeFiltererInputBoundary interactor = new WardrobeFiltererInteractor(repository, outputData -> {
            List<AbstractWear> filteredItems = outputData.getFilteredItems();

            assertEquals(expectedNames.length, filteredItems.size());
            for (int i = 0; i < expectedNames.length; i++) {
                assertEquals(expectedNames[i], filteredItems.get(i).getName());
            }
        });

        interactor.filterItems(filteringCriteria);
    }

    @Test
    void filterEmptyWardrobeTest() {
        WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );
        wardrobeFiltererTestHelper(List.of(), filteringCriteria);
    }

    @Test
    void filterSingleItemByCategoryTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        item1.setName("Nike Shoes");
        item1.setCondition(WearCondition.NEW);
        item1.setTags(List.of("running"));

        WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );

        wardrobeFiltererTestHelper(List.of(item1), filteringCriteria, "Nike Shoes");
    }

    @Test
    void filterNoMatchesTest() {
        final AbstractWear item1 = WearFactory.constructWear("bottomwear", UUID.randomUUID());
        item1.setName("Blue pants");
        item1.setBrand("Adidas");

        WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
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
        WardrobeFiltererInputData filteringCriteria = new WardrobeFiltererInputData(
            "innertopwear", null, null, 0, null
        );

        wardrobeFiltererTestHelper(List.of(item1, item2, item3), filteringCriteria,
            "Nike Jacket",
                            "Adidas T-Shirt");
    }
}
