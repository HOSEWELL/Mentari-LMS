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
            System.out.println("MENTARI ENGINE >>> Bootstrapping database...");

            Set<Class<?>> entities =
                    clazzScanner.scanForDbTables("app.model");

            System.out.println("MENTARI ENGINE >>> Found " + entities.size() + " entities");

            TableGenerator.generateTables(ds.getConnection(), entities);

            System.out.println("MENTARI ENGINE >>> Database Tables Synced Successfully.");

        } catch (Exception e) {
            System.err.println("MENTARI ENGINE >>> Bootstrap failed");
            e.printStackTrace();
        }
    }
}