package ru.klapatnyuk.sberbank.web;

import org.flywaydb.core.Flyway;
import ru.klapatnyuk.sberbank.web.key.ConfigKey;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ResourceBundle;

/**
 * @author klapatnyuk
 */
@WebListener
public class MigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Flyway flyway = new Flyway();

        ResourceBundle bundle = SberbankUI.CONFIG;
        flyway.setDataSource(bundle.getString(ConfigKey.CONNECT_URI.getKey()),
                bundle.getString(ConfigKey.CONNECT_USERNAME.getKey()),
                bundle.getString(ConfigKey.CONNECT_PASSWORD.getKey()));
        flyway.migrate();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // nothing to do
    }
}
