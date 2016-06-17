package ru.klapatnyuk.sberbank.web;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;

/**
 * @author klapatnyuk
 */
@DesignRoot
public class DocumentTabView extends TabView {

    private static final long serialVersionUID = -6068846613188718674L;

    private Panel pollContainer;
    private VerticalLayout pollLayout;
    private Label emptyLabel;
    private Label verticalSeparatorLabel;
    private Label emptySelectionLabel;
    private Label statusLabel;
    private Label questionLabel;
    private VerticalLayout choiceLayout;
    private HorizontalLayout printLayout;
    private Button printButton;

    public DocumentTabView() {
        Design.read(this);
        init();
    }

    public AbstractOrderedLayout getPollLayout() {
        return pollLayout;
    }

    public Label getEmptySelectionLabel() {
        return emptySelectionLabel;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public Label getQuestionLabel() {
        return questionLabel;
    }

    public AbstractOrderedLayout getChoiceLayout() {
        return choiceLayout;
    }

    public AbstractOrderedLayout getPrintLayout() {
        return printLayout;
    }

    public Label getEmptyLabel() {
        return emptyLabel;
    }

    @Override
    protected void init() {
        super.init();

        pollContainer.setWidth("240px");

        emptyLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLLS_EMPTY));
        emptyLabel.setWidth("100%");

        verticalSeparatorLabel.setWidth("1px");

        emptySelectionLabel.setValue(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLLS_SELECTION_EMPTY));
        emptySelectionLabel.setWidth("100%");

        statusLabel.setWidth(StyleDimensions.WIDTH_L);

        questionLabel.setContentMode(ContentMode.HTML);

        printButton.setCaption(SberbankUI.I18N.getString(SberbankKey.Form.MSGR_POLLS_PRINT));
        printButton.setStyleName(StyleNames.BUTTON_STANDARD);
        printButton.setWidth(StyleDimensions.WIDTH);

        // TODO temp
        printButton.setEnabled(false);
    }
}
