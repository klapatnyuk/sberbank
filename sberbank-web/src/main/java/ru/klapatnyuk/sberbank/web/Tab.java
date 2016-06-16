package ru.klapatnyuk.sberbank.web;

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
}