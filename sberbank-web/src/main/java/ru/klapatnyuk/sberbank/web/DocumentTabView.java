package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class DocumentTabView extends AbstractTabView {

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

    public DocumentTabView() {
        Design.read(this);
        init();
    }

    public AbstractSelect getTemplateSelect() {
        return templateSelect;
    }

    public AbstractOrderedLayout getTitleLayout() {
        return titleLayout;
    }

    public DocumentLayout getTemplateLayout() {
        return documentLayout;
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
        return documentContainer;
    }

    @Override
    public AbstractOrderedLayout getEditEntityLayout() {
        return editDocumentLayout;
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

        submitSeparatorLabel.setWidth("100%");
        submitSeparatorLabel.setHeight(StyleDimensions.SEPARATOR_HEIGHT);

        submitButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.PTRN_POLL_ADD));
        submitButton.setWidth(StyleDimensions.WIDTH);
    }
}
