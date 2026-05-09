package app.utility.bootstrap;

import app.utility.db.DataSourceHelper;
import app.utility.db.TableGenerator;
import app.utility.helper.ClassScanner;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Set;

@InitBootstrap
@Dependent
public class DatabaseBootstrap implements Bootstrap {

    @Inject
    private ClassScanner clazzScanner;

    @Inject
    @InitBootstrap
    private DataSourceHelper ds;

    @Override
    public void process() {
        try {
            // Match the method name to the one in ClassScanner
            Set<Class<?>> entities = clazzScanner.scanForDbTables("app.model");

            // Match the method name to the one in TableGenerator
            TableGenerator.generateTables(ds.getConnection(), entities);

            System.out.println("MENTARI ENGINE >>> Database Tables Synced.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}