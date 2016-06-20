package ru.klapatnyuk.sberbank.web;

import com.vaadin.server.VaadinSession;
import ru.klapatnyuk.sberbank.model.entity.User;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public class SberbankSession implements Serializable {

    private static final long serialVersionUID = 6092827324821244960L;

    private User user;

    private SberbankSession() {
    }

    public static SberbankSession get() {
        SberbankSession context = VaadinSession.getCurrent().getAttribute(SberbankSession.class);
        if (context == null) {
            context = new SberbankSession();
            VaadinSession.getCurrent().setAttribute(SberbankSession.class, context);
        }
        return context;
    }

    public void logout() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
