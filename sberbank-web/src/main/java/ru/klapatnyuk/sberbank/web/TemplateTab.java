package ru.klapatnyuk.sberbank.web;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.toggle.ButtonGroupSelectionEvent;
import ru.klapatnyuk.sberbank.logic.TemplateServiceImpl;
import ru.klapatnyuk.sberbank.logic.TransactionalProxyService;
import ru.klapatnyuk.sberbank.logic.api.TemplateService;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.Template;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;
import ru.klapatnyuk.sberbank.model.handler.TemplateHandler;

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
    private final TemplateService templateService = TransactionalProxyService.newInstance(
            templateServiceImpl, SberbankUI.connectionPool, TemplateService.class,
            VaadinServlet.getCurrent().getServletContext().getClassLoader());

    private TemplateTabView design;

    public TemplateTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public void update() {
        super.update();

        // business logic
        try {
            entities = templateService.getAll();
        } catch (BusinessException e) {
            LOGGER.error("Tab updating error", e);
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

        List<Field> fields = null;

        // business logic
        try {
            fields = templateService.getFields(entity.getId());
        } catch (BusinessException e) {
            LOGGER.error("Fields finding error", e);
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

        List<Field> fields = null;

        // business logic
        try {
            fields = templateService.getFields(entity.getId());
        } catch (BusinessException e) {
            LOGGER.error("Fields finding error", e);
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

            // business logic
            try {
                templateService.update(template);
            } catch (BusinessException e) {
                LOGGER.error("Template updating error", e);
                // TODO display WarningMessage
            }

            entity = template;

        } else {
            Template template = new Template();
            template.setTitle(design.getTitleField().getValue().trim());
            template.setFields(design.getTemplateLayout().getFields());

            // business logic
            try {
                templateService.create(template);
            } catch (BusinessException e) {
                LOGGER.error("Template creation error", e);
                // TODO display WarningMessage
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

        // business logic
        try {
            templateService.remove(entity.getId());
        } catch (BusinessException e) {
            LOGGER.error("Template removing error", e);
            // TODO display WarningMessage
        }

        clear();
        update();
    }

    @Override
    protected Predicate<Template> duplicatePredicate(int userId, String title) {
        return item -> item.getTitle().equals(title);
    }
}
