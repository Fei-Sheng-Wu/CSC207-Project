package entity;

import java.util.List;

/**
 * Represents a collection of clothing items.
 */
public final class Wardrobe {
    private final List<AbstractWear> items;

    /**
     * Constructs a new wardrobe.
     *
     * @param items the initial collection of items
     */
    public Wardrobe(List<AbstractWear> items) {
        this.items = items;
    }

    /**
     * Returns the collection of items in the wardrobe.
     *
     * @return the collection of items in the wardrobe
     */
    public List<AbstractWear> getItems() {
        return items;
    }

    /**
     * Adds a clothing item to the wardrobe.
     *
     * @param item the clothing item to add
     */
    public void addItem(AbstractWear item) {
        items.add(item);
    }

    /**
     * Updates a clothing item in the wardrobe.
     *
     * @param item the clothing item to update, whether its UUID is used to locate its existence in the wardrobe
     * @return true if the clothing item was in the wardrobe and is updated; otherwise, false
     */
    public boolean updateItem(AbstractWear item) {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).getUuid().equals(item.getUuid())) {
                continue;
            }

            items.set(i, item);
            return true;
        }

        return false;
    }

    /**
     * Removes a clothing item from the wardrobe.
     *
     * @param item the clothing item to remove
     * @return true if the clothing item was in the wardrobe and is removed; otherwise, false
     */
    public boolean removeItem(AbstractWear item) {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).getUuid().equals(item.getUuid())) {
                continue;
            }

            items.remove(i);
            return true;
        }

        return false;
    }

    /**
     * Removes all clothing items from the wardrobe.
     */
    public void clearItems() {
        items.clear();
    }
}
