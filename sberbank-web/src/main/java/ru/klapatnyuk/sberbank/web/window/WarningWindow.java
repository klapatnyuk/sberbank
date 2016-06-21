package ru.klapatnyuk.sberbank.web.window;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.web.*;
import ru.klapatnyuk.sberbank.web.constant.StyleDimensions;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;
import ru.klapatnyuk.sberbank.web.key.ConfigKey;
import ru.klapatnyuk.sberbank.web.notification.Warning;
import ru.klapatnyuk.sberbank.web.notification.WarningMessage;
import ru.klapatnyuk.sberbank.web.notification.WarningMessageComparator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * @author klapatnyuk
 */
public class WarningWindow extends Window {

    private static final long serialVersionUID = 8550951239209722669L;
    private static final int COUNT_MAX = Integer.parseInt(SberbankUI.CONFIG.getString(ConfigKey.WARNING_LIMIT.getKey()));
    private static final WarningMessageComparator COMPARATOR = new WarningMessageComparator();

    private final List<WarningMessage> messages = new ArrayList<>();
    private final VerticalLayout layout = new VerticalLayout();

    public WarningWindow() {
        setClosable(false);
        setModal(false);
        setDraggable(false);
        setResizable(false);
        addStyleName(StyleNames.WINDOW_WARNING);
        setWidth(StyleDimensions.WINDOW_WIDTH);
        setContent(layout);

        layout.setWidth("100%");
        layout.setHeightUndefined();
        layout.addStyleName(StyleNames.WINDOW_WARNING_LAYOUT);

        setWindowMode(WindowMode.NORMAL);
        int top = 47;
        int left = 0;
        // TODO browser width != template width (because template width has css min-width attribute)
        Page page = UI.getCurrent().getPage();
        if (page != null) {
            left = page.getBrowserWindowWidth() - (int) getWidth() - 25;
        }
        setPositionY(top);
        setPositionX(left);
    }

    public void add(String caption) {
        add(new WarningMessage(caption, (Focusable) null, null));
    }

    public void add(WarningMessage message) {
        if (SberbankUI.getCurrent().isModalWindowAttached()) {
            clear();
            Warning.show(message.getCaption());
            return;
        }

        WarningMessage duplicate = findDuplicate(message.getCaption());
        if (duplicate != null) {
            duplicate.setRemovable(false);
            duplicate.setTime(System.nanoTime());
            duplicate.setDescription(message.getDescription());
            duplicate.setField(message.getField());
            messages.sort(COMPARATOR);
            reload();
            return;
        }

        messages.add(new WarningMessage(message.getCaption(), message.getDescription(), message.getField(),
                message.getSource()));
        messages.sort(COMPARATOR);
        reload();
    }

    public void addAll(List<WarningMessage> messages) {
        ListIterator<WarningMessage> iterator = messages.listIterator(messages.size());
        while (iterator.hasPrevious()) {
            add(iterator.previous());
        }
    }

    public void poll() {
        boolean changed = false;
        Iterator<WarningMessage> iterator = messages.iterator();
        while (iterator.hasNext()) {
            WarningMessage current = iterator.next();
            if (current.isRemovable()) {
                iterator.remove();
                changed = true;
            } else {
                current.setRemovable(true);
            }
        }
        if (changed) {
            if (SberbankUI.getCurrent().isModalWindowAttached()) {
                clear();
            } else {
                reload();
            }
        }
    }

    public void clear(Component source) {
        List<WarningMessage> removable = messages.stream().filter(message -> message.getSource() == source)
                .collect(Collectors.toList());
        messages.removeAll(removable);
        reload();
    }

    private void reload() {

        layout.removeAllComponents();
        for (WarningMessage message : messages) {
            layout.addComponent(newMessageLayout(message));
        }

        if (!messages.isEmpty()) {
            UI ui = UI.getCurrent();

            if (!ui.getWindows().contains(this)) {
                ui.addWindow(this);
            }
            if (ui.getWindows().size() > 1) {
                focus();
            }
            if (messages.size() > COUNT_MAX) {
                setHeight("340px");
            } else {
                setHeightUndefined();
            }
        } else {
            close();
        }
    }

    private void clear() {
        messages.clear();
        layout.removeAllComponents();
        close();
    }

    private WarningMessage findDuplicate(String caption) {
        return messages.stream().filter(message -> message.getCaption().equals(caption)).findFirst()
                .orElse(null);
    }

    private VerticalLayout newMessageLayout(WarningMessage message) {
        VerticalLayout messageLayout = new VerticalLayout();
        messageLayout.setSizeFull();
        if (message.getCaption() != null && !message.getCaption().isEmpty()) {
            Label captionLabel = new Label(message.getCaption());
            captionLabel.setStyleName(StyleNames.LABEL_CAPTION);
            messageLayout.addComponent(captionLabel);
        }
        if (message.getDescription() != null && !message.getDescription().isEmpty()) {
            Label descriptionLabel = new Label(message.getDescription());
            descriptionLabel.setStyleName(StyleNames.LABEL_DESCRIPTION);
            descriptionLabel.setContentMode(ContentMode.HTML);
            messageLayout.addComponent(descriptionLabel);
        }

        if (message.getField() != null) {
            messageLayout.addLayoutClickListener(new FocusLayoutClickListener(message.getField()));
        } else if (message.getListener() == null) {
            messageLayout.addLayoutClickListener(new RemoveLayoutClickListener(message));
        } else {
            messageLayout.addLayoutClickListener(message.getListener());
        }
        return messageLayout;
    }

    /**
     * @author klapatnyuk
     */
    private class RemoveLayoutClickListener implements LayoutEvents.LayoutClickListener {

        private static final long serialVersionUID = -7819432488108167586L;

        private final WarningMessage message;

        public RemoveLayoutClickListener(WarningMessage message) {
            this.message = message;
        }

        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
            messages.remove(message);
            reload();
        }
    }

    /**
     * @author klapatnyuk
     */
    private class FocusLayoutClickListener implements LayoutEvents.LayoutClickListener {

        private static final long serialVersionUID = 2409852843395047519L;

        private final Focusable field;

        public FocusLayoutClickListener(Focusable field) {
            this.field = field;
        }

        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
            if (field.isAttached() && field.isVisible()) {
                field.focus();
            }
        }
    }
}