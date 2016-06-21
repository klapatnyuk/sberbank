package ru.klapatnyuk.sberbank.web.notification;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;

/**
 * @author klapatnyuk
 */
public class WarningMessage implements Comparable<WarningMessage> {

    private final String caption;
    private final LayoutEvents.LayoutClickListener listener;
    private final Component source;

    private String description;
    private Component.Focusable field;

    private boolean removable = false;
    private Long time = System.nanoTime();

    public WarningMessage(String caption, String description, Component.Focusable field, Component source) {
        this.caption = caption;
        this.listener = null;
        this.description = description;
        this.field = field;
        this.source = source;
    }

    public WarningMessage(String caption, Component.Focusable field, Component source) {
        this(caption, null, field, source);
    }

    public WarningMessage(String caption, String description, LayoutEvents.LayoutClickListener listener,
                          Component source) {
        this.caption = caption;
        this.listener = listener;
        this.source = source;
        this.description = description;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public Long getTime() {
        return time;
    }

    public Component getSource() {
        return source;
    }

    public Component.Focusable getField() {
        return field;
    }

    public void setField(Component.Focusable field) {
        this.field = field;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LayoutEvents.LayoutClickListener getListener() {
        return listener;
    }

    @Override
    public int compareTo(WarningMessage message) {
        return -this.time.compareTo(message.getTime());
    }
}