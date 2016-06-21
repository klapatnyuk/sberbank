package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum HeaderKey implements ResourceKey {

    APPLICATION,
    H2,
    COPYRIGHT,
    WINDOW_LOGIN,
    WINDOW_CONFIRM_REMOVE,
    TEMPLATE,
    DOCUMENT;

    @Override
    public String getKey() {
        return "head." + ResourceKey.convert(this);
    }
}
