package ru.klapatnyuk.sberbank.web;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.model.entity.*;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;
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

            if (SberbankSession.get().getUser().getRole() == User.Role.ADMIN) {
                // 'admin' role
                entities = new DocumentHandler(connection).findAll();
            } else {
                // other roles
                entities = new DocumentHandler(connection).findByOwnerId(SberbankSession.get().getUser().getId());
            }

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
        design.getTitleField().setReadOnly(false);
        design.getTitleField().clear();
        design.getTemplateLayout().clear();
        design.getTemplateLayout().setVisible(false);
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

        boolean active = entity.getTemplate().isActive();

        design.getTemplateSelect().setReadOnly(false);
        design.getTemplateSelect().removeValueChangeListener(templateListener);
        design.getTemplateSelect().removeAllItems();
        design.getTemplateSelect().addItem(entity.getTemplate().getTitle());
        design.getTemplateSelect().setValue(entity.getTemplate().getTitle());
        design.getTemplateSelect().setReadOnly(true);

        getDesign().getTitleField().setReadOnly(!active);
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
        design.getTemplateLayout().setReadOnly(!active);

        design.getSubmitButton().setEnabled(active);
        design.getCancelButton().setEnabled(active);
    }

    @Override
    protected void clickEntityButton(Button.ClickEvent event) {
        if (entityIndex >= 0) {
            return;
        }
        super.clickEntityButton(event);

        boolean active = entity.getTemplate().isActive();

        design.getTemplateSelect().setReadOnly(false);
        design.getTemplateSelect().removeValueChangeListener(templateListener);
        design.getTemplateSelect().removeAllItems();
        design.getTemplateSelect().addItem(entity.getTemplate().getTitle());
        design.getTemplateSelect().setValue(entity.getTemplate().getTitle());
        design.getTemplateSelect().setReadOnly(true);

        design.getTitleField().setReadOnly(!active);
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
        design.getTemplateLayout().setReadOnly(!active);

        design.getSubmitButton().setEnabled(active);
        design.getCancelButton().setEnabled(active);
    }

    @Override
    protected void clickSubmitButton() {
        if (!validate()) {
            return;
        }

        if (entityIndex >= 0) {
            Document document = new Document();

            document.setId(entity.getId());
            document.setEdited(entity.getEdited());
            document.setTitle(design.getTitleField().getValue().trim());
            document.setFields(design.getTemplateLayout().getFields());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);

                // compare edited
                DocumentHandler documentHandler = new DocumentHandler(connection);
                if (documentHandler.compareEdited(document.getId(), document.getEdited()) > 0) {
                    throw new SQLException("Concurrency editing detected");
                }

                // update document
                documentHandler.updateDocument(document);

                // remove fields
                FieldHandler fieldHandler = new FieldHandler(connection);
                List<Integer> ids = document.getFields().stream().map(AbstractEntity::getId).collect(Collectors.toList());
                if (ids.isEmpty()) {
                    fieldHandler.removeDocumentFields(document.getId());
                } else {
                    fieldHandler.removeDocumentFieldsExcept(document.getId(), ids);
                }

                // update fields
                fieldHandler.insertAndUpdateDocumentFields(document.getId(), document.getFields());

                connection.commit();
                SberbankUI.connectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("Document edition error", e);
                // TODO display WarningMessage
                return;
            }
            entity = document;

        } else {
            Document document = new Document();

            Template template = new Template();
            template.setId((int) design.getTemplateSelect().getValue());
            document.setTemplate(template);
            document.setTitle(design.getTitleField().getValue().trim());
            document.setFields(design.getTemplateLayout().getFields());
            document.setOwner(SberbankSession.get().getUser());
            try {
                Connection connection = SberbankUI.connectionPool.reserveConnection();
                connection.setAutoCommit(false);

                int id = new DocumentHandler(connection).createDocument(document);
                new FieldHandler(connection).createDocumentFields(id, document.getFields());

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
        super.clickCancelButton();

        if (entityIndex < 0) {
            design.getTitleLayout().setVisible(false);
        }
    }

    @Override
    protected void clickRemoveButton() {
        if (entityIndex < 0) {
            return;
        }

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            connection.setAutoCommit(false);
            new DocumentHandler(connection).removeDocument(entity.getId());
            connection.commit();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Document removing error", e);
            // TODO display WarningMessage
            return;
        }

        clear();

        design.getTitleLayout().setVisible(true);
        update();
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

            design.getTitleField().setReadOnly(false);
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
