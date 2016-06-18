package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class TemplateTabView extends TabView {

    private static final long serialVersionUID = -6788254175139485486L;
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
    private HorizontalLayout titleLayout;
    @SuppressWarnings("unused")
    private Label titleLabel;
    @SuppressWarnings("unused")
    private TextField titleField;

    @SuppressWarnings("unused")
    private Label titleSeparatorLabel;

    @SuppressWarnings("unused")
    private Label fieldsLabel;
    @SuppressWarnings("unused")
    private TemplateLayout templateLayout;

    @SuppressWarnings("unused")
    private Label submitSeparatorLabel;
    @SuppressWarnings("unused")
    private Button submitButton;

    public TemplateTabView() {
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

    public AbstractOrderedLayout getTitleLayout() {
        return titleLayout;
    }

    public AbstractTextField getTitleField() {
        return titleField;
    }

    public TemplateLayout getTemplateLayout() {
        return templateLayout;
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
        createButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_MESSAGE_CREATE));
        createButton.addStyleName(StyleNames.BUTTON_ACTIVE);

        editSeparatorLabel.setWidth("100%");
        editSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        editLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_EDIT));

        verticalSeparatorLabel.setWidth("1px");

        titleLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CUSTOM_LABEL));
        titleLabel.setWidth(StyleDimensions.WIDTH_S);
        titleField.setWidth("100%");
        titleField.setInputPrompt(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_CHOICE_CUSTOM_PROMPT));

        titleSeparatorLabel.setWidth("100%");
        titleSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        fieldsLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLL_FIELDS_LABEL));
        fieldsLabel.setWidth(StyleDimensions.WIDTH_S);
        templateLayout.setWidth("100%");

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
        submitButton.setWidth(StyleDimensions.WIDTH);
    }
}
