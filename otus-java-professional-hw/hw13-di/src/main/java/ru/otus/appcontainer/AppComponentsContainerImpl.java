package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.utils.InvalidAppConfigException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        throwExceptionIfInvalidAppConfig(orderedComponentMethods);

        orderedComponentMethods.forEach(method -> {
            var dependencyTypes = method.getParameterTypes();
            var componentName = method.getAnnotation(AppComponent.class).name();
            Object[] dependencyObjects = new Object[dependencyTypes.length];
            for (int i = 0; i < dependencyTypes.length; i++) {
                Class<?> dependencyType = dependencyTypes[i];
                var componentOptional = appComponents.stream()
                        .filter(component -> dependencyType.isAssignableFrom(component.getClass()))
                        .findFirst();

                if (componentOptional.isEmpty()) {
                    throw new RuntimeException();
                }
                dependencyObjects[i] = componentOptional.get();
            }

            try {
                var component = method.invoke(appConfig, dependencyObjects);
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
    public <C> C getAppComponent(Class<C> requiredComponentClass) {
        var componentOptional = appComponents.stream()
                .filter(component -> requiredComponentClass.isAssignableFrom(component.getClass()))
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

    private void throwExceptionIfInvalidAppConfig(List<Method> componentMethods) throws RuntimeException {
        var counter = new HashMap<String, Integer>();
        componentMethods.stream()
                .map(method -> method.getAnnotation(AppComponent.class).name())
                .forEach(name -> {
                    //counter.compute(name, (key, count) -> key == null ? 1 : count + 1);
                    counter.computeIfPresent(name, (key, count) -> count + 1);
                    counter.computeIfAbsent(name, key -> 1);
                });
        var repeatedComponentNames = counter.entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
        if (!repeatedComponentNames.isEmpty()) {
            throw new InvalidAppConfigException(repeatedComponentNames.toString());
        }
    }
}
