package ru.klapatnyuk.sberbank.web;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author klapatnyuk
 */
@WebListener
public class MigrationListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Flyway flyway = new Flyway();
        // TODO get from config.properties
        flyway.setDataSource("jdbc:h2:mem:sberbank;DB_CLOSE_DELAY=-1", "sberbank", "sberbank");
        flyway.migrate();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // nothing to do
    }
}
