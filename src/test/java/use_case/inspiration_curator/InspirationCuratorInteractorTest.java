package use_case.inspiration_curator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.OutfitIdea;
import entity.WearColor;

public class InspirationCuratorInteractorTest {
    @Test
    public void testCurate() {
        final FakeInspirationDataAccessObject repository = new FakeInspirationDataAccessObject();
        final FakeWardrobeAdderPresenter presenter = new FakeWardrobeAdderPresenter();
        final InspirationCuratorInteractor interactor = new InspirationCuratorInteractor(repository, presenter);

        final AbstractWear item = new InnerTopwear(UUID.randomUUID());
        item.setName("Blue Shirt");
        item.setBrand("Test Brand");
        item.setColor(WearColor.RED);

        interactor.curate(new InspirationCuratorInputData(item));
        final List<OutfitIdea> output = presenter.getIdeas();

        for (OutfitIdea idea : output) {
            assertTrue(idea.getDescription().contains(item.getName()));
            assertTrue(idea.getDescription().contains(item.getBrand()));
            assertTrue(idea.getDescription().contains(WearColor.RED.getDisplayName()));
        }
    }

    private static final class FakeInspirationDataAccessObject implements InspirationDataAccessInterface {
        @Override
        public List<OutfitIdea> getOutfitIdeas(String query) {
            return List.of(new OutfitIdea(query, "www.example.com"));
        }
    }

    private static final class FakeWardrobeAdderPresenter implements InspirationCuratorOutputBoundary {
        private List<OutfitIdea> ideas;

        @Override
        public void prepareSuccessView(InspirationCuratorOutputData output) {
            ideas = output.getOutfitIdeas();
        }

        @Override
        public void prepareFailView(String error) {
        }

        public List<OutfitIdea> getIdeas() {
            return ideas;
        }
    }
}
