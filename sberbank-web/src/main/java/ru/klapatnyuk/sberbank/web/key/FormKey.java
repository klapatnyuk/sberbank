package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum FormKey implements ResourceKey {

    PROFILE_STATUS,
    PROFILE_LOGOUT,

    LOGIN_BUTTON,
    LOGIN_LOGIN_LABEL,
    LOGIN_PASSWORD_LABEL,

    STRINGS_PROMPT,
    STRINGS_LABEL_PROMPT,
    STRINGS_TYPE_PROMPT,
    STRINGS_TYPE_LINE,
    STRINGS_TYPE_AREA,
    STRINGS_TYPE_CHECKBOX,
    STRINGS_UP,
    STRINGS_DOWN,
    STRINGS_CLOSE,

    MSGR_POLL_CHOICE_CUSTOM_PROMPT,
    MSGR_POLL_CUSTOM_LABEL,
    MSGR_POLL_FIELDS_LABEL,
    MSGR_POLL_QUESTION,

    PTRN_MESSAGE_CREATE,
    PTRN_MESSAGE_EDIT,

    PTRN_POLL_CREATE,
    PTRN_POLL_EDIT,
    PTRN_POLL_ADD,
    PTRN_POLL_CLEAR,
    PTRN_POLL_CANCEL,
    PTRN_POLL_REMOVE,
    PTRN_POLL_SAVE,
    PTRN_POLL_PROMPT;

    @Override
    public String getKey() {
        return "form." + ResourceKey.convert(this);
    }
}
