package entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public class WearFactory {
    private static final Class<?>[] TYPES = new Class<?>[]{
        InnerTopwear.class, OuterTopwear.class, Bottomwear.class, Footwear.class, Headwear.class, Accessory.class,
    };
    private static final Map<Class<?>, String> ICONS = Map.ofEntries(
        Map.entry(InnerTopwear.class, "👕"),
        Map.entry(OuterTopwear.class, "🧥"),
        Map.entry(Bottomwear.class, "👖"),
        Map.entry(Footwear.class, "👟"),
        Map.entry(Headwear.class, "🧢"),
        Map.entry(Accessory.class, "🕶")
    );
    private static final Map<Class<?>, String> NAMES = Map.ofEntries(
        Map.entry(InnerTopwear.class, "Inner Topwear"),
        Map.entry(OuterTopwear.class, "Outer Topwear"),
        Map.entry(Bottomwear.class, "Bottomwear"),
        Map.entry(Footwear.class, "Footwear"),
        Map.entry(Headwear.class, "Headwear"),
        Map.entry(Accessory.class, "Accessory")
    );

    /**
     * Gets a collection of all wear types.
     *
     * @return a collection of all wear types
     */
    public static Class<?>[] getAllTypes() {
        return TYPES.clone();
    }

    /**
     * Constructs and returns an instance of a subclass of AbstractWear based on the specified clothing type.
     *
     * @param type the type of the given clothing item
     * @param uuid the UUID of the clothing item
     * @return an instance of a subclass of AbstractWear based on the specified type
     * @throws IllegalArgumentException if the type is null or unsupported
     * @throws RuntimeException         if the clothing item cannot be instantiated
     */
    public static AbstractWear constructWear(String type, UUID uuid) throws IllegalArgumentException {
        AbstractWear wear = null;
        for (Class<?> targetType : TYPES) {
            if (!targetType.getSimpleName().equalsIgnoreCase(type)) {
                continue;
            }

            try {
                wear = (AbstractWear) targetType.getDeclaredConstructor(UUID.class).newInstance(uuid);
            } catch (NoSuchMethodException
                     | InstantiationException
                     | IllegalAccessException
                     | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }

            break;
        }

        if (wear == null) {
            throw new IllegalArgumentException("The type is invalid.");
        }

        return wear;
    }

    /**
     * Returns the icon string associated with the clothing item.
     *
     * @param type the type of the given clothing item
     * @return the icon string
     */
    @Nullable
    public static String getIcon(Class<?> type) {
        return ICONS.get(type);
    }

    /**
     * Returns the display string associated with the clothing item.
     *
     * @param type the type of the given clothing item
     * @return the display string
     */
    public static String getDisplayName(Class<?> type) {
        final String result = NAMES.get(type);
        if (result == null) {
            return "";
        }

        return result;
    }
}
