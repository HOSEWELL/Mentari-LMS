package app.utility.helper;

import app.framework.MentariTable;
import jakarta.enterprise.context.Dependent;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

@Dependent
public class ClassScanner {

    public Set<Class<?>> scanForDbTables(String basePackage) {

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> entities =
                reflections.getTypesAnnotatedWith(MentariTable.class);

        Set<Class<?>> result = new HashSet<>(entities);

        System.out.println("MENTARI SCANNER >>> Found " + result.size() + " entities");

        for (Class<?> entity : result) {
            System.out.println("MENTARI SCANNER >>> " + entity.getName());
        }

        return result;
    }
}