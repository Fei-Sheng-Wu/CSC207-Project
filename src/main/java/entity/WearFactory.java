package entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public class WearFactory {
    private static final Map<Class<?>, String> TYPES = Map.ofEntries(
        Map.entry(InnerTopwear.class, "👕"),
        Map.entry(OuterTopwear.class, "🧥"),
        Map.entry(Bottomwear.class, "👖"),
        Map.entry(Footwear.class, "👟"),
        Map.entry(Headwear.class, "🧢"),
        Map.entry(Accessory.class, "🕶")
    );

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
        for (Class<?> targetType : TYPES.keySet()) {
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
        if (!TYPES.containsKey(type)) {
            return null;
        }

        return TYPES.get(type);
    }
}
