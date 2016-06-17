package ru.klapatnyuk.sberbank.web;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;

/**
 * @author klapatnyuk
 */
public interface UITemplate {

    ProfileLayout getProfileLayout();

    HorizontalLayout getFilterLayout();

    MenuBar getActionMenuBar();

    void setHeader(String header);

    void clickMenu(MenuTab tab);

    void clickActionMenu(MenuTab tab);

    AbstractTab getTab();

    AbstractTab getTab(MenuTab actionTab);

    AbstractTab getTab(MenuTab tab, MenuTab actionTab);
}
