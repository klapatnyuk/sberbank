package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;
import ru.klapatnyuk.sberbank.web.constant.StyleDimensions;
import ru.klapatnyuk.sberbank.web.constant.StyleNames;

/**
 * TODO needs to be refactored
 *
 * @author klapatnyuk
 */
public class ButtonLogout extends Button {

    private static final long serialVersionUID = -5839614779261797299L;

    public ButtonLogout(String caption) {
        super(caption);

        setWidth("100px");
        setHeight(StyleDimensions.HEIGHT_S);
        addStyleName(StyleNames.BUTTON_STANDARD);
    }
}
