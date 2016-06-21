package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.ui.*;

/**
 * @author klapatnyuk
 */
public abstract class AbstractTabView extends HorizontalLayout {

    private static final long serialVersionUID = 2028146581661208756L;

    abstract AbstractOrderedLayout getTemplateLayout();

    abstract Button getCreateButton();

    abstract Button getSubmitButton();

    abstract Button getCancelButton();

    abstract Button getRemoveButton();

    abstract AbstractOrderedLayout getEditEntityLayout();

    abstract Label getEditSeparatorLabel();

    abstract Label getEditLabel();

    abstract SingleComponentContainer getEntityContainer();

    abstract AbstractTextField getTitleField();

    abstract void init();
}
