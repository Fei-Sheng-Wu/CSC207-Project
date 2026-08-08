package use_case.wardrobe_analyzer;

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
import static org.junit.jupiter.api.Assertions.fail;

public class WardrobeAnalyzerInteractorTest {

    private void wardrobeAnalyzerTestHelper(List<AbstractWear> inputItems,
                                            int expectedTotalCount,
                                            double expectedMeanFondness,
                                            int expectedDonationCandidates,
                                            int expectedOldestAge,
                                            int expectedNewestAge) {

        WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        WardrobeAnalyzerOutputBoundary presenter = new WardrobeAnalyzerOutputBoundary() {
            @Override
            public void prepareSuccessView(WardrobeAnalyzerOutputData outputData) {
                assertEquals(expectedTotalCount, outputData.getTotalItems());
                assertEquals(expectedMeanFondness, outputData.getAverageFondness(), 0.01);
                assertEquals(expectedDonationCandidates, outputData.getDonationCandidateCount());
                assertEquals(expectedOldestAge, outputData.getOldestItemAge());
                assertEquals(expectedNewestAge, outputData.getNewestItemAge());
            }

            @Override
            public void prepareFailView(String message) {
                fail("Use case has failed: " + message);
            }
        };

        WardrobeAnalyzerInputBoundary interactor = new WardrobeAnalyzerInteractor(repository, presenter);
        interactor.analyze();
    }

    @Test
    void analyzePopulatedWardrobeTest() {
        final AbstractWear item1 = WearFactory.constructWear("innertopwear", UUID.randomUUID());
        final AbstractWear item2 = WearFactory.constructWear("bottomwear", UUID.randomUUID());

        item1.setName("Favorite Hoodie");
        item1.setFondness(90.0);
        item1.setCondition(WearCondition.NEW);

        item2.setName("Old Uncomfortable Jeans");
        item2.setFondness(30.0);
        item2.setCondition(WearCondition.FAIR);

        wardrobeAnalyzerTestHelper(List.of(item1, item2), 2, 60.0, 0, 0, 0);
    }

    @Test
    void analyzeEmptyWardrobeTest() {

        wardrobeAnalyzerTestHelper(List.of(), 0, 0.0, 0, 0, 0);
    }
}
