package ru.klapatnyuk.sberbank.web.notification;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * @author klapatnyuk
 */
public class Warning extends Notification {

    private static final long serialVersionUID = 1472999010909744908L;

    public Warning(String caption) {
        super(caption);
    }

    public static void show(String caption) {
        Notification notification = new Notification(caption, Type.WARNING_MESSAGE);
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.TOP_RIGHT);
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());
    }

    public static void show(String caption, String description) {
        Notification notification = new Notification(caption, description, Type.WARNING_MESSAGE);
        notification.setHtmlContentAllowed(true);
        notification.setPosition(Position.TOP_RIGHT);
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());
    }
}
