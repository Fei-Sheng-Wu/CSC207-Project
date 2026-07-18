package entity;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class WearFactory {
    private static final Class<?>[] TYPES = {
        InnerTopwear.class,
        OuterTopwear.class,
        Bottomwear.class,
        Footwear.class,
        Headwear.class,
        Accessory.class,
    };

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
            if (targetType.getSimpleName().equals(type.toLowerCase())) {
                try {
                    wear = (AbstractWear) targetType.getDeclaredConstructor(UUID.class).newInstance(uuid);
                } catch (NoSuchMethodException
                         | InstantiationException
                         | IllegalAccessException
                         | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if (wear == null) {
            throw new IllegalArgumentException("The type is invalid.");
        }

        return wear;
    }
}
