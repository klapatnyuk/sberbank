package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class TemplateTabView extends AbstractTabView {

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
    private Panel templateContainer;
    @SuppressWarnings("unused")
    private VerticalLayout editTemplateLayout;
    @SuppressWarnings("unused")
    private Label verticalSeparatorLabel;
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
    @SuppressWarnings("unused")
    private Button cancelButton;
    @SuppressWarnings("unused")
    private Button removeButton;

    public TemplateTabView() {
        Design.read(this);
        init();
    }

    public TemplateLayout getTemplateLayout() {
        return templateLayout;
    }

    @Override
    public Label getEditSeparatorLabel() {
        return editSeparatorLabel;
    }

    @Override
    public Label getEditLabel() {
        return editLabel;
    }

    @Override
    public SingleComponentContainer getEntityContainer() {
        return templateContainer;
    }

    @Override
    public AbstractOrderedLayout getEditEntityLayout() {
        return editTemplateLayout;
    }

    @Override
    public AbstractTextField getTitleField() {
        return titleField;
    }

    @Override
    public Button getCreateButton() {
        return createButton;
    }

    @Override
    public Button getSubmitButton() {
        return submitButton;
    }

    @Override
    public Button getCancelButton() {
        return cancelButton;
    }

    @Override
    public Button getRemoveButton() {
        return removeButton;
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
        cancelButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_CLEAR));
        cancelButton.setWidth(StyleDimensions.WIDTH_S);
        removeButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_REMOVE));
        removeButton.setWidth(StyleDimensions.WIDTH_S);
    }
}
