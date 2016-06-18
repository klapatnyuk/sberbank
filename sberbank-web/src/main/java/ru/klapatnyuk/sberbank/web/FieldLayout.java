package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.HorizontalLayout;

/**
 * @author klapatnyuk
 */
public class FieldLayout extends HorizontalLayout {

    private final Integer id;

    public FieldLayout(Integer id) {
        this.id = id;
    }

    public Integer getFieldId() {
        return id;
    }
}
