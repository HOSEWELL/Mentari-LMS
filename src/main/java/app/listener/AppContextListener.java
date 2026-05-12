package app.listener;

import app.utility.bootstrap.Bootstrap;
import app.utility.bootstrap.InitBootstrap;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Inject
    @InitBootstrap
    private Instance<Bootstrap> bootstraps;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("********** MENTARI SYSTEM STARTING **********");

        // This automatically finds DatabaseBootstrap   
        for (Bootstrap bootstrap : bootstraps) {
            bootstrap.process();
        }

        System.out.println("********** MENTARI SYSTEM READY **********");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("********** MENTARI SYSTEM SHUTTING DOWN **********");
    }
}