package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.ui.Component;

/**
 * @author klapatnyuk
 */
public interface Tab {

    String getHeader();

    void update();

    void poll();

    void clear();

    boolean validate();

    Component getValidationSource();
}
