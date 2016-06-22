package ru.klapatnyuk.sberbank.web.window;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.style.StyleDimensions;
import ru.klapatnyuk.sberbank.web.style.StyleNames;
import ru.klapatnyuk.sberbank.web.key.FormKey;

/**
 * @author klapatnyuk
 */
public class ConfirmWindow extends Window {

    private static final long serialVersionUID = -8997631146249313492L;

    private Button cancelButton;
    private Button okButton;
    private Label questionLabel = new Label();

    public ConfirmWindow(String title, String question) {
        super(title);

        center();
        setClosable(false);
        setModal(true);
        setDraggable(false);
        setResizable(false);
        setWidth(StyleDimensions.WINDOW_WIDTH_XL);

        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName(StyleNames.WINDOW_CONFIRM);

        initQuestionLabel(question);
        initCancelButton();
        initOkButton();

        layout.addComponent(newQuestionRow());
        layout.addComponent(newSubmitRow());

        setContent(layout);
    }

    public Button getOkButton() {
        return okButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    private void initOkButton() {
        okButton = new Button(SberbankUI.I18N.getString(FormKey.CONFIRM_OK));
        okButton.setWidth(StyleDimensions.WIDTH);
        okButton.addStyleName(StyleNames.BUTTON_STANDARD);
        okButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private void initQuestionLabel(String question) {
        questionLabel.setContentMode(ContentMode.HTML);
        questionLabel.setValue(question);
    }

    private void initCancelButton() {
        cancelButton = new Button(SberbankUI.I18N.getString(FormKey.CONFIRM_CANCEL));
        cancelButton.setWidth(StyleDimensions.WIDTH);
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    private HorizontalLayout newQuestionRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(StyleNames.LAYOUT_ROW);
        row.setSizeFull();
        row.addComponent(questionLabel);
        return row;
    }

    private HorizontalLayout newSubmitRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(StyleNames.LAYOUT_ROW);
        row.setSizeFull();
        row.addComponent(cancelButton);
        row.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
        row.setExpandRatio(cancelButton, 1);
        row.addComponent(okButton);
        row.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
        row.setExpandRatio(okButton, 1);
        return row;
    }
}
