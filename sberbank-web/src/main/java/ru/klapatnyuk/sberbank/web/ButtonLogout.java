package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;

/**
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
