package ru.klapatnyuk.sberbank.web.key;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public enum FormKey implements ResourceKey {

    PROFILE_STATUS_LABEL,
    PROFILE_LOGOUT_BUTTON,

    LOGIN_BUTTON,
    LOGIN_LOGIN_LABEL,
    LOGIN_PASSWORD_LABEL,

    FIELD_TITLE_PROMPT,
    FIELD_LABEL_PROMPT,
    FIELD_TYPE_PROMPT,
    FIELD_TYPE_LINE,
    FIELD_TYPE_AREA,
    FIELD_TYPE_CHECKBOX,
    FIELD_UP,
    FIELD_DOWN,
    FIELD_REMOVE,

    ENTITY_TITLE_PROMPT,
    ENTITY_TITLE_LABEL,
    ENTITY_CREATE,
    ENTITY_CLEAR,
    ENTITY_CANCEL,
    ENTITY_REMOVE,
    ENTITY_SAVE,

    DOCUMENT_TEMPLATE_LABEL,
    DOCUMENT_EDIT_LABEL,
    DOCUMENT_CREATE_NEW_BUTTON,
    DOCUMENT_TEMPLATE_PROMPT,

    TEMPLATE_FIELDS_LABEL,
    TEMPLATE_CREATE_NEW_BUTTON,
    TEMPLATE_EDIT_LABEL;

    @Override
    public String getKey() {
        return "form." + ResourceKey.convert(this);
    }
}
