package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroup;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.AbstractEntity;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.web.key.ConfigKey;
import ru.klapatnyuk.sberbank.web.key.FormKey;
import ru.klapatnyuk.sberbank.web.key.NotificationKey;
import ru.klapatnyuk.sberbank.web.SberbankSession;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;
import ru.klapatnyuk.sberbank.web.notification.WarningMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author klapatnyuk
 */
public abstract class AbstractTab<T extends AbstractEntity> extends HorizontalLayout implements Tab {

    protected List<T> entities = new ArrayList<>();
    protected int entityIndex = -1;
    protected T entity;

    private static final int LENGTH =
            Integer.parseInt(SberbankUI.CONFIG.getString(ConfigKey.PATTERN_SUBJECT_LENGTH.getKey()));
    private static final long serialVersionUID = -6229564821654218076L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTab.class);

    private ButtonGroup buttonGroup = new ButtonGroup();

    public AbstractTab() {
        init();
        setVisible(false);
    }

    @Override
    public void update() {
        LOGGER.debug("Tab's update started");

        entities.clear();
        buttonGroup = new ButtonGroup();
        getDesign().getEditEntityLayout().removeAllComponents();
    }

    @Override
    public void poll() {
        // nothing to do
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            update();
        }
        super.setVisible(visible);
    }

    @Override
    public Component getValidationSource() {
        return getDesign().getSubmitButton();
    }

    protected void clickCreateButton() {
        // update model
        entityIndex = -1;

        // update styles
        getDesign().getCreateButton().addStyleName(StyleNames.BUTTON_ACTIVE);
        if (buttonGroup.getSelectedButton() != null) {
            buttonGroup.getSelectedButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }

        // update form
        clear();

        getDesign().getSubmitButton().setCaption(SberbankUI.I18N.getString(FormKey.PTRN_POLL_ADD));
        getDesign().getSubmitButton().setEnabled(true);
        getDesign().getCancelButton().setCaption(SberbankUI.I18N.getString(FormKey.PTRN_POLL_CLEAR));
        getDesign().getCancelButton().setEnabled(true);
        getDesign().getRemoveButton().setVisible(false);
    }

    protected void updateEntityLayout() {
        getDesign().getEditSeparatorLabel().setVisible(entities != null && !entities.isEmpty());
        getDesign().getEditLabel().setVisible(entities != null && !entities.isEmpty());
        getDesign().getEntityContainer().setVisible(entities != null && !entities.isEmpty());

        entities.forEach(item -> {
            Button button = new Button((item.getTitle().length() > LENGTH) ?
                    item.getTitle().substring(0, LENGTH) + ".." : item.getTitle());
            button.setDescription(item.getTitle());
            button.setWidth("100%");
            button.addStyleName("pattern-button");
            button.addClickListener(this::clickEntityButton);
            buttonGroup.addButton(button);
            getDesign().getEditEntityLayout().addComponent(button);
        });

        buttonGroup.addSelectionListener(this::selectEntity);

        if (entityIndex >= buttonGroup.countButtons()) {
            entityIndex = buttonGroup.countButtons() - 1;
        }
        if (entityIndex >= 0) {
            buttonGroup.setSelectedButtonIndex(entityIndex);
            selectEntity(new ButtonGroupSelectionEvent(buttonGroup, buttonGroup.getButtons()[entityIndex], null));
        } else {
            clickCreateButton();
        }
    }

    protected List<WarningMessage> validateTitle() {
        List<WarningMessage> messages = new ArrayList<>();

        if (getDesign().getTitleField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.PTRN_POLL_BODY_VALIDATE),
                    getDesign().getTitleField(), getValidationSource()));

        } else {
            String title = getDesign().getTitleField().getValue().trim();
            boolean duplicate = false;
            if (entityIndex < 0) {
                if (entities.stream().filter(duplicatePredicate(SberbankSession.get().getUser().getId(), title))
                        .findAny().isPresent()) {
                    duplicate = true;
                }
            } else if (entities.stream().filter(item -> !item.equals(entities.get(entityIndex)))
                    .filter(duplicatePredicate(SberbankSession.get().getUser().getId(), title)).findAny().isPresent()) {
                duplicate = true;
            }
            if (duplicate) {
                messages.add(new WarningMessage(
                        SberbankUI.I18N.getString(NotificationKey.PTRN_POLL_BODY_UNIQUE_VALIDATE),
                        getDesign().getTitleField(), getValidationSource()));
            }
        }
        return messages;
    }

    protected void selectEntity(ButtonGroupSelectionEvent event) {
        LOGGER.debug("Inside AbstractTab.selectEntity");

        // update model
        entityIndex = buttonGroup.indexOfButton(buttonGroup.getSelectedButton());
        entity = entities.get(entityIndex);

        // update styles
        if (event.getPreviousButton() != null) {
            event.getPreviousButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }
        getDesign().getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        event.getSelectedButton().addStyleName(StyleNames.BUTTON_ACTIVE);

        // update form
        getDesign().getSubmitButton().setCaption(SberbankUI.I18N.getString(FormKey.PTRN_POLL_SAVE));
        getDesign().getCancelButton().setCaption(SberbankUI.I18N.getString(FormKey.PTRN_POLL_CANCEL));
        // 'admin' role
        getDesign().getRemoveButton().setVisible(SberbankSession.get().getUser().getRole() == User.Role.ADMIN);
        getDesign().getTitleField().setReadOnly(false);
        getDesign().getTitleField().setValue(entity.getTitle());
    }

    protected void clickEntityButton(Button.ClickEvent event) {
        LOGGER.debug("Inside AbstractTab.clickEntityButton");

        // update model
        Button button = event.getButton();
        int index = buttonGroup.indexOfButton(button);
        if (entityIndex == index) {
            return;
        }
        entityIndex = index;
        entity = entities.get(entityIndex);

        // update styles
        getDesign().getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        button.addStyleName(StyleNames.BUTTON_ACTIVE);

        // update form
        getDesign().getSubmitButton().setCaption(SberbankUI.I18N.getString(FormKey.PTRN_POLL_SAVE));
        getDesign().getCancelButton().setCaption(SberbankUI.I18N.getString(FormKey.PTRN_POLL_CANCEL));
        // 'admin' role
        getDesign().getRemoveButton().setVisible(SberbankSession.get().getUser().getRole() == User.Role.ADMIN);
        getDesign().getTitleField().setReadOnly(false);
        getDesign().getTitleField().setValue(entity.getTitle());
    }

    protected void clickCancelButton() {
        if (entityIndex >= 0) {
            update();
        } else {
            clear();
            update();
        }
    }

    protected abstract Predicate<T> duplicatePredicate(int userId, String title);

    protected abstract AbstractTabView getDesign();

    protected abstract void clickRemoveButton();

    protected abstract void clickSubmitButton();

    private void init() {
        LOGGER.debug("Tab's init started");
        setSizeFull();
        addComponent(getDesign());

        getDesign().getCreateButton().addClickListener(event -> clickCreateButton());
        getDesign().getSubmitButton().addClickListener(event -> clickSubmitButton());
        getDesign().getCancelButton().addClickListener(event -> clickCancelButton());

        // 'admin' role
        if (SberbankSession.get().getUser().getRole() == User.Role.ADMIN) {
            getDesign().getRemoveButton().addClickListener(event -> clickRemoveButton());
        }
    }
}
