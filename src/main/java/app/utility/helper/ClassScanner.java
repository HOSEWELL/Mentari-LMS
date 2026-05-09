package app.utility.helper;

import app.framework.MentariTable;
import jakarta.enterprise.context.Dependent;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Dependent
public class ClassScanner {
    public Set<Class<?>> scanForDbTables(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            if (resource != null) {
                File directory = new File(resource.getFile());
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith(".class")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(MentariTable.class)) {
                                classes.add(clazz);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}