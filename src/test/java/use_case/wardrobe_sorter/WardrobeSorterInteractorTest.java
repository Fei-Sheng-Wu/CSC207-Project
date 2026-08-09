package use_case.wardrobe_sorter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WardrobeSort;
import entity.WearFactory;
import use_case.data_access.MockWardrobeRepository;
import use_case.wardrobe.WardrobeDataAccessInterface;

class WardrobeSorterInteractorTest {

    private void wardrobeSorterTestHelper(List<AbstractWear> inputItems, WardrobeSort sortBy, String... expectedNames) {
        final WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        final WardrobeSorterOutputBoundary successPresenter = outputData -> {
            final List<AbstractWear> sortedItems = outputData.getSortedItems();
            assertEquals(expectedNames.length, sortedItems.size());
            for (int i = 0; i < expectedNames.length; i++) {
                assertEquals(expectedNames[i], sortedItems.get(i).getName());
            }
        };

        final WardrobeSorterInputBoundary interactor = new WardrobeSorterInteractor(
            repository,
            successPresenter);
        interactor.sortWardrobe(new WardrobeSorterInputData(sortBy));
    }

    @Test
    void sortEmptyWardrobeTest() {
        wardrobeSorterTestHelper(List.of(), WardrobeSort.NAME_ASC);
    }

    @Test
    void sortSingleItemTest() {
        final AbstractWear item = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        item.setName("Very Cool Jacket");
        wardrobeSorterTestHelper(List.of(item), WardrobeSort.NAME_ASC, "Very Cool Jacket");
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
            WardrobeSort.NAME_ASC,
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
            WardrobeSort.NAME_DESC,
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
            WardrobeSort.BRAND_ASC,
            "No Brand Shirt",
            "Nike Shirt",
            "Pineapple Shoes");
    }

    @Test
    void sortByBrandDescendingWithNullBrandTest() {
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
            WardrobeSort.BRAND_DESC,
            "Pineapple Shoes",
            "Nike Shirt",
            "No Brand Shirt");
    }

    @Test
    void sortByTypeTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        final AbstractWear item3 = WearFactory.constructWear("footwear", UUID.randomUUID());

        item1.setName("Inner Shirt");
        item2.setName("Outer Jacket");
        item3.setName("My Shoes");

        wardrobeSorterTestHelper(List.of(item1, item2, item3),
            WardrobeSort.TYPE,
            "My Shoes",
            "Inner Shirt",
            "Outer Jacket");
    }

    @Test
    void sortByNullTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("outertopwear", UUID.randomUUID());
        final AbstractWear item3 = WearFactory.constructWear("footwear", UUID.randomUUID());

        item1.setName("Inner Shirt");
        item2.setName("Outer Jacket");
        item3.setName("My Shoes");

        wardrobeSorterTestHelper(List.of(item1, item2, item3),
            WardrobeSort.NONE,
            "Inner Shirt",
            "Outer Jacket",
            "My Shoes");
    }
}
