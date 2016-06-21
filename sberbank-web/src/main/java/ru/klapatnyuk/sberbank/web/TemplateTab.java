package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.AbstractEntity;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

            TemplateHandler handler = new TemplateHandler();
            handler.setConnection(connection);
            entities = handler.findAll();

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

            FieldHandler handler = new FieldHandler();
            handler.setConnection(connection);
            fields = handler.findByTemplateId(entity.getId());

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

            FieldHandler handler = new FieldHandler();
            handler.setConnection(connection);
            fields = handler.findByTemplateId(entity.getId());

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
            Template template = new Template();
            template.setId(entity.getId());
            template.setEdited(entity.getEdited());
            template.setTitle(design.getTitleField().getValue().trim());
            template.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);

                // compare edited
                TemplateHandler templateHandler = new TemplateHandler();
                templateHandler.setConnection(connection);
                if (templateHandler.compareEdited(template.getId(), template.getEdited()) > 0) {
                    throw new SQLException("Concurrency editing detected");
                }

                // update template
                templateHandler.updateTemplate(template);

                // remove fields
                FieldHandler fieldHandler = new FieldHandler();
                fieldHandler.setConnection(connection);
                List<Integer> ids = template.getFields().stream().map(AbstractEntity::getId).collect(Collectors.toList());
                fieldHandler.removeTemplateFieldsExcept(template.getId(), ids);

                // update fields
                fieldHandler.insertAndUpdateTemplateFields(template.getId(), template.getFields());

                connection.commit();
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Template edition error", e);
                // TODO display WarningMessage
                return;
            }
            entity = template;

        } else {
            Template template = new Template();
            template.setTitle(design.getTitleField().getValue().trim());
            template.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);

                TemplateHandler templateHandler = new TemplateHandler();
                templateHandler.setConnection(connection);
                int id = templateHandler.createTemplate(template);

                FieldHandler fieldHandler = new FieldHandler();
                fieldHandler.setConnection(connection);
                fieldHandler.createTemplateFields(id, template.getFields());

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
    protected void clickRemoveButton() {
        if (entityIndex < 0) {
            return;
        }

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            connection.setAutoCommit(false);

            TemplateHandler handler = new TemplateHandler();
            handler.setConnection(connection);
            handler.removeTemplate(entity.getId());

            connection.commit();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Document removing error", e);
            // TODO display WarningMessage
            return;
        }

        clear();
        update();
    }

    @Override
    protected Predicate<Template> duplicatePredicate(int userId, String title) {
        return item -> item.getTitle().equals(title);
    }
}
