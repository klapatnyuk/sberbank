package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum ConfigKey implements ResourceKey {

    CONNECT_URI,
    CONNECT_USERNAME,
    CONNECT_PASSWORD,
    CONNECT_INITIAL,
    CONNECT_MAX,

    TIME_INACTIVE_INTERVAL,
    TIME_POLL_INTERVAL,
    WARNING_LIMIT;

    @Override
    public String getKey() {
        return ResourceKey.convert(this);
    }
}
