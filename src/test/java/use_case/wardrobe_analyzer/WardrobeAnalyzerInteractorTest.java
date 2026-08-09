package use_case.wardrobe_analyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


public class WardrobeAnalyzerInteractorTest {

    private void wardrobeAnalyzerTestHelper(
        List<AbstractWear> inputItems,
        int expectedTotalCount,
        double expectedMeanFondness,
        int expectedDonationCandidates,
        int expectedOldestAge,
        int expectedNewestAge) {

        final WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        final WardrobeAnalyzerOutputBoundary presenter = new WardrobeAnalyzerOutputBoundary() {
            @Override
            public void prepareSuccessView(WardrobeAnalyzerOutputData outputData) {
                assertEquals(expectedTotalCount, outputData.getTotalItems());
                assertEquals(expectedMeanFondness, outputData.getAverageFondness(), 0.01);
                assertEquals(expectedDonationCandidates, outputData.getDonationCandidateCount());
                assertEquals(expectedOldestAge, outputData.getOldestItemAge());
                assertEquals(expectedNewestAge, outputData.getNewestItemAge());
                assertNotNull(outputData.getCategoryCounts());
                assertNotNull(outputData.getConditionCounts());
            }

            @Override
            public void prepareFailView(String message) {
                fail("Use case has failed: " + message);
            }
        };

        final WardrobeAnalyzerInputBoundary interactor = new WardrobeAnalyzerInteractor(repository, presenter);
        interactor.analyze();
    }

    @Test
    void analyzeEmptyWardrobeTest() {
        wardrobeAnalyzerTestHelper(List.of(),
            0, 0.0, 0, 0, 0);
    }

    @Test
    void analyzePopulatedWardrobeTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("bottomwear", UUID.randomUUID());

        item1.setName("Hoodie");
        item1.setFondness(90.0);
        item1.setCondition(WearCondition.NEW);

        item2.setName("Old Uncomfortable Jeans");
        item2.setFondness(30.0);
        item2.setCondition(WearCondition.FAIR);

        wardrobeAnalyzerTestHelper(List.of(item1, item2),
            2, 60.0, 0, 0, 0);
    }

    @Test
    void analyzeItemWithNullConditionTest() {
        final AbstractWear item = WearFactory.constructWear("headwear", UUID.randomUUID());
        item.setName("Hat");
        item.setFondness(50.0);
        item.setCondition(null);

        wardrobeAnalyzerTestHelper(List.of(item),
            1, 50.0, 0, 0, 0);
    }

    @Test
    void analyzeDonationCandidateAndAgesTest() {
        final AbstractWear oldItem = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        oldItem.setName("Old Shirt");
        oldItem.setFondness(20.0); // Low fondness (< 50.0)
        oldItem.setCondition(WearCondition.FAIR);
        oldItem.setPurchaseDate(LocalDate.now().minusMonths(15)); // Older than 12 months -> Donation candidate

        final AbstractWear newerItem = WearFactory.constructWear("bottomwear", UUID.randomUUID());
        newerItem.setName("Recent Pants");
        newerItem.setFondness(80.0);
        newerItem.setCondition(WearCondition.NEW);
        newerItem.setPurchaseDate(LocalDate.now().minusMonths(2)); // Newer item

        wardrobeAnalyzerTestHelper(List.of(oldItem, newerItem),
            2, 50.0, 1, 15, 2);
    }

    @Test
    void analyzeOldItemWithHighFondnessTest() {
        final AbstractWear oldLovedItem = WearFactory.constructWear(
            "innertopwear", UUID.randomUUID()
        );
        oldLovedItem.setName("Jacket");
        oldLovedItem.setFondness(90.0); // High fondness (>= 50.0) -> false branch
        oldLovedItem.setCondition(WearCondition.NEW);
        oldLovedItem.setPurchaseDate(
            LocalDate.now().minusMonths(24)
        ); // Older than 12 months

        wardrobeAnalyzerTestHelper(
            List.of(oldLovedItem), 1, 90.0, 0, 24, 24
        );
    }
}
