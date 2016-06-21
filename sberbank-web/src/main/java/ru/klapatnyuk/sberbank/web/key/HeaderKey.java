package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum HeaderKey implements ResourceKey {

    APP,
    H2,
    FOOTER_PRODUCTION,
    FOOTER_COPYRIGHT,
    WINDOW_LOGIN,

    PTRN_MESSAGE,
    PTRN_POLL;

    @Override
    public String getKey() {
        return "head." + ResourceKey.convert(this);
    }
}
