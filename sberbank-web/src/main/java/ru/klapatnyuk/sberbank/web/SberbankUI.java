package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.Page;
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
@Theme("legenda")
public class SberbankUI extends UI {

    public static final ResourceProvider I18N = ResourceFactory.getProvider(SberbankUI.class);
    public static final ResourceBundle CONFIG =
            PropertyResourceBundle.getBundle(SberbankUI.class.getPackage().getName() + "." + SberbankUI.CONFIG_NAME);

    private static final String CONFIG_NAME = "config";

    private static JDBCConnectionPool connectionPool;

    private WarningWindow warningWindow;
    private LoginWindow loginWindow;

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
        if (!loginWindow.isAttached()) {
            addWindow(loginWindow);
        }

        BrownieSession.get().logout();
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
        Page.getCurrent().setTitle(I18N.getString(SberbankKey.Header.APP));

        initLoginWindow();
        initWarningWindow();

        SberbankUITemplate template = new SberbankUITemplate();
        template.getProfileLayout().getNameLabel().setValue(BrownieSession.get().getLoggedInUser());
        setContent(template);

        /*try (Connection connection = connectionPool.reserveConnection()) {

            FieldHandler handler = new FieldHandler(connection);
            System.out.println(handler.findByTemplateId(1));

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO add SQLException to log
        }*/
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
        /*loginWindow.getLoginTextField().setValidationVisible(false);
        loginWindow.getPasswordField().setValidationVisible(false);
        try {
            loginWindow.getLoginTextField().validate();
            loginWindow.getPasswordField().validate();
        } catch (Validator.InvalidValueException e) {
            loginWindow.getCaptchaComponent().reload();
            AbstractUI.getWarningWindow().add(AbstractUI.I18N.getString(LOGIN_LOGIN_PASSWORD_INVALID));
            loginWindow.getLoginTextField().setValidationVisible(true);
            loginWindow.getPasswordField().setValidationVisible(true);
            return;
        }
        if (loginAttempt >= loginAttemptLimit) {
            if (!loginWindow.getCaptchaComponent().validate()) {
                loginWindow.getCaptchaComponent().reload();
                AbstractUI.getWarningWindow().add(AbstractUI.I18N.getString(LOGIN_CAPTCHA_INVALID));
                return;
            }
        }
        String login = loginWindow.getLoginTextField().getValue();
        String password = loginWindow.getPasswordField().getValue();
        boolean loggedIn = false;
        try {
            loggedIn = loginService.login(login, password);
        } catch (RemoteServiceException e) {
            loginWindow.getPasswordField().clear();
            String caption;
            if (e.getCause().getMessage().endsWith(ErrorConstant.LOGIN_INVALID_POSTFIX)) {
                caption = AbstractUI.I18N.getString(LOGIN_INVALID);
            } else if (e.getCause().getMessage().endsWith(ErrorConstant.CONNECT_POSTFIX)) {
                caption = AbstractUI.I18N.getString(LOGIN_CONNECT_ERROR);
            } else {
                caption = AbstractUI.I18N.getString(LOGIN_ERROR);
            }
            AbstractUI.getWarningWindow().add(caption);
        }
        if (loggedIn) {
            loginAttempt = 0;
            loginWindow.getCaptchaLayout().setVisible(false);
            LOG.info("User is logged in");
            BrownieSession.get().setBackendSessionId(loginService.getSessionId().substring(0, 32));
            BrownieSession.get().setLoggedInUser(loginService.getLoggedInUser());
            BrownieSession.get().setLogin(login);
            BrownieSession.get().setPassword(password);
            loginWindow.close();

            receiveConfig();
            ConciergeUITemplate template = new ConciergeUITemplate();
            template.getProfileLayout().getNameLabel().setValue(BrownieSession.get().getLoggedInUser());
            setContent(template);
            removePollListener(guestPollListener);
            addPollListener(pollListener);
        } else {
            loginAttempt++;
            if (loginAttempt >= loginAttemptLimit) {
                loginWindow.getCaptchaLayout().setVisible(true);
                loginWindow.getCaptchaComponent().reload();
            }
        }*/
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
    }
}
