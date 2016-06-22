package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.logic.TemplateServiceImpl;
import ru.klapatnyuk.sberbank.logic.TransactionalProxyService;
import ru.klapatnyuk.sberbank.logic.api.TemplateService;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;
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

/**
 * @author klapatnyuk
 */
public class TemplateTab extends AbstractTab<Template> {

    private static final long serialVersionUID = 621247325187983282L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateTab.class);

    private final TemplateService templateServiceImpl = new TemplateServiceImpl(new TemplateHandler(), new FieldHandler());
    private final TemplateService templateService =
            TransactionalProxyService.newInstance(templateServiceImpl, SberbankUI.connectionPool, TemplateService.class);

    private ConfirmWindow confirmWindow;
    private TemplateTabView design;

    @Override
    public void update() {
        super.update();

        // business logic
        try {
            entities = templateService.getAll();
        } catch (Throwable th) {
            LOGGER.error("Template tab updating error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_GET_ERROR), th,
                            design.getEntityContainer()));
        }

        updateEntityLayout();
    }

    @Override
    public String getHeader() {
        return SberbankUI.I18N.getString(HeaderKey.H2, SberbankUI.I18N.getString(MenuKey.EDITOR),
                SberbankUI.I18N.getString(HeaderKey.TEMPLATE));
    }

    @Override
    public boolean validate() {
        List<WarningMessage> messages = new ArrayList<>();

        // validate title
        messages.addAll(validateTitle());

        // validate template body
        if (design.getTemplateLayout().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_FIELDS_REQUIRED),
                    design.getTemplateLayout().getEmptyRowsField(), getValidationSource()));
        } else if (design.getTemplateLayout().hasDuplicates()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_FIELD_TITLE_UNIQUE_VALIDATE),
                    design.getTemplateLayout().getFirstDuplicateField(), getValidationSource()));
        }
        AbstractField emptyField = design.getTemplateLayout().getFirstEmptyField();
        if (emptyField != null) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_FIELDS_ALL_REQUIRED),
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

        List<Field> fields = null;

        // business logic
        try {
            fields = templateService.getFields(entity.getId());
        } catch (Throwable th) {
            LOGGER.error("Template fields finding error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_FIELD_GET_ERROR), th,
                            event.getSelectedButton()));
            design.getSubmitButton().setEnabled(false);
            design.getCancelButton().setEnabled(false);
            design.getRemoveButton().setEnabled(false);
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

        List<Field> fields = null;

        // business logic
        try {
            fields = templateService.getFields(entity.getId());
        } catch (Throwable th) {
            LOGGER.error("Template fields finding error", th);
            SberbankUI.getWarningWindow()
                    .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_FIELD_GET_ERROR), th,
                            event.getButton()));
            design.getSubmitButton().setEnabled(false);
            design.getCancelButton().setEnabled(false);
            design.getRemoveButton().setEnabled(false);
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

            // business logic
            try {
                templateService.update(template);
            } catch (Throwable th) {
                LOGGER.error("Template updating error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_UPDATE_ERROR), th,
                                design.getSubmitButton()));
                return;
            }

            Tray.show(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_UPDATED));
            entity = template;

        } else {
            Template template = new Template();
            template.setTitle(design.getTitleField().getValue().trim());
            template.setFields(design.getTemplateLayout().getFields());

            // business logic
            try {
                templateService.create(template);
            } catch (Throwable th) {
                LOGGER.error("Template creation error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_CREATE_ERROR), th,
                                design.getSubmitButton()));
                return;
            }

            Tray.show(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_CREATED));
            clear();
        }
        update();
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
                SberbankUI.I18N.getString(FormKey.CONFIRM_TEMPLATE_REMOVE_LABEL));
        confirmWindow.getCancelButton().addClickListener(cancelEvent -> confirmWindow.close());
        confirmWindow.getOkButton().addClickListener(okEvent -> {

            // business logic
            try {
                templateService.remove(entity.getId());
            } catch (Throwable th) {
                LOGGER.error("Template removing error", th);
                SberbankUI.getWarningWindow()
                        .add(new WarningMessage(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_REMOVE_ERROR), th,
                                design.getRemoveButton()));
                return;
            }

            Tray.show(SberbankUI.I18N.getString(NotificationKey.TEMPLATE_REMOVED));
            clear();
            update();
            confirmWindow.close();
        });
    }

    @Override
    protected Predicate<Template> duplicatePredicate(int userId, String title) {
        return item -> item.getTitle().equals(title);
    }
}
