package interface_adapter.wardrobe;

import interface_adapter.AbstractViewModel;

/**
 * Represents the wardrobe view model.
 */
public class WardrobeViewModel extends AbstractViewModel {
    private WardrobeState wardrobeState = new WardrobeState();

    public WardrobeState getWardrobeState() {
        return this.wardrobeState;
    }

    public void setWardrobeState(WardrobeState wardrobeState) {
        this.wardrobeState = wardrobeState;
    }
}
