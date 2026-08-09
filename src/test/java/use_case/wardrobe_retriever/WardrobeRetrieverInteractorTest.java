package use_case.wardrobe_retriever;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearFactory;
import use_case.data_access.MockWardrobeRepository;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class WardrobeRetrieverInteractorTest {

    private void wardrobeReporterTestHelper(
        List<AbstractWear> inputItems,
        int expectedAllSize
    ) {
        final WardrobeDataAccessInterface repository = new MockWardrobeRepository(
            new Wardrobe(new ArrayList<>(inputItems))
        );

        final WardrobeRetrieverOutputBoundary outputBoundary = new WardrobeRetrieverOutputBoundary() {
            @Override
            public void prepareSuccessView(WardrobeRetrieverOutputData outputData) {
                assertEquals(expectedAllSize, outputData.getWearsAll().size());
            }

            @Override
            public void prepareFailView(String message) {
                fail("Reporter use case failed unexpectedly: " + message);
            }
        };

        final WardrobeRetrieverInputBoundary interactor = new WardrobeRetrieverInteractor(
            repository, outputBoundary
        );

        interactor.retrieve();
    }

    @Test
    void reportWardrobeWithOldAndNewItemsTest() {
        final AbstractWear newItem = WearFactory.constructWear(
            "bottomwear", UUID.randomUUID()
        );
        newItem.setName("New Pants");

        wardrobeReporterTestHelper(List.of(newItem), 1);
    }

    @Test
    void reportEmptyWardrobeTest() {
        wardrobeReporterTestHelper(
            List.of(), 0
        );
    }
}
