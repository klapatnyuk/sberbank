package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class DocumentTabView extends TabView {

    private static final long serialVersionUID = -2698683814256829227L;

    private VerticalLayout patternLayout;
    private Button createButton;
    private Label editSeparatorLabel;
    private Label editLabel;
    private Panel patternContainer;
    private VerticalLayout editPatternLayout;
    private Label verticalSeparatorLabel;
    private Label questionLabel;
    private TextArea bodyField;
    private Label choiceLabel;
    private StringSelect choiceSelect;
    private CheckBox allowCustomField;
    private HorizontalLayout customLayout;
    private Label customLabel;
    private TextField customField;
    private CheckBox allowMultiplyField;
    private Label structureSeparatorLabel;
    private Label folderLabel;
    private Label currentFolderLabel;
    private Button folderButton;
    private Label tagLabel;
    private StringSelect tagSelect;
    private Label submitSeparatorLabel;
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

    public AbstractTextField getBodyField() {
        return bodyField;
    }

    public StringSelect getChoiceSelect() {
        return choiceSelect;
    }

    public AbstractField<Boolean> getAllowCustomField() {
        return allowCustomField;
    }

    public AbstractOrderedLayout getCustomLayout() {
        return customLayout;
    }

    public AbstractTextField getCustomField() {
        return customField;
    }

    public AbstractField<Boolean> getAllowMultiplyField() {
        return allowMultiplyField;
    }

    public Label getCurrentFolderLabel() {
        return currentFolderLabel;
    }

    public Button getFolderButton() {
        return folderButton;
    }

    public StringSelect getTagSelect() {
        return tagSelect;
    }

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

        questionLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_QUESTION));
        questionLabel.setWidth(StyleDimensions.WIDTH_S);
        bodyField.setHeight("100px");
        bodyField.setValidationVisible(false);
        bodyField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_PROMPT));

        choiceLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE));
        choiceLabel.setWidth(StyleDimensions.WIDTH_S);

        allowCustomField.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM));
        allowCustomField.setHeight(StyleDimensions.HEIGHT_S);

        customLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM_LABEL));
        customLabel.setWidth(StyleDimensions.WIDTH_S);
        customField.setWidth("100%");
        customField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE_CUSTOM_PROMPT));

        allowMultiplyField.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_MULTIPLY));
        allowMultiplyField.setHeight(StyleDimensions.HEIGHT_S);

        structureSeparatorLabel.setWidth("100%");
        structureSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        folderLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_FOLDER));
        folderLabel.setWidth(StyleDimensions.WIDTH_S);
        currentFolderLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_FOLDER_EMPTY));
        currentFolderLabel.setWidthUndefined();
        folderButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_FOLDER_SET));
        folderButton.setWidth(StyleDimensions.WIDTH);

        tagLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_TAGS));
        tagLabel.setWidth(StyleDimensions.WIDTH_S);

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
        submitButton.setWidth(StyleDimensions.WIDTH);
    }
}
