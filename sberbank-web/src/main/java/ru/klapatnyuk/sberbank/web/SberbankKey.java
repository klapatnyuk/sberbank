package ru.klapatnyuk.sberbank.web;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public interface SberbankKey {

    /**
     * @author klapatnyuk
     */
    enum FormKey implements ResourceKey {

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
        FILTER_LIST,

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

    /**
     * @author klapatnyuk
     */
    enum MenuKey implements ResourceKey {

        MSGR,
        MSGR_IN,
        MSGR_OUT;

        @Override
        public String getKey() {
            return "menu." + ResourceKey.convert(this);
        }
    }

    /**
     * @author klapatnyuk
     */
    enum NotificationKey implements ResourceKey {

        ERROR_DESCRIPTION_TIME,
        ERROR_DESCRIPTION_BUSINESS,
        ERROR_DESCRIPTION_BUSINESS_TIME,

        SESSION_EXPIRED_CAPTION,
        SESSION_EXPIRED,
        COMMUNICATION_PROBLEM_CAPTION,
        COMMUNICATION_PROBLEM,
        AUTHENTICATION_PROBLEM_CAPTION,
        AUTHENTICATION_PROBLEM,
        INTERNAL_ERROR_CAPTION,
        INTERNAL_ERROR,
        COOKIES_DISABLED_CAPTION,
        COOKIES_DISABLED,

        LOGIN_ERROR,
        LOGIN_LOGIN_INVALID,
        LOGIN_PASSWORD_INVALID,
        LOGIN_LOGIN_PASSWORD_INVALID,

        PTRN_POLL_TEMPLATE_VALIDATE,
        PTRN_POLL_BODY_VALIDATE,
        PTRN_POLL_BODY_UNIQUE_VALIDATE,
        PTRN_POLL_CHOICES_REQUIRED,
        PTRN_POLL_CHOICES_DUPLICATES,
        PTRN_POLL_CUSTOM_REQUIRED;

        @Override
        public String getKey() {
            return "noti." + ResourceKey.convert(this);
        }
    }

    /**
     * @author klapatnyuk
     */
    enum HeaderKey implements ResourceKey {

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
}
