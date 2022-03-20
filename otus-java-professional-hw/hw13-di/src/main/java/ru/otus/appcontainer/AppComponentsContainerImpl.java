package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var configObject = configClass.getConstructor().newInstance();
        checkConfigClass(configClass);
        List<Method> componentMethods = getComponentMethods(configClass);
        List<Method> orderedComponentMethods = sortComponentMethodsByOrder(componentMethods);
        saveComponents(configObject, orderedComponentMethods);
    }

    private void saveComponents(Object appConfig, List<Method> orderedComponentMethods) {
        orderedComponentMethods.forEach(method -> {
            var parameterTypes = method.getParameterTypes();
            var componentName = method.getAnnotation(AppComponent.class).name();
            Object[] objects = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                var componentOptional = appComponents.stream()
                        .filter(obj -> type == obj.getClass() || Arrays.asList(obj.getClass().getInterfaces()).contains(type))
                        .findFirst();

                if (componentOptional.isEmpty()) {
                    throw new RuntimeException();
                }
                objects[i] = componentOptional.get();
            }

            try {
                var component = method.invoke(appConfig, objects);
                appComponents.add(component);
                appComponentsByName.put(componentName, component);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Method> sortComponentMethodsByOrder(List<Method> componentMethods) {
        return componentMethods.stream()
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private List<Method> getComponentMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .toList();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var componentOptional = appComponents.stream()
                .filter(component -> component.getClass().equals(componentClass)
                        || Arrays.asList(component.getClass().getInterfaces()).contains(componentClass))
                .findFirst();
        if (componentOptional.isEmpty()) return null;

        return (C)componentOptional.get();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component == null) return null;

        return (C)component;
    }
}
