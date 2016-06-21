package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum MenuKey implements ResourceKey {

    EDITOR,
    EDITOR_DOCUMENT,
    EDITOR_TEMPLATE;

    @Override
    public String getKey() {
        return "menu." + ResourceKey.convert(this);
    }
}
