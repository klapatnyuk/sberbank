package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum MenuKey implements ResourceKey {

    MSGR,
    MSGR_IN,
    MSGR_OUT;

    @Override
    public String getKey() {
        return "menu." + ResourceKey.convert(this);
    }
}
