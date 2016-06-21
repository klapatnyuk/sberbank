package ru.klapatnyuk.sberbank.web.notification;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * @author klapatnyuk
 */
public class Tray extends Notification {

    private static final long serialVersionUID = 4931764721280234816L;

    public Tray(String caption) {
        super(caption);
    }

    public static void show(String caption) {
        Notification notification = new Notification(caption, Type.TRAY_NOTIFICATION);
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }
}
