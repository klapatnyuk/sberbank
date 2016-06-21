package ru.klapatnyuk.sberbank.web.menu;

/**
 * @author klapatnyuk
 */
public interface MenuTab {

    int getIndex();

    MenuTab get(int index);

    MenuTab getDefaultSub();
}
