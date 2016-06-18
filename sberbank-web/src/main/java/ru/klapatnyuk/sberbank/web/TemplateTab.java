package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
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
public class TemplateTab extends AbstractTab implements EditableTab {

    private static final long serialVersionUID = 621247325187983282L;
    private static final int LENGTH =
            Integer.parseInt(SberbankUI.CONFIG.getString(ConfigKey.PATTERN_SUBJECT_LENGTH.getKey()));
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateTab.class);

    private List<Template> templates = new ArrayList<>();

    private int templateIndex = -1;
    private Template template;

    private ButtonGroup buttonGroup = new ButtonGroup();
    private TemplateTabView design;

    public TemplateTab(MenuTab tab, MenuTab actionTab) {
        super(tab, actionTab);
    }

    @Override
    public boolean isUpdated() {
        return false;
    }

    @Override
    public void update() {
        templates.clear();
        buttonGroup = new ButtonGroup();
        design.getEditPatternLayout().removeAllComponents();

        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            templates = new TemplateHandler(connection).findAll();
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }
        design.getEditSeparatorLabel().setVisible(templates != null);
        design.getEditLabel().setVisible(templates != null);
        design.getPatternContainer().setVisible(templates != null);

        templates.forEach(item -> {
            Button button = new Button((item.getTitle().length() > LENGTH) ?
                    item.getTitle().substring(0, LENGTH) + ".." : item.getTitle());
            button.setDescription(item.getTitle());
            button.setWidth("100%");
            button.addStyleName("pattern-button");
            button.addClickListener(this::clickTemplateButton);
            buttonGroup.addButton(button);
            design.getEditPatternLayout().addComponent(button);
        });

        buttonGroup.addSelectionListener(this::selectTemplate);

        if (templateIndex >= 0) {
            buttonGroup.setSelectedButtonIndex(templateIndex);
            selectTemplate(new ButtonGroupSelectionEvent(buttonGroup, buttonGroup.getButtons()[templateIndex], null));
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
        }

        /*if (design.getChoiceSelect().getStrings().size() < 2) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CHOICES_REQUIRED),
                    design.getChoiceSelect().getLastField(),
                    getValidationSource()));
        } else if (design.getChoiceSelect().hasDuplicates()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CHOICES_DUPLICATES),
                    design.getChoiceSelect().getFirstDuplicateField(),
                    getValidationSource()));
        }

        if (design.getAllowCustomField().getValue() && design.getCustomField().getValue().trim().isEmpty()) {
            messages.add(new WarningMessage(SberbankUI.I18N.getString(SberbankKey.Notification.PTRN_POLL_CUSTOM_REQUIRED),
                    design.getCustomField(), getValidationSource()));
        }*/

        SberbankUI.getWarningWindow().addAll(messages);
        return messages.isEmpty();
    }

    @Override
    public Component getValidationSource() {
        return design.getSubmitButton();
    }

    @Override
    public void clear() {
        design.getTitleField().clear();
        design.getFieldsLayout().clear();
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

    private void selectTemplate(ButtonGroupSelectionEvent event) {
        LOGGER.debug("Inside TemplateTab.selectTemplate");

        // update model
        templateIndex = buttonGroup.indexOfButton(buttonGroup.getSelectedButton());
        template = templates.get(templateIndex);

        // update styles
        if (event.getPreviousButton() != null) {
            event.getPreviousButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }
        design.getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        event.getSelectedButton().addStyleName(StyleNames.BUTTON_ACTIVE);

        // update form
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_SAVE));
        design.getTitleField().setValue(template.getTitle());

        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByTemplateId(templates.get(templateIndex).getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null) {
            return;
        }
        design.getFieldsLayout().setFields(fields);
    }

    private void clickTemplateButton(Button.ClickEvent event) {
        LOGGER.debug("Inside TemplateTab.clickTemplateButton");
        if (templateIndex >= 0) {
            return;
        }

        // update model
        Button button = event.getButton();
        int index = buttonGroup.indexOfButton(button);
        if (templateIndex == index) {
            return;
        }
        templateIndex = index;
        template = templates.get(templateIndex);

        // update styles
        design.getCreateButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        button.addStyleName(StyleNames.BUTTON_ACTIVE);

        // update form
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_MESSAGE_SAVE));
        design.getTitleField().setValue(template.getTitle());

        List<Field> fields = null;
        try {
            Connection connection = SberbankUI.connectionPool.reserveConnection();
            fields = new FieldHandler(connection).findByTemplateId(templates.get(templateIndex).getId());
            SberbankUI.connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Templates finding error", e);
            // TODO display WarningMessage
        }

        if (fields == null) {
            return;
        }
        design.getFieldsLayout().setFields(fields);
    }

    private void clickCreateButton() {
        if (templateIndex < 0) {
            return;
        }

        // update model
        templateIndex = -1;

        // update styles
        design.getCreateButton().addStyleName(StyleNames.BUTTON_ACTIVE);
        if (buttonGroup.getSelectedButton() != null) {
            buttonGroup.getSelectedButton().removeStyleName(StyleNames.BUTTON_ACTIVE);
        }

        // update form
        clear();
        design.getSubmitButton().setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
    }

    private void clickSubmitButton() {
        if (!validate()) {
            return;
        }

        /*if (templateIndex >= 0) {
            // edit existed pattern
            PollPatternRequest model = new PollPatternRequest();
            model.setSid(pattern.getSid());
            model.setDeleted(Pattern.DELETED);
            model.setBody(design.getBodyField().getValue().trim());
            model.setChoiceType(design.getAllowMultiplyField().getValue() ? PollQuestionType.MULTIPLE_ANSWERS.toString()
                    : PollQuestionType.SINGLE_ANSWER.toString());
            model.setAnswers(design.getChoiceSelect().getStrings());

            model.setCustom(design.getAllowCustomField().getValue());
            if (design.getAllowCustomField().getValue()) {
                model.setCustomAnswer(design.getCustomField().getValue().trim());
            }
            model.setTags(design.getTagSelect().getUniqueStrings());

            PollPatternsRequest request = new PollPatternsRequest(Arrays.asList(model));
            PollPatternsResponse response;
            try {
                response = service.setPolls(request);
            } catch (RemoteServiceException e) {
                BrownieErrorHandler.handle(e, I18N.getString(PTRN_POLL_EDIT_ERROR));
                return;
            }
            PollPattern pattern = PollPattern.valueOf(response.getData().get(0));
            List<PollPattern> patterns = BrownieSession.getMasterdata().getPolls();
            patterns.set(templateIndex, pattern);
            Collections.sort(patterns);
            int index = patterns.stream().map(Pattern::getSid).collect(Collectors.toList()).indexOf(pattern.getSid());
            templateIndex = (index < 0) ? 0 : index;
            Tray.show(I18N.getString(PTRN_POLL_UPDATED));
            update();
        } else {
            // create new pattern
            PollPatternRequest model = new PollPatternRequest();
            model.setBody(design.getBodyField().getValue().trim());
            model.setChoiceType(design.getAllowMultiplyField().getValue() ? PollQuestionType.MULTIPLE_ANSWERS.toString()
                    : PollQuestionType.SINGLE_ANSWER.toString());
            model.setAnswers(design.getChoiceSelect().getStrings());
            model.setCustom(design.getAllowCustomField().getValue());
            if (design.getAllowCustomField().getValue()) {
                model.setCustomAnswer(design.getCustomField().getValue().trim());
            }
            model.setTags(design.getTagSelect().getUniqueStrings());
            model.setDeleted(false);

            PollPatternsRequest request = new PollPatternsRequest(Arrays.asList(model));
            PollPatternsResponse response = null;
            try {
                response = service.setPolls(request);
            } catch (RemoteServiceException e) {
                BrownieErrorHandler.handle(e, I18N.getString(PTRN_POLL_CREATE_ERROR));
            }
            if (response != null) {
                PollPattern pattern = PollPattern.valueOf(response.getData().get(0));
                List<PollPattern> patterns = BrownieSession.getMasterdata().getPolls();
                patterns.add(pattern);
                Collections.sort(patterns);
                Tray.show(I18N.getString(PTRN_POLL_CREATED));
                clear();
                update();
            }
        }*/
    }
}
