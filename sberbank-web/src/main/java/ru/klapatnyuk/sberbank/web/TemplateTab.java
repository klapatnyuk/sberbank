package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroup;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klapatnyuk
 */
public class TemplateTab extends AbstractTab<Template> {

    private static final long serialVersionUID = 621247325187983282L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateTab.class);

    private TemplateTabView design;

    public TemplateTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public void update() {
        entities.clear();
        buttonGroup = new ButtonGroup();
        design.getEditPatternLayout().removeAllComponents();

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            entities = new TemplateHandler(connection).findAll();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }
        design.getEditSeparatorLabel().setVisible(entities != null);
        design.getEditLabel().setVisible(entities != null);
        design.getPatternContainer().setVisible(entities != null);

        entities.forEach(item -> {
            Button button = new Button((item.getTitle().length() > LENGTH) ?
                    item.getTitle().substring(0, LENGTH) + ".." : item.getTitle());
            button.setDescription(item.getTitle());
            button.setWidth("100%");
            button.addStyleName("pattern-button");
            button.addClickListener(this::clickEntityButton);
            buttonGroup.addButton(button);
            design.getEditPatternLayout().addComponent(button);
        });

        buttonGroup.addSelectionListener(this::selectEntity);

        if (entityIndex >= 0) {
            buttonGroup.setSelectedButtonIndex(entityIndex);
            selectEntity(new ButtonGroupSelectionEvent(buttonGroup, buttonGroup.getButtons()[entityIndex], null));
        }
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.PTRN_MESSAGE));
    }

    @Override
    public boolean validate() {
        List<WarningMessage> messages = new ArrayList<>();

        if (design.getTitleField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_BODY_VALIDATE),
                    design.getTitleField(), getValidationSource()));
        } else {
            String title = design.getTitleField().getValue().trim();
            boolean duplicate = false;
            if (entityIndex < 0) {
                if (entities.stream().map(Template::getTitle).filter(item -> item.equals(title)).findAny().isPresent()) {
                    duplicate = true;
                }
            } else if (entities.stream().filter(item -> !item.equals(entities.get(entityIndex)))
                    .map(Template::getTitle).filter(item -> item.equals(title)).findAny().isPresent()) {
                duplicate = true;
            }
            if (duplicate) {
                messages.add(new WarningMessage(
                        SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_BODY_UNIQUE_VALIDATE),
                        design.getTitleField(), getValidationSource()));
            }
        }

        if (design.getTemplateLayout().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CHOICES_REQUIRED),
                    design.getTemplateLayout().getEmptyRowsField(), getValidationSource()));
        } else if (design.getTemplateLayout().hasDuplicates()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CHOICES_DUPLICATES),
                    design.getTemplateLayout().getFirstDuplicateField(), getValidationSource()));
        }

        AbstractField emptyField = design.getTemplateLayout().getFirstEmptyField();
        if (emptyField != null) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CUSTOM_REQUIRED),
                    emptyField, getValidationSource()));
        }

        SberbankUI.getWarningWindow().addAll(messages);
        return messages.isEmpty();
    }

    @Override
    public void clear() {
        design.getTitleField().clear();
        design.getTemplateLayout().clear();
    }

    @Override
    protected void init() {
        super.init();

        design.getCreateButton().addClickListener(event -> clickCreateButton());
        design.getSubmitButton().addClickListener(event -> clickSubmitButton());
    }

    @Override
    protected TabView getDesign() {
        if (design == null) {
            design = new TemplateTabView();
        }
        return design;
    }

    @Override
    protected void selectEntity(ButtonGroupSelectionEvent event) {
        LOGGER.debug("Inside TemplateTab.selectEntity");

        // update model
        entityIndex = buttonGroup.indexOfButton(buttonGroup.getSelectedButton());
        entity = entities.get(entityIndex);

        // update styles
        if (event.getPreviousButton() != null) {
            event.getPreviousButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }
        design.getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        event.getSelectedButton().addStyleName(StyleNames.BUTTON_ACTIVE);

        // update form
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_SAVE));
        design.getTitleField().setValue(entity.getTitle());

        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByTemplateId(entities.get(entityIndex).getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null) {
            return;
        }
        design.getTemplateLayout().setFields(fields);
    }

    @Override
    protected void clickEntityButton(Button.ClickEvent event) {
        LOGGER.debug("Inside TemplateTab.clickEntityButton");
        if (entityIndex >= 0) {
            return;
        }

        // update model
        Button button = event.getButton();
        int index = buttonGroup.indexOfButton(button);
        if (entityIndex == index) {
            return;
        }
        entityIndex = index;
        entity = entities.get(entityIndex);

        // update styles
        design.getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        button.addStyleName(StyleNames.BUTTON_ACTIVE);

        // update form
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_MESSAGE_SAVE));
        design.getTitleField().setValue(entity.getTitle());

        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByTemplateId(entities.get(entityIndex).getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null) {
            return;
        }
        design.getTemplateLayout().setFields(fields);
    }

    @Override
    protected void clickSubmitButton() {
        if (!validate()) {
            return;
        }
        if (entityIndex >= 0) {
            Template updatedTemplate = new Template();
            updatedTemplate.setId(entity.getId());
            updatedTemplate.setEdited(entity.getEdited());
            updatedTemplate.setTitle(design.getTitleField().getValue().trim());
            updatedTemplate.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);
                new TemplateHandler(connection).updateTemplate(updatedTemplate);
                connection.commit();
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Template edition error", e);
                // TODO display WarningMessage
                return;
            }
            entity = updatedTemplate;

        } else {
            Template createdTemplate = new Template();
            createdTemplate.setTitle(design.getTitleField().getValue().trim());
            createdTemplate.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);
                new TemplateHandler(connection).createTemplate(createdTemplate);
                connection.commit();
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Template creation error", e);
                // TODO display WarningMessage
                return;
            }
            clear();
        }

        update();
    }
}
