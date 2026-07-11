package entity;

import java.util.List;

/**
 * Represents the user's entire collection of clothing.
 */
public class Wardrobe {
    private final List<Wear> items;

    public Wardrobe(List<Wear> items) {
        this.items = items;
    }

    public List<Wear> getItems() {
        return items;
    }

    public void addItem(Wear item) {
        this.items.add(item);
    }

    public void clearWardrobe() {
        this.items.clear();
    }
}
