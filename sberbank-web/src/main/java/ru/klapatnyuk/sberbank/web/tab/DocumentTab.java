package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
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
import ru.klapatnyuk.sberbank.model.handler.DocumentFieldHandler;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateFieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;
import ru.klapatnyuk.sberbank.web.SberbankSession;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.key.FormKey;
import ru.klapatnyuk.sberbank.web.key.HeaderKey;
import ru.klapatnyuk.sberbank.web.key.MenuKey;
import ru.klapatnyuk.sberbank.web.key.NotificationKey;
import ru.klapatnyuk.sberbank.web.notification.Tray;
import ru.klapatnyuk.sberbank.web.notification.WarningMessage;
import ru.klapatnyuk.sberbank.web.window.ConfirmWindow;

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

    private final DocumentService documentServiceImpl =
            new DocumentServiceImpl(new DocumentHandler(), new DocumentFieldHandler());
    private final DocumentService documentService =
            TransactionalProxyService.newInstance(documentServiceImpl, SberbankUI.connectionPool, DocumentService.class);
    private final TemplateService templateServiceImpl =
            new TemplateServiceImpl(new TemplateHandler(), new TemplateFieldHandler());
    private final TemplateService templateService =
            TransactionalProxyService.newInstance(templateServiceImpl, SberbankUI.connectionPool, TemplateService.class);

    private final Property.ValueChangeListener templateListener = new TemplateChangeListener();

    private ConfirmWindow confirmWindow;
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
        } catch (Throwable th) {
            LOGGER.error("Document tab updating error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_GET_ERROR), th,
                            design.getEntityContainer()));
        }

        updateEntityLayout();
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
        } catch (Throwable th) {
            LOGGER.error("Document fields finding error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_FIELD_GET_ERROR), th,
                            event.getSelectedButton()));
            design.getSubmitButton().setEnabled(false);
            design.getCancelButton().setEnabled(false);
            design.getRemoveButton().setEnabled(false);
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
        } catch (Throwable th) {
            LOGGER.error("Document fields finding error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_FIELD_GET_ERROR), th,
                            event.getButton()));
            design.getSubmitButton().setEnabled(false);
            design.getCancelButton().setEnabled(false);
            design.getRemoveButton().setEnabled(false);
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
            Document document = Document.newBuilder()
                    .setId(entity.getId())
                    .setEdited(entity.getEdited())
                    .setTitle(design.getTitleField().getValue().trim())
                    .setFields(design.getTemplateLayout().getFields())
                    .build();

            // business logic
            try {
                documentService.update(document);
            } catch (Throwable th) {
                LOGGER.error("Document updating error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_UPDATE_ERROR), th,
                                design.getSubmitButton()));
                return;
            }

            Tray.show(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_UPDATED));
            entity = document;

        } else {
            Document document = Document.newBuilder()
                    .setTemplate(Template.newBuilder()
                            .setId((int) design.getTemplateSelect().getValue())
                            .build())
                    .setTitle(design.getTitleField().getValue().trim())
                    .setFields(design.getTemplateLayout().getFields())
                    .setOwner(SberbankSession.get().getUser())
                    .build();

            // business logic
            try {
                documentService.create(document);
            } catch (Throwable th) {
                LOGGER.error("Document creation error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_CREATE_ERROR), th,
                                design.getSubmitButton()));
                return;
            }

            Tray.show(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_CREATED));
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
        UI.getCurrent().addWindow(confirmWindow);
    }

    @Override
    protected void init() {
        super.init();

        confirmWindow = new ConfirmWindow(SberbankUI.I18N.getString(HeaderKey.WINDOW_CONFIRM_REMOVE),
                SberbankUI.I18N.getString(FormKey.CONFIRM_DOCUMENT_REMOVE_LABEL));
        confirmWindow.getCancelButton().addClickListener(cancelEvent -> confirmWindow.close());
        confirmWindow.getOkButton().addClickListener(okEvent -> {

            // business logic
            try {
                documentService.remove(entity.getId());
            } catch (Throwable th) {
                LOGGER.error("Document removing error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_REMOVE_ERROR), th,
                                design.getRemoveButton()));
                return;
            }

            Tray.show(SberbankUI.I18N.getString(NotificationKey.DOCUMENT_REMOVED));
            clear();
            design.getTitleLayout().setVisible(true);
            update();
            confirmWindow.close();
        });
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
        } catch (Throwable th) {
            LOGGER.error("Templates reading error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_GET_ERROR), th,
                            design.getTemplateSelect()));
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
            } catch (Throwable th) {
                LOGGER.error("Template fields reading error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_FIELD_GET_ERROR), th,
                                design.getTemplateSelect()));
                design.getSubmitButton().setEnabled(false);
                design.getCancelButton().setEnabled(false);
            }

            if (fields == null || fields.isEmpty()) {
                return;
            }
            // move ids to referenceIds
            fields = fields.stream()
                    .map(item -> Field.newBuilder()
                            // set id = 0
                            .setId(0)
                            // set referenceId = id
                            .setReferenceId(item.getId())
                            .setTitle(item.getTitle())
                            .setLabel(item.getLabel())
                            .setType(item.getType())
                            .setIndex(item.getIndex())
                            .setRelated(item.getRelated())
                            .build())
                    .collect(Collectors.toList());

            design.getTemplateSeparatorLabel().setVisible(true);
            design.getTemplateLayout().setVisible(true);
            design.getTemplateLayout().setFields(fields);
        }
    }
}
