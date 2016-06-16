package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.event.UIEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.util.CurrentInstance;
import ru.klapatnyuk.sberbank.web.i18n.ResourceFactory;
import ru.klapatnyuk.sberbank.web.i18n.ResourceProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author klapatnyuk
 */
@Theme("mytheme")
public class SberbankUI extends UI {

    public static final ResourceProvider I18N = ResourceFactory.getProvider(SberbankUI.class);
    public static final ResourceBundle CONFIG =
            PropertyResourceBundle.getBundle(SberbankUI.class.getPackage().getName() + "." + SberbankUI.CONFIG_NAME);

    protected static final String CONFIG_NAME = "config";

    protected UIEvents.PollListener pollListener = event -> SberbankUI.getTemplate().poll();

    private static JDBCConnectionPool connectionPool;

    private WarningWindow warningWindow;
    protected LoginWindow loginWindow;
    protected UIEvents.PollListener guestPollListener;

    static {
        try {
            connectionPool = new SimpleJDBCConnectionPool("org.postgresql.Driver",
                    "jdbc:postgresql://localhost:5432/sberbank", "sberbank", "sberbank", 2, 5);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO add SQLException to log
        }
    }

    public static SberbankUITemplate getTemplate() {
        return (SberbankUITemplate) getCurrent().getContent();
    }

    public void login() {
        setContent(null);
        removePollListener(pollListener);
        if (!loginWindow.isAttached()) {
            addWindow(loginWindow);
        }

        BrownieSession.get().logout();
        addPollListener(guestPollListener);
    }

    public static SberbankUI getCurrent() {
        return (SberbankUI) CurrentInstance.get(UI.class);
    }

    public boolean isModalWindowAttached() {
        return getWindows().stream().filter(Window::isModal).findAny().isPresent();
    }

    public static WarningWindow getWarningWindow() {
        return getCurrent().warningWindow;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        /*try (Connection connection = connectionPool.reserveConnection()) {

            FieldHandler handler = new FieldHandler(connection);
            System.out.println(handler.findByTemplateId(1));

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO add SQLException to log
        }*/

        SberbankUITemplate template = new SberbankUITemplate();
        template.getProfileLayout().getNameLabel().setValue(BrownieSession.get().getLoggedInUser());
        setContent(template);

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
