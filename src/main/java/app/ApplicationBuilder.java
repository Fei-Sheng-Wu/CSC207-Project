package app;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import view.AbstractView;
import view.ApplicationManager;

/**
 * Represents an application builder.
 */
public class ApplicationBuilder {
    private final Map<Class<?>, Object> registry = new HashMap<>();
    private final List<Class<? extends AbstractView>> navigationsTop = new ArrayList<>();
    private final List<Class<? extends AbstractView>> navigationsBottom = new ArrayList<>();
    private final ApplicationManager manager = new ApplicationManager(registry, navigationsTop, navigationsBottom);

    /**
     * Registers an object.
     *
     * @param objectClass      the class of the object
     * @param parameterClasses the classes of the parameters of the service
     * @param <T>              the type of the object
     * @return the current application builder
     * @throws RuntimeException if the object cannot be registered
     */
    public <T> ApplicationBuilder registerSimple(Class<T> objectClass, Class<?>... parameterClasses) {
        try {
            final Object[] parameters = new Object[parameterClasses.length];
            for (int i = 0; i < parameterClasses.length; i++) {
                parameters[i] = manager.get(parameterClasses[i]);
            }

            manager.register(
                objectClass,
                objectClass.getDeclaredConstructor(parameterClasses).newInstance(parameters)
            );
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return this;
    }

    /**
     * Registers an object.
     *
     * @param objectClass      the class of the object
     * @param parameters    the parameters of the service
     * @param <T>              the type of the object
     * @return the current application builder
     * @throws RuntimeException if the object cannot be registered
     */
    public <T> ApplicationBuilder registerSimple(Class<T> objectClass, Object... parameters) {
        try {
            final Class<?>[] parameterClasses = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameterClasses[i] = parameters[i].getClass();
            }

            manager.register(
                objectClass,
                objectClass.getDeclaredConstructor(parameterClasses).newInstance(parameters)
            );
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return this;
    }

    /**
     * Registers a service.
     *
     * @param abstractClass    the abstract class of the service
     * @param concreteClass    the class of the service
     * @param parameterClasses the classes of the parameters of the service
     * @param <T>              the type of the abstract service
     * @param <S>              the type of the concrete service
     * @return the current application builder
     * @throws RuntimeException if the object is unregistered or invalid
     */
    public <T, S extends T> ApplicationBuilder registerImplementation(
        Class<T> abstractClass,
        Class<S> concreteClass,
        Class<?>... parameterClasses
    ) {
        try {
            final Object[] parameters = new Object[parameterClasses.length];
            for (int i = 0; i < parameterClasses.length; i++) {
                parameters[i] = manager.get(parameterClasses[i]);
            }

            manager.register(
                abstractClass,
                concreteClass.getDeclaredConstructor(parameterClasses).newInstance(parameters)
            );
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return this;
    }

    /**
     * Registers a service.
     *
     * @param abstractClass the abstract class of the service
     * @param concreteClass the class of the service
     * @param parameters    the parameters of the service
     * @param <T>           the type of the abstract service
     * @param <S>           the type of the concrete service
     * @return the current application builder
     * @throws RuntimeException if the object is unregistered or invalid
     */
    public <T, S extends T> ApplicationBuilder registerImplementation(
        Class<T> abstractClass,
        Class<S> concreteClass,
        Object... parameters
    ) {
        try {
            final Class<?>[] parameterClasses = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameterClasses[i] = parameters[i].getClass();
            }

            manager.register(
                abstractClass,
                concreteClass.getDeclaredConstructor(parameterClasses).newInstance(parameters)
            );
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return this;
    }

    /**
     * Registers a view.
     *
     * @param viewClass the class of the view
     * @param <T>       the type of the view
     * @return the current application builder
     * @throws RuntimeException if the view cannot be registered
     */
    public <T extends AbstractView> ApplicationBuilder registerView(Class<T> viewClass) {
        try {
            manager.register(
                viewClass,
                viewClass.getDeclaredConstructor(ApplicationManager.class).newInstance(manager)
            );
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return this;
    }

    /**
     * Updates the initial view.
     *
     * @param viewClass the class of the initial view
     * @return the current application builder
     */
    public ApplicationBuilder setInitialView(Class<? extends AbstractView> viewClass) {
        manager.showView(viewClass);

        return this;
    }

    /**
     * Updates the top navigations.
     *
     * @param navigations the collection of classes of the views for the top navigation.
     * @return the current application builder
     */
    public ApplicationBuilder setTopNavigations(List<Class<? extends AbstractView>> navigations) {
        navigationsTop.clear();
        navigationsTop.addAll(navigations);

        return this;
    }

    /**
     * Updates the bottom navigations.
     *
     * @param navigations the collection of classes of the views for the bottom navigation.
     * @return the current application builder
     */
    public ApplicationBuilder setBottomNavigations(List<Class<? extends AbstractView>> navigations) {
        navigationsBottom.clear();
        navigationsBottom.addAll(navigations);

        return this;
    }

    /**
     * Creates the application.
     *
     * @return the application
     */
    public JFrame build() {
        return manager.buildWindow();
    }
}
