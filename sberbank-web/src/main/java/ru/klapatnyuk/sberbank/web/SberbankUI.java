package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Validator;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.event.UIEvents;
import com.vaadin.server.*;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.handler.UserHandler;
import ru.klapatnyuk.sberbank.web.i18n.ResourceFactory;
import ru.klapatnyuk.sberbank.web.i18n.ResourceProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author klapatnyuk
 */
@Theme("legenda")
public class SberbankUI extends UI {

    public static final ResourceProvider I18N = ResourceFactory.getProvider(SberbankUI.class);
    public static final ResourceBundle CONFIG =
            PropertyResourceBundle.getBundle(SberbankUI.class.getPackage().getName() + "." + SberbankUI.CONFIG_NAME);

    public static JDBCConnectionPool connectionPool;

    private static final String CONFIG_NAME = "config";
    private static final int POLL_INTERVAL = Integer.parseInt(CONFIG.getString(ConfigKey.TIME_POLL_INTERVAL.getKey()));
    private static final int INACTIVE_INTERVAL = Integer.parseInt(CONFIG.getString(ConfigKey.TIME_INACTIVE_INTERVAL.getKey()));
    private static final Logger LOGGER = LoggerFactory.getLogger(SberbankUI.class);

    private WarningWindow warningWindow;
    private LoginWindow loginWindow;
    private UIEvents.PollListener guestPollListener;
    private UIEvents.PollListener pollListener = event -> SberbankUI.getTemplate().poll();

    static {
        try {
//            connectionPool = new SimpleJDBCConnectionPool("org.postgresql.Driver",
//                    "jdbc:postgresql://localhost:5432/sberbank", "sberbank", "sberbank", 2, 5);
            connectionPool = new SimpleJDBCConnectionPool("org.h2.Driver",
                    "jdbc:h2:mem:sberbank;DB_CLOSE_DELAY=-1", "sberbank", "sberbank", 2, 5);
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
        SberbankSession.get().logout();
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
        setPollInterval(POLL_INTERVAL);
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(INACTIVE_INTERVAL);

        Page.getCurrent().setTitle(I18N.getString(SberbankKey.Header.APP));

        initLoginWindow();
        initWarningWindow();

        guestPollListener = event -> SberbankUI.getWarningWindow().poll();

        if (SberbankSession.get().getUser() == null) {
            LOGGER.info("User is not logged in");
            login();

        } else {
            LOGGER.info("User is logged in");
            SberbankUITemplate template = new SberbankUITemplate();
            template.getProfileLayout().getNameLabel().setValue(SberbankSession.get().getUser().getLogin());
            setContent(template);

            removePollListener(guestPollListener);
            addPollListener(pollListener);
        }
    }

    private void initLoginWindow() {
        loginWindow = new LoginWindow(CONFIG, SberbankUI.I18N.getString(SberbankKey.Header.WINDOW_LOGIN));
        loginWindow.getSubmitButton().addClickListener(event -> clickLoginButton());
    }

    private void initWarningWindow() {
        warningWindow = new WarningWindow();
        Page.getCurrent().addBrowserWindowResizeListener(event -> warningWindow
                .setPositionX(Page.getCurrent().getBrowserWindowWidth() - (int) warningWindow.getWidth() - 25));
    }

    private void clickLoginButton() {
        try {
            loginWindow.getLoginTextField().validate();
            loginWindow.getPasswordField().validate();
        } catch (Validator.InvalidValueException e) {
            SberbankUI.getWarningWindow().add(SberbankUI.I18N.getString(SberbankKey.Notification.LOGIN_LOGIN_PASSWORD_INVALID));
            return;
        }
        String login = loginWindow.getLoginTextField().getValue();
        String password = loginWindow.getPasswordField().getValue();

        User user = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            user = new UserHandler(connection).login(login, password);
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Login error", e);
            // TODO display WarningMessage
        }

        loginWindow.getPasswordField().clear();
        if (user != null) {
            LOGGER.info("User is logged in");
            SberbankSession.get().setUser(user);
            loginWindow.close();

            SberbankUITemplate template = new SberbankUITemplate();
            template.getProfileLayout().getNameLabel().setValue(SberbankSession.get().getUser().getLogin());
            setContent(template);

            removePollListener(guestPollListener);
            addPollListener(pollListener);

        } else {
            SberbankUI.getWarningWindow().add(SberbankUI.I18N.getString(SberbankKey.Notification.LOGIN_ERROR));
        }
    }

    /**
     * @author klapatnyuk
     */
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

        @Override
        protected void servletInitialized() throws ServletException {
            getService().setSystemMessagesProvider(systemMessagesInfo -> {
                CustomizedSystemMessages messages = new CustomizedSystemMessages();

                messages.setSessionExpiredCaption(I18N.getString(SberbankKey.Notification.SESSION_EXPIRED_CAPTION));
                messages.setSessionExpiredMessage(I18N.getString(SberbankKey.Notification.SESSION_EXPIRED));
                messages.setCommunicationErrorCaption(I18N.getString(SberbankKey.Notification.COMMUNICATION_PROBLEM_CAPTION));
                messages.setCommunicationErrorMessage(I18N.getString(SberbankKey.Notification.COMMUNICATION_PROBLEM));
                messages.setAuthenticationErrorCaption(I18N.getString(SberbankKey.Notification.AUTHENTICATION_PROBLEM_CAPTION));
                messages.setAuthenticationErrorMessage(I18N.getString(SberbankKey.Notification.AUTHENTICATION_PROBLEM));
                messages.setInternalErrorCaption(I18N.getString(SberbankKey.Notification.INTERNAL_ERROR_CAPTION));
                messages.setInternalErrorMessage(I18N.getString(SberbankKey.Notification.INTERNAL_ERROR));
                messages.setCookiesDisabledCaption(I18N.getString(SberbankKey.Notification.COOKIES_DISABLED_CAPTION));
                messages.setCookiesDisabledMessage(I18N.getString(SberbankKey.Notification.COOKIES_DISABLED));
                return messages;
            });
        }
    }
}
