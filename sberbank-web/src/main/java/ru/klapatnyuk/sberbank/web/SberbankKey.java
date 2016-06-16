package ru.klapatnyuk.sberbank.web;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public interface SberbankKey {

    enum Form implements ResourceKey {

        PROFILE_STATUS,
        PROFILE_LOGOUT,
        CONFIRM_OK,
        CONFIRM_CANCEL,
        LOGIN_BUTTON,
        LOGIN_LOGIN_LABEL,
        LOGIN_PASSWORD_LABEL,
        LOGIN_LAZY_WAIT,
        LOGOUT,
        REQUEST_STATUS_ACTIVE,
        REQUEST_STATUS_APPROVED,
        REQUEST_STATUS_INACTIVE,
        REQUEST_STATUS_CANCELLED,
        STRINGS_PROMPT,
        STRINGS_CLOSE,
        FILTER_GROUP,
        FILTER_LIST,
        PRINT_H1,

        POLL_FULL_CLICK,
        POLL_ANSWER_PROMPT,
        POLL_SUBMIT,
        MESSAGE_FULL_CLICK,
        MESSAGE_READ_CLICK,
        MESSAGE_NEXT,
        MESSAGE_PREVIOUS;

        @Override
        public String getKey() {
            return "form." + KeyConverter.convert(this);
        }
    }

    enum Notification implements ResourceKey {

        RPC_RESPONSE_NULL,
        RPC_RESPONSE_FIELD_NULL,
        RPC_RESPONSE_LIST_EMPTY,
        RPC_RESPONSE_LIST_SIZE,
        RPC_RESPONSE_LIST_NULL,

        CONFIG_RECEIVE_ERROR,
        CONCIERGE_RECEIVE_ERROR,
        FOLDER_RECEIVE_ERROR,
        PATTERN_MESSAGE_RECEIVE_ERROR,
        PATTERN_POLL_RECEIVE_ERROR,

        ENCODING_ERROR,

        ERROR_DESCRIPTION_TIME,
        ERROR_DESCRIPTION_BUSINESS,
        ERROR_DESCRIPTION_BUSINESS_TIME,

        GK_ACCESS_PORTAL_INCORRECT_REQUEST_DATA,
        GK_ACCESS_PORTAL_BRAND_CODE_NOT_SPECIFIED,
        GK_ACCESS_PORTAL_NO_BRAND_CODE_FOUND,
        GK_ACCESS_PORTAL_NO_LICENSE,
        GK_ACCESS_PORTAL_LICENSE_BRANDCODE_MISMATCH,
        GK_ACCESS_PORTAL_FF_NOT_SUPPORTED,
        GK_ACCESS_PORTAL_NO_REGISTERED_FF,
        GK_ACCESS_PORTAL_INCORRECT_CREDENTIALS,
        GK_ACCESS_PORTAL_LICENSE_EXPIRED,

        GK_LICENSING_FORCED_LIMIT_MISMATCH,
        GK_LICENSING_DUPLICATED_FLAT_FRONTEND,

        VERSION_ERROR,
        STRUCTURE_ERROR,

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

        LAYOUT_ERROR,

        LOGIN_ERROR,
        LOGIN_CONNECT_ERROR,
        LOGIN_INVALID,
        LOGIN_LOGIN_INVALID,
        LOGIN_PASSWORD_INVALID,
        LOGIN_LOGIN_PASSWORD_INVALID,
        LOGIN_CAPTCHA_INVALID,

        EVENT_ERROR,

        SAVE_ERROR,
        SAVE_VALIDATION_ERROR,
        SAVE_FONT_ERROR,

        MESSAGE_RECEIVE_ERROR,
        MESSAGE_FULL_RECEIVE_ERROR,
        MESSAGE_ACKNOWLEDGE_ERROR,

        REQUEST_RECEIVE_ERROR,

        POLL_REPLY_ERROR,
        POLL_REPLY_SENT;

        @Override
        public String getKey() {
            return "noti." + KeyConverter.convert(this);
        }
    }

    enum Header implements ResourceKey {

        APP,
        APP_CODE,
        H2,
        H3,
        BROWNIE_CODE,
        GATEKEEPER_CODE,
        VERSION,
        FOOTER,
        FOOTER_PRODUCTION,
        FOOTER_COPYRIGHT,

        WINDOW_LOGIN,
        WINDOW_LOGIN_LAZY,
        WINDOW_LOGOUT,
        REGISTRATION_WINDOW;

        @Override
        public String getKey() {
            return "head." + KeyConverter.convert(this);
        }
    }
}
