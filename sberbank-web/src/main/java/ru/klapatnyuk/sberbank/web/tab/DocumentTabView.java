package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import ru.klapatnyuk.sberbank.web.key.FormKey;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.style.StyleDimensions;
import ru.klapatnyuk.sberbank.web.style.StyleNames;
import ru.klapatnyuk.sberbank.web.layout.DocumentLayout;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class DocumentTabView extends AbstractTabView {

    private static final long serialVersionUID = -2698683814256829227L;

    @SuppressWarnings("unused")
    private VerticalLayout entityLayout;
    @SuppressWarnings("unused")
    private Button createButton;
    @SuppressWarnings("unused")
    private Label editSeparatorLabel;
    @SuppressWarnings("unused")
    private Label editLabel;
    @SuppressWarnings("unused")
    private Panel documentContainer;
    @SuppressWarnings("unused")
    private VerticalLayout editDocumentLayout;
    @SuppressWarnings("unused")
    private Label verticalSeparatorLabel;
    @SuppressWarnings("unused")
    private Label templateLabel;
    @SuppressWarnings("unused")
    private ComboBox templateSelect;
    @SuppressWarnings("unused")
    private HorizontalLayout titleLayout;
    @SuppressWarnings("unused")
    private Label titleLabel;
    @SuppressWarnings("unused")
    private TextField titleField;
    @SuppressWarnings("unused")
    private Label templateSeparatorLabel;
    @SuppressWarnings("unused")
    private DocumentLayout documentLayout;
    @SuppressWarnings("unused")
    private Label submitSeparatorLabel;
    @SuppressWarnings("unused")
    private Button submitButton;
    @SuppressWarnings("unused")
    private Button cancelButton;
    @SuppressWarnings("unused")
    private Button removeButton;

    DocumentTabView() {
        Design.read(this);
        init();
    }

    AbstractSelect getTemplateSelect() {
        return templateSelect;
    }

    AbstractOrderedLayout getTitleLayout() {
        return titleLayout;
    }

    Label getTemplateSeparatorLabel() {
        return templateSeparatorLabel;
    }

    @Override
    DocumentLayout getTemplateLayout() {
        return documentLayout;
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
        return documentContainer;
    }

    @Override
    AbstractOrderedLayout getEditEntityLayout() {
        return editDocumentLayout;
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
        createButton.setCaption(SberbankUI.I18N.getString(FormKey.DOCUMENT_CREATE_NEW_BUTTON));
        createButton.addStyleName(StyleNames.BUTTON_ACTIVE);

        editSeparatorLabel.setWidth("100%");
        editSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        editLabel.setValue(SberbankUI.I18N.getString(FormKey.DOCUMENT_EDIT_LABEL));

        verticalSeparatorLabel.setWidth("1px");

        templateLabel.setValue(SberbankUI.I18N.getString(FormKey.DOCUMENT_TEMPLATE_LABEL));
        templateLabel.setWidth(StyleDimensions.WIDTH_S);
        templateSelect.setWidth("50%");
        templateSelect.setFilteringMode(FilteringMode.CONTAINS);
        templateSelect.setInputPrompt(SberbankUI.I18N.getString(FormKey.DOCUMENT_TEMPLATE_PROMPT));

        templateSeparatorLabel.setWidth("100%");
        templateSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        titleLabel.setValue(SberbankUI.I18N.getString(FormKey.ENTITY_TITLE_LABEL));
        titleLabel.setWidth(StyleDimensions.WIDTH_S);
        titleField.setWidth("100%");
        titleField.setInputPrompt(SberbankUI.I18N.getString(FormKey.ENTITY_TITLE_PROMPT));

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
