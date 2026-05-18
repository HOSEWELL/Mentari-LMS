package app.listener;

import app.utility.helper.ClassScanner;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener
        implements ServletContextListener {

    @Override
    public void contextInitialized(
            ServletContextEvent sce
    ) {

        System.out.println(
                "********** MENTARI SYSTEM STARTING **********"
        );

        /*
         * SCAN ACTIONS
         */
        ClassScanner.scanForAction(
                "app.action"
        );
    }

    @Override
    public void contextDestroyed(
            ServletContextEvent sce
    ) {

    }
}