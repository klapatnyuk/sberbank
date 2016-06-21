package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum ConfigKey implements ResourceKey {

    TIME_INACTIVE_INTERVAL,
    TIME_POLL_INTERVAL,
    PATTERN_SUBJECT_LENGTH,
    WARNING_LIMIT;

    @Override
    public String getKey() {
        return ResourceKey.convert(this);
    }
}
