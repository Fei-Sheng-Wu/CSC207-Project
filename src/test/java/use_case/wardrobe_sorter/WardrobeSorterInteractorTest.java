package use_case.wardrobe_sorter;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearFactory;
import org.junit.jupiter.api.Test;
import use_case.data_access.MockWardrobeRepository;
import use_case.wardrobe.WardrobeDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WardrobeSorterInteractorTest {

    private void wardrobeSorterTestHelper(List<AbstractWear> inputItems, String sortBy, String ... expectedNames) {
        WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        WardrobeSorterOutputBoundary successPresenter = outputData -> {
            List<AbstractWear> sortedItems = outputData.getSortedItems();
            assertEquals(expectedNames.length, sortedItems.size());
            for (int i = 0; i < expectedNames.length; i++) {
                assertEquals(expectedNames[i], sortedItems.get(i).getName());
            }
        };

        WardrobeSorterInputBoundary interactor = new WardrobeSorterInteractor(repository, successPresenter);
        interactor.sortWardrobe(new WardrobeSorterInputData(sortBy));
    }

    @Test
    void sortEmptyWardrobeTest() {
        wardrobeSorterTestHelper(List.of(), "NAME_ASC");
    }

    @Test
    void sortSingleItemTest() {
        final AbstractWear item = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        item.setName("Very Cool Jacket");
        wardrobeSorterTestHelper(List.of(item), "NAME_ASC", "Very Cool Jacket");
    }

    @Test
    void sortByNameAscendingTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        final AbstractWear item3 = WearFactory.constructWear("footwear", UUID.randomUUID());
        item1.setName("Zebra Shirt");
        item2.setName("Apple Jacket");
        item3.setName("Pineapple Shoes");
        wardrobeSorterTestHelper(List.of(item1, item2, item3),
                    "NAME_ASC",
              "Apple Jacket",
            "Pineapple Shoes",
               "Zebra Shirt");
    }

    @Test
    void sortByNameDescendingTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        final AbstractWear item3 = WearFactory.constructWear("footwear", UUID.randomUUID());
        item1.setName("Zebra Shirt");
        item2.setName("Apple Jacket");
        item3.setName("Pineapple Shoes");

        wardrobeSorterTestHelper(List.of(item1, item2, item3),
            "NAME_DESC",
            "Zebra Shirt",
            "Pineapple Shoes",
            "Apple Jacket");
    }

    @Test
    void sortByBrandAscendingWithNullBrandTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        final AbstractWear item3 = WearFactory.constructWear("footwear", UUID.randomUUID());

        item1.setName("No Brand Shirt");
        item1.setBrand(null);
        item2.setName("Nike Shirt");
        item2.setBrand("Nike");
        item3.setName("Pineapple Shoes");
        item3.setBrand("Pineapple");

        wardrobeSorterTestHelper(List.of(item2, item1, item3),
            "BRAND_ASC",
            "No Brand Shirt",
            "Nike Shirt",
            "Pineapple Shoes");
    }
}
