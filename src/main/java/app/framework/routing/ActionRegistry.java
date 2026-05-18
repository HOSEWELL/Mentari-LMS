package app.framework.routing;

import java.util.ArrayList;
import java.util.List;

public class ActionRegistry {

    private static final List<Class<?>> actions =
            new ArrayList<>();

    public static void register(
            Class<?> clazz
    ) {

        actions.add(clazz);
    }

    public static List<Class<?>> getRegisteredActions() {

        return actions;
    }
}