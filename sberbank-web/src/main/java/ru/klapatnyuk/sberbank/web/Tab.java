package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.Component;

/**
 * @author klapatnyuk
 */
public interface Tab {

    String getHeader();

    boolean isUpdated();

    void update();

    void poll();

    MenuTab getTab();

    MenuTab getActionTab();

    void clear();

    boolean validate();

    Component getValidationSource();
}
