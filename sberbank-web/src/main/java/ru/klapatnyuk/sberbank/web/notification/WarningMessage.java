package ru.klapatnyuk.sberbank.web.notification;

import com.vaadin.ui.Component;

/**
 * @author klapatnyuk
 */
public class WarningMessage implements Comparable<WarningMessage> {

    private final String caption;
    private final Component source;

    private String description;
    private Component.Focusable field;

    private boolean removable;
    private Long time = System.nanoTime();

    public WarningMessage(String caption, String description, Component.Focusable field, Component source) {
        this.caption = caption;
        this.description = description;
        this.field = field;
        this.source = source;
    }

    public WarningMessage(String caption, Throwable throwable, Component.Focusable field, Component source) {
        this.caption = caption;
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        this.description = throwable.getMessage();
        this.field = field;
        this.source = source;
    }

    public WarningMessage(String caption, Component.Focusable field, Component source) {
        this(caption, (String) null, field, source);
    }

    public WarningMessage(String caption, Throwable throwable, Component source) {
        this.caption = caption;
        this.source = source;
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        this.description = throwable.getMessage();
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

    @Override
    public int compareTo(WarningMessage message) {
        return -time.compareTo(message.getTime());
    }
}