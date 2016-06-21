package ru.klapatnyuk.sberbank.web.menu;

/**
 * @author klapatnyuk
 */
public enum SberbankMenuTab implements MenuTab {

    EDITOR(0, EditorMenuTab.DOCUMENT);

    private int index;
    private MenuTab defaultSub;

    SberbankMenuTab(int index, MenuTab defaultSub) {
        this.index = index;
        this.defaultSub = defaultSub;
    }

    @Override
    public MenuTab getDefaultSub() {
        return defaultSub;
    }

    @Override
    public MenuTab get(int index) {
        for (MenuTab tab : SberbankMenuTab.values()) {
            if (tab.getIndex() == index) {
                return tab;
            }
        }
        return null;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
