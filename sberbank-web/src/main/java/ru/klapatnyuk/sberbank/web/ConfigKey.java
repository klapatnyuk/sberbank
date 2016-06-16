package ru.klapatnyuk.sberbank.web;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum ConfigKey implements ResourceKey {

    CAPTCHA_BROWNIE_KEY,
    CAPTCHA_BROWNIE_SECRET,
    CAPTCHA_GATEKEEPER_KEY,
    CAPTCHA_GATEKEEPER_SECRET,

    DATE_FORMAT_TASK_ANSWER,
    DATE_FORMAT_WARNING,
    DATE_FORMAT_RECEIVABLE,
    DATE_FORMAT_POLL,
    DATE_FORMAT_ADVERT,
    DATE_FORMAT_TASK,
    DATE_FORMAT_TASK_LONG,
    DATE_FORMAT_LICENSE,

    DATE_FORMAT_SHORT,

    ERROR_POSTFIX_CONNECT,
    ERROR_POSTFIX_LOGIN_INVALID,

    TIME_INACTIVE_INTERVAL,
    TIME_POLL_INTERVAL,
    TIME_LOGIN_INTERVAL,
    TIME_SCREEN_INTERVAL,

    LOGIN_ATTEMPT_LIMIT,

    MESSAGE_RECEIVE_LIMIT,
    MESSAGE_SAVE_LIMIT,
    MESSAGE_DESCRIPTION_LENGTH,
    MESSAGE_SUBJECT_LENGTH,
    MESSAGE_INTERCOM_LENGTH,
    MESSAGE_INTERCOM_CYRILLIC,

    PANEL_SUBJECT_LENGTH,

    PATTERN_SUBJECT_LENGTH,

    RECEIVABLE_DESCRIPTION_LENGTH,

    WARNING_LIMIT,

    URL_HOUSING,
    URL_BROWNIE,
    URL_FLAT,
    ;

    @Override
    public String getKey() {
        return KeyConverter.convert(this);
    }
}
