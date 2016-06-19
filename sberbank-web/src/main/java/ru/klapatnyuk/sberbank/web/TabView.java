package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author klapatnyuk
 */
public abstract class TabView extends HorizontalLayout {

    private static final long serialVersionUID = 2028146581661208756L;

    public abstract Button getCreateButton();

    public abstract Button getSubmitButton();

    protected void init() {
    }
}
