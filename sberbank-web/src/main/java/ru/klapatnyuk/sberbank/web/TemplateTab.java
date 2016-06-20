package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
        super.update();

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            entities = new TemplateHandler(connection).findAll();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        updateEntityLayout();
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.PTRN_MESSAGE));
    }

    @Override
    public boolean validate() {
        List<WarningMessage> messages = new ArrayList<>();

        // validate title
        messages.addAll(validateTitle());

        // validate template body
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
        design.getCancelButton().addClickListener(event -> clickCancelButton());
    }

    @Override
    protected AbstractTabView getDesign() {
        if (design == null) {
            design = new TemplateTabView();
        }
        return design;
    }

    @Override
    protected void selectEntity(ButtonGroupSelectionEvent event) {
        super.selectEntity(event);

        // update form
        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByTemplateId(entity.getId());
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
        if (entityIndex >= 0) {
            return;
        }
        super.clickEntityButton(event);

        // update form
        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByTemplateId(entity.getId());
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

    @Override
    protected void clickCancelButton() {
        if (entityIndex >= 0) {
            update();
        } else {
            clear();
            update();
        }
    }

    @Override
    protected Predicate<Template> duplicatePredicate(int userId, String title) {
        return item -> item.getTitle().equals(title);
    }
}
