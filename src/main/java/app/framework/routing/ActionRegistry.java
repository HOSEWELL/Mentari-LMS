package app.framework.routing;

import app.framework.annotation.Action;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionRegistry {

    private static final List<Class<?>> actions =
            new ArrayList<>();

    public static void scanAndRegister(
            String packageName
    ) {

        Reflections reflections =
                new Reflections(packageName);

        Set<Class<?>> discoveredActions =
                reflections.getTypesAnnotatedWith(
                        Action.class
                );

        actions.clear();

        actions.addAll(
                discoveredActions
        );
    }

    public static List<Class<?>> getRegisteredActions() {

        return actions;
    }
}