package ru.klapatnyuk.sberbank.web.window;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import ru.klapatnyuk.sberbank.web.SberbankKey;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.constant.StyleDimensions;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;
import ru.klapatnyuk.sberbank.web.constant.ValidatePattern;

/**
 * @author klapatnyuk
 */
public class LoginWindow extends AbstractCenteredWindow {

    private static final long serialVersionUID = 8550951239209722669L;

    private TextField loginTextField;
    private PasswordField passwordField;
    private Button submitButton;

    public LoginWindow(String title) {
        super(title);
    }

    public TextField getLoginTextField() {
        return loginTextField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    @Override
    public void close() {
        super.close();
        loginTextField.clear();
        passwordField.clear();
    }

    @Override
    protected void init() {
        super.init();

        setModal(false);
        setStyleName(StyleNames.WINDOW_LOGIN);
        setWidth(StyleDimensions.WINDOW_WIDTH);
    }

    @Override
    protected void initContent() {
        initLoginTextField();
        initPasswordField();
        initSubmitButton();

        VerticalLayout layout = new VerticalLayout();
        layout.setStyleName(StyleNames.WINDOW_LOGIN_LAYOUT);
        layout.addComponent(newLoginRow());
        layout.addComponent(newPasswordRow());
        layout.addComponent(newSubmitRow());

        setContent(layout);
    }

    private void initLoginTextField() {
        loginTextField = new TextField();
        loginTextField.setWidth("200px");
        loginTextField.setValidationVisible(false);
        final Validator validator =
                new StringLengthValidator(SberbankUI.I18N.getString(SberbankKey.NotificationKey.LOGIN_LOGIN_INVALID,
                        SberbankUI.I18N.getString(SberbankKey.FormKey.LOGIN_LOGIN_LABEL)), 1,
                        ValidatePattern.LOGIN_LENGTH, false);
        loginTextField.addValidator(validator);
    }

    private void initPasswordField() {
        passwordField = new PasswordField();
        passwordField.setWidth("200px");
        passwordField.setValidationVisible(false);
        final Validator validator =
                new StringLengthValidator(SberbankUI.I18N.getString(SberbankKey.NotificationKey.LOGIN_PASSWORD_INVALID,
                        SberbankUI.I18N.getString(SberbankKey.FormKey.LOGIN_PASSWORD_LABEL)), 1,
                        ValidatePattern.PASSWORD_LENGTH, false);
        passwordField.addValidator(validator);
    }

    private void initSubmitButton() {
        submitButton = new Button(SberbankUI.I18N.getString(SberbankKey.FormKey.LOGIN_BUTTON));
        submitButton.setWidth(StyleDimensions.WIDTH);
        submitButton.addStyleName(StyleNames.BUTTON_STANDARD);
        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private HorizontalLayout newLoginRow() {
        return newFormRow(SberbankUI.I18N.getString(SberbankKey.FormKey.LOGIN_LOGIN_LABEL), loginTextField);
    }

    private HorizontalLayout newPasswordRow() {
        return newFormRow(SberbankUI.I18N.getString(SberbankKey.FormKey.LOGIN_PASSWORD_LABEL), passwordField);
    }

    private HorizontalLayout newSubmitRow() {
        return newSingleRow(submitButton);
    }

    private HorizontalLayout newFormRow(String caption, AbstractTextField field) {
        Label label = new Label(caption);
        label.setWidth("100px");

        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(StyleNames.LAYOUT_ROW);
        row.setSizeFull();
        row.addComponent(label);
        row.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        row.setExpandRatio(label, 1);
        row.addComponent(field);
        row.setExpandRatio(field, 2);
        return row;
    }

    private HorizontalLayout newSingleRow(Component component) {
        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(StyleNames.LAYOUT_ROW);
        row.setSizeFull();
        row.addComponent(component);
        row.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
        row.setExpandRatio(component, 1);
        return row;
    }
}