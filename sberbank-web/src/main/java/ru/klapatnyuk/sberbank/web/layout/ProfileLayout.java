package ru.klapatnyuk.sberbank.web.layout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.style.StyleDimensions;
import ru.klapatnyuk.sberbank.web.style.StyleNames;
import ru.klapatnyuk.sberbank.web.key.FormKey;

/**
 * @author klapatnyuk
 */
public class ProfileLayout extends HorizontalLayout {

    private static final long serialVersionUID = 6933490056199145771L;

    private Label nameLabel = new Label("");
    private Button logoutButton = new Button(SberbankUI.I18N.getString(FormKey.PROFILE_LOGOUT_BUTTON));

    public ProfileLayout() {
        addStyleName(StyleNames.BAR_PROFILE);

        Label status = new Label(SberbankUI.I18N.getString(FormKey.PROFILE_STATUS_LABEL));
        addComponent(status);

        addComponent(nameLabel);

        logoutButton.setWidth("100px");
        logoutButton.setHeight(StyleDimensions.HEIGHT_S);
        logoutButton.addStyleName(StyleNames.BUTTON_STANDARD);
        addComponent(logoutButton);

        setComponentAlignment(status, Alignment.MIDDLE_CENTER);
        setComponentAlignment(nameLabel, Alignment.MIDDLE_CENTER);
        setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);

        status.addStyleName(StyleNames.SPACER_RIGHT);
        nameLabel.addStyleName(StyleNames.SPACER_RIGHT);
        nameLabel.addStyleName(StyleNames.BAR_PROFILE_USER_NAME);
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public Label getNameLabel() {
        return nameLabel;
    }
}
