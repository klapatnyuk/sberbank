package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author klapatnyuk
 */
@Theme("mytheme")
public class SberbankUI extends UI {

    private static JDBCConnectionPool connectionPool;

    static {
        try {
            connectionPool = new SimpleJDBCConnectionPool("org.postgresql.Driver",
                    "jdbc:postgresql://localhost:5432/sberbank", "sberbank", "sberbank", 2, 5);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO add SQLException to log
        }
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        try (Connection connection = connectionPool.reserveConnection()) {

            FieldHandler handler = new FieldHandler(connection);
            System.out.println(handler.findByTemplateId(1));

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO add SQLException to log
        }

    }

    @WebServlet(urlPatterns = "/*", name = "SberbankUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = SberbankUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        @Override
        public void init() throws ServletException {
            super.init();

            // TODO initialize in-memory database here
        }

        @Override
        public void destroy() {
            super.destroy();

            // TODO destroy in-memory database here
        }
    }
}
