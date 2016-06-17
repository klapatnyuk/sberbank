package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class DocumentTabView extends TabView {

    private static final long serialVersionUID = -2698683814256829227L;

    @SuppressWarnings("unused")
    private VerticalLayout patternLayout;
    @SuppressWarnings("unused")
    private Button createButton;
    @SuppressWarnings("unused")
    private Label editSeparatorLabel;
    @SuppressWarnings("unused")
    private Label editLabel;
    @SuppressWarnings("unused")
    private Panel patternContainer;
    @SuppressWarnings("unused")
    private VerticalLayout editPatternLayout;
    @SuppressWarnings("unused")
    private Label verticalSeparatorLabel;

    @SuppressWarnings("unused")
    private Label templateLabel;
    @SuppressWarnings("unused")
    private ComboBox templateSelect;

    @SuppressWarnings("unused")
    private Label templateSeparatorLabel;

    @SuppressWarnings("unused")
    private HorizontalLayout titleLayout;
    @SuppressWarnings("unused")
    private Label titleLabel;
    @SuppressWarnings("unused")
    private TextField titleField;

    private Label lineLabel;
    private TextField lineField;
    private Label areaLabel;
    private TextArea areaField;
    private CheckBox checkboxField;

    @SuppressWarnings("unused")
    private Label submitSeparatorLabel;
    @SuppressWarnings("unused")
    private Button submitButton;

    public DocumentTabView() {
        Design.read(this);
        init();
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Label getEditSeparatorLabel() {
        return editSeparatorLabel;
    }

    public Label getEditLabel() {
        return editLabel;
    }

    public SingleComponentContainer getPatternContainer() {
        return patternContainer;
    }

    public AbstractOrderedLayout getEditPatternLayout() {
        return editPatternLayout;
    }

    public AbstractSelect getTemplateSelect() {
        return templateSelect;
    }

    public AbstractOrderedLayout getTitleLayout() {
        return titleLayout;
    }

    public AbstractTextField getTitleField() {
        return titleField;
    }

//    public StringSelect getChoiceSelect() {
//        return choiceSelect;
//    }
//
//    public AbstractField<Boolean> getAllowCustomField() {
//        return allowCustomField;
//    }
//
//    public AbstractOrderedLayout getCustomLayout() {
//        return customLayout;
//    }
//
//    public AbstractTextField getCustomField() {
//        return customField;
//    }
//
//    public AbstractField<Boolean> getAllowMultiplyField() {
//        return allowMultiplyField;
//    }
//
//    public StringSelect getTagSelect() {
//        return tagSelect;
//    }

    public Button getSubmitButton() {
        return submitButton;
    }

    @Override
    protected void init() {
        super.init();

        patternLayout.setWidthUndefined();
        patternLayout.setHeight("100%");

        createButton.setWidth(StyleDimensions.WIDTH_L);
        createButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_CREATE));
        createButton.addStyleName(StyleNames.BUTTON_ACTIVE);

        editSeparatorLabel.setWidth("100%");
        editSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        editLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_MESSAGE_EDIT));

        verticalSeparatorLabel.setWidth("1px");

        templateLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_QUESTION));
        templateLabel.setWidth(StyleDimensions.WIDTH_S);
        templateSelect.setWidth("50%");
        templateSelect.setFilteringMode(FilteringMode.CONTAINS);
        templateSelect.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_PROMPT));

        templateSeparatorLabel.setWidth("100%");
        templateSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        titleLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM_LABEL));
        titleLabel.setWidth(StyleDimensions.WIDTH_S);
        titleField.setWidth("100%");
        titleField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE_CUSTOM_PROMPT));

        lineLabel.setValue("Line label:");
        lineLabel.setWidth(StyleDimensions.WIDTH_S);
        lineField.setWidth("50%");
        lineField.setInputPrompt("Line prompt..");
        areaLabel.setValue("Area label:");
        areaLabel.setWidth(StyleDimensions.WIDTH_S);
        areaField.setHeight("100px");
        areaField.setValidationVisible(false);
        areaField.setInputPrompt("Area prompt..");
        checkboxField.setCaption("Checkbox caption");
        checkboxField.setHeight(StyleDimensions.HEIGHT_S);

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
        submitButton.setWidth(StyleDimensions.WIDTH);
    }
}
