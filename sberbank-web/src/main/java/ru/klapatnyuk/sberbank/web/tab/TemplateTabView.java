package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import ru.klapatnyuk.sberbank.web.key.FormKey;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.constant.StyleDimensions;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;
import ru.klapatnyuk.sberbank.web.layout.TemplateLayout;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class TemplateTabView extends AbstractTabView {

    private static final long serialVersionUID = -6788254175139485486L;

    @SuppressWarnings("unused")
    private VerticalLayout entityLayout;
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

    TemplateTabView() {
        Design.read(this);
        init();
    }

    @Override
    TemplateLayout getTemplateLayout() {
        return templateLayout;
    }

    @Override
    Label getEditSeparatorLabel() {
        return editSeparatorLabel;
    }

    @Override
    Label getEditLabel() {
        return editLabel;
    }

    @Override
    SingleComponentContainer getEntityContainer() {
        return templateContainer;
    }

    @Override
    AbstractOrderedLayout getEditEntityLayout() {
        return editTemplateLayout;
    }

    @Override
    AbstractTextField getTitleField() {
        return titleField;
    }

    @Override
    Button getCreateButton() {
        return createButton;
    }

    @Override
    Button getSubmitButton() {
        return submitButton;
    }

    @Override
    Button getCancelButton() {
        return cancelButton;
    }

    @Override
    Button getRemoveButton() {
        return removeButton;
    }

    @Override
    void init() {
        entityLayout.setWidthUndefined();
        entityLayout.setHeight("100%");

        createButton.setWidth(StyleDimensions.WIDTH_L);
        createButton.setCaption(SberbankUI.I18N.getString(FormKey.TEMPLATE_CREATE_NEW_BUTTON));
        createButton.addStyleName(StyleNames.BUTTON_ACTIVE);

        editSeparatorLabel.setWidth("100%");
        editSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        editLabel.setValue(SberbankUI.I18N.getString(FormKey.TEMPLATE_EDIT_LABEL));

        verticalSeparatorLabel.setWidth("1px");

        titleLabel.setValue(SberbankUI.I18N.getString(FormKey.ENTITY_TITLE_LABEL));
        titleLabel.setWidth(StyleDimensions.WIDTH_S);
        titleField.setWidth("100%");
        titleField.setInputPrompt(SberbankUI.I18N.getString(FormKey.ENTITY_TITLE_PROMPT));

        titleSeparatorLabel.setWidth("100%");
        titleSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        fieldsLabel.setValue(SberbankUI.I18N.getString(FormKey.TEMPLATE_FIELDS_LABEL));
        fieldsLabel.setWidth(StyleDimensions.WIDTH_S);
        templateLayout.setWidth("100%");

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setCaption(SberbankUI.I18N.getString(FormKey.ENTITY_CREATE));
        submitButton.setWidth(StyleDimensions.WIDTH);
        cancelButton.setCaption(SberbankUI.I18N.getString(FormKey.ENTITY_CLEAR));
        cancelButton.setWidth(StyleDimensions.WIDTH_S);
        removeButton.setCaption(SberbankUI.I18N.getString(FormKey.ENTITY_REMOVE));
        removeButton.setWidth(StyleDimensions.WIDTH_S);
    }
}
