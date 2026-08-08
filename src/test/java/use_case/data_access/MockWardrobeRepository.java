package use_case.data_access;

import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class MockWardrobeRepository implements WardrobeDataAccessInterface {
    private final Wardrobe mockWardrobe;

    public MockWardrobeRepository(Wardrobe mockWardrobe) {
        this.mockWardrobe = mockWardrobe;
    }

    @Override
    public Wardrobe fetchWardrobe() {
        return mockWardrobe;
    }

    @Override
    public void saveWardrobe(Wardrobe wardrobe) {
    }
}
