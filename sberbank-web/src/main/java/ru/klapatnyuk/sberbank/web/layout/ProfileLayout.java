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

    private Label statusLabel = new Label(SberbankUI.I18N.getString(SberbankKey.Form.PROFILE_STATUS));
    private Label nameLabel = new Label("");
    private ButtonLogout logoutButton = new ButtonLogout(SberbankUI.I18N.getString(SberbankKey.Form.PROFILE_LOGOUT));

    public ProfileLayout() {
        addStyleName(StyleNames.BAR_PROFILE);

        addComponent(statusLabel);
        addComponent(nameLabel);
        addComponent(logoutButton);

        setComponentAlignment(statusLabel, Alignment.MIDDLE_CENTER);
        setComponentAlignment(nameLabel, Alignment.MIDDLE_CENTER);
        setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);

        statusLabel.addStyleName(StyleNames.SPACER_RIGHT);
        nameLabel.addStyleName(StyleNames.SPACER_RIGHT);
        nameLabel.addStyleName(StyleNames.BAR_PROFILE_USER_NAME);
    }

    public ButtonLogout getLogoutButton() {
        return logoutButton;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label label) {
        this.nameLabel = label;
    }
}
