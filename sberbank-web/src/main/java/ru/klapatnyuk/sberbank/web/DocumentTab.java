package ru.klapatnyuk.sberbank.web;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;
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
public class DocumentTab extends AbstractTab<Document> {

    private static final long serialVersionUID = 5490116135419202151L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTab.class);

    private final Property.ValueChangeListener templateListener = new TemplateChangeListener();

    private DocumentTabView design;

    public DocumentTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public void update() {
        super.update();

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            entities = new DocumentHandler(connection).findAll();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        updateEntityLayout();
        updateTemplateSelect();
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(SberbankKey.Header.H2, SberbankUI.I18N.getString(SberbankKey.Menu.MSGR),
                SberbankUI.I18N.getString(SberbankKey.Header.PTRN_POLL));
    }

    @Override
    public boolean validate() {
        List<WarningMessage> messages = new ArrayList<>();

        if (entityIndex < 0 && design.getTemplateSelect().getValue() == null) {
            // validate template
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_TEMPLATE_VALIDATE),
                    design.getTemplateSelect(), getValidationSource()));
        } else {
            // validate title
            messages.addAll(validateTitle());
        }

        SberbankUI.getWarningWindow().addAll(messages);
        return messages.isEmpty();
    }

    @Override
    public void clear() {
        design.getTemplateSelect().setReadOnly(false);
        design.getTemplateSelect().removeValueChangeListener(templateListener);
        design.getTemplateSelect().removeAllItems();
        design.getTemplateSeparatorLabel().setVisible(false);
        design.getTitleField().clear();
        design.getTemplateLayout().clear();
        design.getTemplateLayout().setVisible(false);
    }

    @Override
    protected void init() {
        super.init();

        design.getCreateButton().addClickListener(event -> clickCreateButton());
        design.getSubmitButton().addClickListener(event -> clickSubmitButton());
        design.getCancelButton().addClickListener(event -> clickCancelButton());
    }

    @Override
    protected void clickCreateButton() {
        super.clickCreateButton();

        design.getTitleLayout().setVisible(false);
        design.getTemplateSeparatorLabel().setVisible(false);

        updateTemplateSelect();
    }

    @Override
    protected AbstractTabView getDesign() {
        if (design == null) {
            design = new DocumentTabView();
        }
        return design;
    }

    @Override
    protected void selectEntity(ButtonGroupSelectionEvent event) {
        super.selectEntity(event);

        design.getTemplateSelect().setReadOnly(false);
        design.getTemplateSelect().removeValueChangeListener(templateListener);
        design.getTemplateSelect().removeAllItems();
        design.getTemplateSelect().addItem(entity.getTemplate().getTitle());
        design.getTemplateSelect().setValue(entity.getTemplate().getTitle());
        design.getTemplateSelect().setReadOnly(true);

        design.getTitleLayout().setVisible(true);

        // update form
        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByDocumentId(entity.getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null || fields.isEmpty()) {
            return;
        }
        design.getTemplateSeparatorLabel().setVisible(true);
        design.getTemplateLayout().setVisible(true);
        design.getTemplateLayout().setFields(fields);
    }

    @Override
    protected void clickEntityButton(Button.ClickEvent event) {
        if (entityIndex >= 0) {
            return;
        }
        super.clickEntityButton(event);

        design.getTemplateSelect().setReadOnly(false);
        design.getTemplateSelect().removeValueChangeListener(templateListener);
        design.getTemplateSelect().removeAllItems();
        design.getTemplateSelect().addItem(entity.getTemplate().getTitle());
        design.getTemplateSelect().setValue(entity.getTemplate().getTitle());
        design.getTemplateSelect().setReadOnly(true);

        design.getTitleLayout().setVisible(true);

        // update form
        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByDocumentId(entity.getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null || fields.isEmpty()) {
            return;
        }
        design.getTemplateSeparatorLabel().setVisible(true);
        design.getTemplateLayout().setVisible(true);
        design.getTemplateLayout().setFields(fields);
    }

    @Override
    protected void clickSubmitButton() {
        if (!validate()) {
            return;
        }

        if (entityIndex >= 0) {
            Document updatedDocument = new Document();

            updatedDocument.setId(entity.getId());
            updatedDocument.setEdited(entity.getEdited());
            updatedDocument.setTitle(design.getTitleField().getValue().trim());
            updatedDocument.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);
                new DocumentHandler(connection).updateDocument(updatedDocument);
                connection.commit();
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Document edition error", e);
                // TODO display WarningMessage
                return;
            }
            entity = updatedDocument;

        } else {
            Document createdDocument = new Document();

            Template template = new Template();
            template.setId((int) design.getTemplateSelect().getValue());
            createdDocument.setTemplate(template);
            createdDocument.setTitle(design.getTitleField().getValue().trim());
            createdDocument.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);
                new DocumentHandler(connection).createDocument(createdDocument);
                connection.commit();
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Document creation error", e);
                // TODO display WarningMessage
                return;
            }
            clear();
            design.getTitleLayout().setVisible(false);
        }

        update();
    }

    @Override
    protected void clickCancelButton() {
        if (entityIndex >= 0) {
            update();
        } else {
            clear();
            design.getTitleLayout().setVisible(false);
            update();
        }
    }

    @Override
    protected Predicate<Document> duplicatePredicate(int userId, String title) {
        return item -> item.getOwner().getId() == userId && item.getTitle().equals(title);
    }

    private void updateTemplateSelect() {
        List<Template> templates = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            templates = new TemplateHandler(connection).findAll();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }
        if (templates == null) {
            return;
        }
        templates.forEach(item -> {
            design.getTemplateSelect().addItem(item.getId());
            design.getTemplateSelect().setItemCaption(item.getId(), item.getTitle());
        });

        design.getTemplateSelect().addValueChangeListener(templateListener);
    }

    /**
     * @author klapatnyuk
     */
    private class TemplateChangeListener implements Property.ValueChangeListener {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            Integer templateId = (Integer) event.getProperty().getValue();
            design.getTitleLayout().setVisible(templateId != null);

            if (templateId == null) {
                clear();
                updateTemplateSelect();
                return;
            }

            design.getTitleField().clear();

            List<Field> fields = null;
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                fields = new FieldHandler(connection).findByTemplateId(templateId);
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Templates finding error", e);
                // TODO display WarningMessage
            }

            if (fields == null || fields.isEmpty()) {
                return;
            }
            // move ids to referenceIds
            fields.forEach(item -> {
                item.setReferenceId(item.getId());
                item.setId(0);
            });

            design.getTemplateSeparatorLabel().setVisible(true);
            design.getTemplateLayout().setVisible(true);
            design.getTemplateLayout().setFields(fields);
        }
    }
}
