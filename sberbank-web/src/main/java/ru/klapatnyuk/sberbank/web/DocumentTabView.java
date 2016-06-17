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

//    private Label choiceLabel;
//    private StringSelect choiceSelect;
//    private CheckBox allowCustomField;
//    private HorizontalLayout customLayout;
//    private Label customLabel;
//    private TextField customField;
//    private CheckBox allowMultiplyField;
//    private Label structureSeparatorLabel;
//    private Label tagLabel;
//    private StringSelect tagSelect;

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

        editLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_EDIT));

        verticalSeparatorLabel.setWidth("1px");

        templateLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_QUESTION));
        templateLabel.setWidth(StyleDimensions.WIDTH_S);
        templateSelect.setWidth("50%");
        templateSelect.setFilteringMode(FilteringMode.CONTAINS);
        templateSelect.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_PROMPT));

//        choiceLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE));
//        choiceLabel.setWidth(StyleDimensions.WIDTH_S);
//
//        allowCustomField.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM));
//        allowCustomField.setHeight(StyleDimensions.HEIGHT_S);
//
//        customLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM_LABEL));
//        customLabel.setWidth(StyleDimensions.WIDTH_S);
//        customField.setWidth("100%");
//        customField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE_CUSTOM_PROMPT));
//
//        allowMultiplyField.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_MULTIPLY));
//        allowMultiplyField.setHeight(StyleDimensions.HEIGHT_S);
//
//        structureSeparatorLabel.setWidth("100%");
//        structureSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);
//
//        tagLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_TAGS));
//        tagLabel.setWidth(StyleDimensions.WIDTH_S);

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
        submitButton.setWidth(StyleDimensions.WIDTH);
    }
}
