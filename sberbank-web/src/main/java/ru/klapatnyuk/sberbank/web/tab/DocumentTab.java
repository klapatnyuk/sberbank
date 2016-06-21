package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.logic.DocumentServiceImpl;
import ru.klapatnyuk.sberbank.logic.TemplateServiceImpl;
import ru.klapatnyuk.sberbank.logic.TransactionalProxyService;
import ru.klapatnyuk.sberbank.logic.api.DocumentService;
import ru.klapatnyuk.sberbank.logic.api.TemplateService;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;
import ru.klapatnyuk.sberbank.web.key.HeaderKey;
import ru.klapatnyuk.sberbank.web.key.MenuKey;
import ru.klapatnyuk.sberbank.web.key.NotificationKey;
import ru.klapatnyuk.sberbank.web.SberbankSession;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.notification.WarningMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author klapatnyuk
 */
public class DocumentTab extends AbstractTab<Document> {

    private static final long serialVersionUID = 5490116135419202151L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTab.class);

    private final DocumentService documentServiceImpl = new DocumentServiceImpl(new DocumentHandler(), new FieldHandler());
    private final DocumentService documentService = TransactionalProxyService.newInstance(
            documentServiceImpl, SberbankUI.connectionPool, DocumentService.class,
            VaadinServlet.getCurrent().getServletContext().getClassLoader());
    private final TemplateService templateServiceImpl = new TemplateServiceImpl(new TemplateHandler(), new FieldHandler());
    private final TemplateService templateService = TransactionalProxyService.newInstance(
            templateServiceImpl, SberbankUI.connectionPool, TemplateService.class,
            VaadinServlet.getCurrent().getServletContext().getClassLoader());

    private final Property.ValueChangeListener templateListener = new TemplateChangeListener();

    private DocumentTabView design;

    @Override
    public void update() {
        super.update();

        // business logic
        try {
            if (SberbankSession.get().getUser().getRole() == User.Role.ADMIN) {
                entities = documentService.getAll();
            } else {
                entities = documentService.get(SberbankSession.get().getUser());
            }
        } catch (BusinessException e) {
            LOGGER.error("Tab updating error", e);
            // TODO display WarningMessage
        }

        updateEntityLayout();
        updateTemplateSelect();
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(HeaderKey.H2, SberbankUI.I18N.getString(MenuKey.EDITOR),
                SberbankUI.I18N.getString(HeaderKey.DOCUMENT));
    }

    @Override
    public boolean validate() {
        List<WarningMessage> messages = new ArrayList<>();

        if (entityIndex < 0 && design.getTemplateSelect().getValue() == null) {
            // validate template
            messages.add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_TEMPLATE_VALIDATE),
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

        design.getTemplateSelect().setReadOnly(false);
        design.getTemplateSelect().removeValueChangeListener(templateListener);
        design.getTemplateSelect().removeAllItems();
        design.getTemplateSelect().addItem(entity.getTemplate().getTitle());
        design.getTemplateSelect().setValue(entity.getTemplate().getTitle());
        design.getTemplateSelect().setReadOnly(true);

        boolean active = entity.getTemplate().isActive();
        getDesign().getTitleField().setReadOnly(!active);
        design.getTitleLayout().setVisible(true);

        List<Field> fields = null;

        // business logic
        try {
            fields = documentService.getFields(entity.getId());
        } catch (BusinessException e) {
            LOGGER.error("Fields finding error", e);
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

        List<Field> fields = null;

        // business logic
        try {
            fields = documentService.getFields(entity.getId());
        } catch (BusinessException e) {
            LOGGER.error("Fields finding error", e);
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

            // business logic
            try {
                documentService.update(document);
            } catch (BusinessException e) {
                LOGGER.error("Document updating error", e);
                // TODO display WarningMessage
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

            // business logic
            try {
                documentService.create(document);
            } catch (BusinessException e) {
                LOGGER.error("Document creation error", e);
                // TODO display WarningMessage
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

        // business logic
        try {
            documentService.remove(entity.getId());
        } catch (BusinessException e) {
            LOGGER.error("Document removing error", e);
            // TODO display WarningMessage
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

        // business logic
        try {
            templates = templateService.getAll();
        } catch (BusinessException e) {
            LOGGER.error("Templates reading error", e);
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

            // business logic
            try {
                fields = templateService.getFields(templateId);
            } catch (BusinessException e) {
                LOGGER.error("Template fields reading error", e);
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
