package entity;

import java.util.List;

public class Outfit {
    private InnerTopwear topwearInner;
    private OuterTopwear topwearOuter;
    private Bottomwear bottomwear;
    private Footwear footwear;
    private Headwear headwear;
    private List<Accessory> accessories;

    public Outfit(InnerTopwear topwearInner, OuterTopwear topwearOuter, Bottomwear bottomwear,
                  Footwear footwear, Headwear headwear, List<Accessory> accessories) {
        this.topwearInner = topwearInner;
        this.topwearOuter = topwearOuter;
        this.bottomwear = bottomwear;
        this.footwear = footwear;
        this.headwear = headwear;
        this.accessories = accessories;
    }

    /**
     * Checks if the outfit is appropriate based on current weather data.
     */
    public boolean isAppropriateForWeather(Weather weather) {
        if (weather.getTemperature() < 0.0 && this.topwearOuter == null) {
            return false;
        }
        return true;
    }

    public InnerTopwear getTopwearInner() {
        return topwearInner;
    }
    public OuterTopwear getTopwearOuter() {
        return topwearOuter;
    }
    public Bottomwear getBottomwear() {
        return bottomwear;
    }
    public Footwear getFootwear() {
        return footwear;
    }
    public Headwear getHeadwear() {
        return headwear;
    }
    public List<Accessory> getAccessories() {
        return accessories;
    }
}
