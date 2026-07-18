package entity;

import java.util.UUID;

public class WearFactory {
    // Do we need an interface for this? HW5 had an interface for CommonUserFactory.
    // Also renamed it from AbstractWearFactory to WearFactory cuz checkstyle had an issue with that.
    private AbstractWear wear;

    /**
     * Constructs and returns an instance of a subclass of AbstractWear based on the specified clothing type.
     *
     * @param type the type of the given wear item.
     * @param uuid the UUID of the wear item.
     * @return an instance of a subclass of AbstractWear based on the specified type.
     * @throws IllegalArgumentException if the type is null or unsupported.
     */
    public AbstractWear constructWear(String type, UUID uuid) { // Should we make it static?
        switch (type.toLowerCase()) {
            // --- ACCESSORIES (e.g. watches, rings, sunglasses, bags, earrings) ---
            case "watch":
            case "ring":
            case "sunglasses":
            case "bag":
            case "earrings":
            case "scarf":
                wear = new Accessory(uuid);
                break;

            // --- BOTTOMWEAR (e.g. pants, jeans, skirts, and shorts) ---
            case "pants":
            case "jeans":
            case "skirt":
            case "shorts":
                wear = new Bottomwear(uuid);
                break;

            // ---  FOOTWEAR (e.g. sneakers, boots, sandals, and heels) ---
            case "sneakers":
            case "boots":
            case "sandals":
            case "heel":
            case "running shoes": // Added this
            case "loafers": // Added this
            case "dress shoes": // Added this
                wear = new Footwear(uuid);
                break;

            // --- HEADWEAR (e.g. hats, caps, and beanies) ---
            case "hat":
            case "cap":
            case "beanie":
                wear = new Headwear(uuid);
                break;

            // --- INNERTOPWEAR (e.g. shirts, crew-necks, sports bras, and tank tops) ---
            case "shirts":
            case "t-shirts":
            // case "crew-necks": Should this be removed? It is a broad category, not a specific one.
            case "sports bras":
            case "tank tops":
            case "undershirts": // Added this
            case "polos": // Added this
                wear = new InnerTopwear(uuid);
                break;

            // --- OUTERTOPWEAR (e.g. coats, hoodies, jackets, windbreakers, and parkas). ---
            case "coats":
            case "hoodies":
            case "jackets": // Should this be removed? It is a broad category, not a specific one.
            case "windbreakers":
            case "parkas":
            case "sweaters": // Added this
            case "raincoats": // Added this
                wear = new OuterTopwear(uuid);
                break;

            default:
                throw new IllegalArgumentException("Unsupported clothing item!");
        }
        return wear;
    }
}
