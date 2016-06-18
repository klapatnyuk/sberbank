package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.HorizontalLayout;

/**
 * @author klapatnyuk
 */
public class FieldLayout extends HorizontalLayout {

    private final int id;

    public FieldLayout(int id) {
        this.id = id;
    }

    public int getFieldId() {
        return id;
    }
}
