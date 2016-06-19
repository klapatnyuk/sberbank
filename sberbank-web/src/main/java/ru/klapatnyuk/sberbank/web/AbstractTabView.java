package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.*;

/**
 * @author klapatnyuk
 */
public abstract class AbstractTabView extends HorizontalLayout {

    private static final long serialVersionUID = 2028146581661208756L;

    public abstract Button getCreateButton();

    public abstract Button getSubmitButton();

    public abstract AbstractOrderedLayout getEditEntityLayout();

    public abstract Label getEditSeparatorLabel();

    public abstract Label getEditLabel();

    public abstract SingleComponentContainer getEntityContainer();

    public abstract AbstractTextField getTitleField();

    protected void init() {
    }
}
