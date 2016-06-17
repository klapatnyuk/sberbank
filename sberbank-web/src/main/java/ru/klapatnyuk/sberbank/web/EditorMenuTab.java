package ru.klapatnyuk.sberbank.web;

/**
 * @author klapatnyuk
 */
public enum EditorMenuTab implements MenuTab {

    DOCUMENT(0),
    TEMPLATE(1);

    private int index;

    EditorMenuTab(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public MenuTab getDefaultSub() {
        return DOCUMENT;
    }

    @Override
    public MenuTab get(int index) {
        for (MenuTab tab : EditorMenuTab.values()) {
            if (tab.getIndex() == index) {
                return tab;
            }
        }
        return null;
    }
}
