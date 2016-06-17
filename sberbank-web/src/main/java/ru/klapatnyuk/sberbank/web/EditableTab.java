package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Component;

/**
 * @author klapatnyuk
 */
public interface EditableTab {

    void clear();

    boolean validate();

    Component getValidationSource();
}
