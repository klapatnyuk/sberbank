package ru.klapatnyuk.sberbank.web.tab;

import com.vaadin.ui.Component;
import ru.klapatnyuk.sberbank.web.menu.MenuTab;

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
