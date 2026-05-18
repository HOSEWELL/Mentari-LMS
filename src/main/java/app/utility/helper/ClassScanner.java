package app.utility.helper;

import app.framework.annotation.Action;
import app.framework.routing.ActionRegistry;

import org.reflections.Reflections;

import java.util.Set;

public class ClassScanner {

    public static Set<Class<?>> scanForAction(
            String basePackage
    ) {

        Reflections reflections =
                new Reflections(basePackage);

        Set<Class<?>> actions =
                reflections.getTypesAnnotatedWith(
                        Action.class
                );

        System.out.println(
                "MENTARI SCANNER >>> Found "
                        + actions.size()
                        + " actions"
        );

        for (Class<?> action : actions) {

            /*
             * REGISTER ACTION
             */
            ActionRegistry.register(action);

            System.out.println(
                    "MENTARI ACTION >>> "
                            + action.getName()
            );
        }

        return actions;
    }
}