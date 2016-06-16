package ru.klapatnyuk.sberbank.web;

import com.vaadin.server.VaadinSession;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public class BrownieSession implements RemoteServiceSession, Serializable {

    private static final long serialVersionUID = 6092827324821244960L;

    private String backendSessionId;
    private String loggedInUser;
    private String login;
    private String password;
    private String flatId;
    private String accessPortalUrl;
    private String addressDisplayName;

    public BrownieSession() {}

    public static BrownieSession get() {
        BrownieSession context = VaadinSession.getCurrent().getAttribute(BrownieSession.class);
        if (context == null) {
            context = new BrownieSession();
            VaadinSession.getCurrent().setAttribute(BrownieSession.class, context);
        }
        return context;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setBackendSessionId(String id) {
        this.backendSessionId = id;
    }

    public void logout() {

        // remove session attributes
        backendSessionId = null;
        loggedInUser = null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String id) {
        this.flatId = id;
    }

    public String getAddressDisplayName() {
        return addressDisplayName;
    }

    public void setAddressDisplayName(String addressDisplayName) {
        this.addressDisplayName = addressDisplayName;
    }

    public String getAccessPortalUrl() {
        return accessPortalUrl;
    }

    public void setAccessPortalUrl(String url) {
        this.accessPortalUrl = url;
    }

    @Override
    public String getBackendSessionId() {
        return backendSessionId;
    }
}
