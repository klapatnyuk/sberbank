package ru.klapatnyuk.sberbank.web.layout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import ru.klapatnyuk.sberbank.web.ButtonLogout;
import ru.klapatnyuk.sberbank.web.SberbankKey;
import ru.klapatnyuk.sberbank.web.SberbankUI;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;

/**
 * @author klapatnyuk
 */
public class ProfileLayout extends HorizontalLayout {

    private static final long serialVersionUID = 6933490056199145771L;

    private Label nameLabel = new Label("");
    private ButtonLogout logoutButton = new ButtonLogout(SberbankUI.I18N.getString(SberbankKey.FormKey.PROFILE_LOGOUT));

    public ProfileLayout() {
        addStyleName(StyleNames.BAR_PROFILE);

        Label status = new Label(SberbankUI.I18N.getString(SberbankKey.FormKey.PROFILE_STATUS));
        addComponent(status);
        addComponent(nameLabel);
        addComponent(logoutButton);

        setComponentAlignment(status, Alignment.MIDDLE_CENTER);
        setComponentAlignment(nameLabel, Alignment.MIDDLE_CENTER);
        setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);

        status.addStyleName(StyleNames.SPACER_RIGHT);
        nameLabel.addStyleName(StyleNames.SPACER_RIGHT);
        nameLabel.addStyleName(StyleNames.BAR_PROFILE_USER_NAME);
    }

    public ButtonLogout getLogoutButton() {
        return logoutButton;
    }

    public Label getNameLabel() {
        return nameLabel;
    }
}
