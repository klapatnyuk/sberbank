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
        STRINGS_LABEL_PROMPT,
        STRINGS_TYPE_PROMPT,
        STRINGS_TYPE_LINE,
        STRINGS_TYPE_AREA,
        STRINGS_TYPE_CHECKBOX,
        STRINGS_UP,
        STRINGS_DOWN,
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
        MESSAGE_PREVIOUS,
        MSGR_MASTERDATA,
        MSGR_MASTERDATA_FLOOR,
        MSGR_PORCH_CAPTION,
        MSGR_FLOOR_CAPTION,
        MSGR_FLOOR_CAPTION_FULL,

        MSGR_PATTERN,
        MSGR_PATTERN_FILTER_FOLDER,
        MSGR_PATTERN_FILTER_TAG,
        MSGR_PATTERN_EMPTY,
        MSGR_FOLDER_EMPTY,
        MSGR_FOLDER_NESTED,
        MSGR_TAG_PROMPT,

        MSGR_IN_WRITE,
        MSGR_IN_EMPTY,
        MSGR_IN_EMPTY_RECIPIENT,

        MSGR_OUT_SIMPLE,
        MSGR_OUT_ADVERT,
        MSGR_OUT_RECIPIENT,
        MSGR_OUT_PORCH,
        MSGR_OUT_PORCH_PROMPT,
        MSGR_OUT_FLOOR,
        MSGR_OUT_FLOOR_PROMPT,
        MSGR_OUT_FLAT,
        MSGR_OUT_FLAT_PROMPT,
        MSGR_OUT_STARTS,
        MSGR_OUT_EXPIRES,
        MSGR_OUT_BODY,
        MSGR_OUT_BODY_ADVERT,
        MSGR_OUT_BODY_PROMPT,
        MSGR_OUT_BODY_LIMIT,
        MSGR_OUT_INTERCOM,
        MSGR_OUT_SUBMIT,
        MSGR_OUT_SUBMIT_SAVE,
        // TODO needs to be renamed
        MSGR_OUT_PRINT_CONFIRM,
        MSGR_OUT_SUBJECT,
        MSGR_OUT_SUBJECT_PROMPT,
        // TODO needs to be renamed
        MSGR_OUT_SUBJECT_VALUE,
        MSGR_OUT_CANCEL,
        MSGR_OUT_OK,

        MSGR_POLLS_EMPTY,
        MSGR_POLLS_SELECTION_EMPTY,
        MSGR_POLLS_STATUS_ACTIVE,
        MSGR_POLLS_STATUS_STOPPED,
        MSGR_POLLS_STATUS_FINISHED,
        MSGR_POLLS_CHOICE,
        MSGR_POLLS_PRINT,

        MSGR_POLL_RECIPIENT,
        MSGR_POLL_RECIPIENT_GROUPING_FLOOR,
        MSGR_POLL_RECIPIENT_GROUPING_PORCH,
        MSGR_POLL_RECIPIENT_GROUPING_HOUSE,
        MSGR_POLL_RECIPIENT_REMOVE,
        MSGR_POLL_RECIPIENT_PROMPT,
        MSGR_POLL_START,
        MSGR_POLL_EXPIRATION,
        MSGR_POLL_CHOICE,
        MSGR_POLL_CHOICE_CUSTOM_PROMPT,
        MSGR_POLL_CUSTOM,
        MSGR_POLL_CUSTOM_LABEL,
        MSGR_POLL_FIELDS_LABEL,
        MSGR_POLL_MULTIPLY,
        MSGR_POLL_QUESTION,
        MSGR_POLL_QUESTION_PROMPT,
        MSGR_POLL_SUBMIT,

        MSGR_REQUESTS_EMPTY,
        MSGR_REQUESTS_RECIPIENT_EMPTY,

        MSGR_HISTORY_EMPTY,
        MSGR_HISTORY_SELECTION_EMPTY,
        MSGR_HISTORY_REMOVE,
        MSGR_HISTORY_PRINT,
        MSGR_HISTORY_SUBJECT,
        MSGR_HISTORY_BODY,

        PTRN_FOLDER,
        PTRN_FOLDER_EMPTY,
        PTRN_FOLDER_SET,
        PTRN_FOLDER_CREATE,
        PTRN_FOLDER_CREATE_PROMPT,
        PTRN_FOLDER_RENAME,
        PTRN_FOLDER_RENAME_PROMPT,
        PTRN_TAGS,

        PTRN_MESSAGE_CREATE,
        PTRN_MESSAGE_EDIT,
        PTRN_MESSAGE_SUBJECT,
        PTRN_MESSAGE_SUBJECT_PROMPT,
        PTRN_MESSAGE_BODY,
        PTRN_MESSAGE_ADD,
        PTRN_MESSAGE_SAVE,
        PTRN_MESSAGE_PROMPT,

        PTRN_POLL_CREATE,
        PTRN_POLL_EDIT,
        PTRN_POLL_ADD,
        PTRN_POLL_SAVE,
        PTRN_POLL_PROMPT,

        PTRN_STYLE_LIST,
        PTRN_STYLE_LIST_DESCRIPTION,
        PTRN_STYLE_TREE,
        PTRN_STYLE_TREE_DESCRIPTION,
        PTRN_STYLE_TAG,
        PTRN_STYLE_TAG_DESCRIPTION;

        @Override
        public String getKey() {
            return "form." + KeyConverter.convert(this);
        }
    }

    enum Menu implements ResourceKey {

        MSGR,

        MSGR_IN,
        MSGR_OUT,
        MSGR_POLLS,
        MSGR_POLL,
        MSGR_REQUESTS,
        MSGR_HISTORY,

        PTRN,
        PTRN_MESSAGE,
        PTRN_POLL,
        PTRN_STYLE;

        @Override
        public String getKey() {
            return "menu." + KeyConverter.convert(this);
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
        POLL_REPLY_SENT,
        MSGR_ATTRIBUTES_PORCHES_EMPTY,
        MSGR_ATTRIBUTES_FLOORS_EMPTY,
        MSGR_MASTERDATA_FLATS_EMPTY,

        MSGR_IN_CHECK_ERROR,

        MSGR_OUT_SIMPLE_SENT,
        MSGR_OUT_SIMPLE_ERROR,
        MSGR_OUT_ADVERT_SENT,
        MSGR_OUT_ADVERT_ERROR,
        MSGR_OUT_RECIPIENT_VALIDATE,
        MSGR_OUT_RECIPIENTS_VALIDATE,
        MSGR_OUT_STARTS_VALIDATE,
        MSGR_OUT_EXPIRES_VALIDATE,
        MSGR_OUT_PERIOD_EMPTY,
        MSGR_OUT_BODY_SIMPLE_VALIDATE,
        MSGR_OUT_BODY_ADVERT_VALIDATE,
        MSGR_OUT_BODY_VALIDATE,
        MSGR_OUT_RECIPIENT_INVALID,

        MSGR_POLLS_RECEIVE_SPECIFIC_ERROR,
        MSGR_POLLS_RECEIVE_ERROR,
        MSGR_POLLS_RECEIVE_RESULT_ERROR,

        MSGR_POLL_RECIPIENT_REQUIRED,
        MSGR_POLL_START_REQUIRED,
        MSGR_POLL_EXPIRATION_REQUIRED,
        MSGR_POLL_PERIOD_EMPTY,
        MSGR_POLL_BODY_REQUIRED,
        MSGR_POLL_CHOICES_REQUIRED,
        MSGR_POLL_CHOICES_DUPLICATES,
        MSGR_POLL_CUSTOM_REQUIRED,
        MSGR_POLL_CREATE_ERROR,
        MSGR_POLL_CREATED,

        MSGR_HISTORY_RECEIVE_ERROR,
        MSGR_HISTORY_UNMOUNT_ERROR,
        MSGR_HISTORY_UNMOUNTED,

        PTRN_FOLDER_DUPLICATE,
        PTRN_FOLDER_CREATE_VALIDATE,
        PTRN_FOLDER_CREATE_ERROR,
        PTRN_FOLDER_RENAME_ERROR,
        PTRN_FOLDER_CREATED,
        PTRN_FOLDER_RENAMED,
        PTRN_FOLDER_VALIDATE,

        PTRN_MESSAGE_CREATE_ERROR,
        PTRN_MESSAGE_EDIT_ERROR,
        PTRN_MESSAGE_UPDATED,
        PTRN_MESSAGE_CREATED,
        PTRN_MESSAGE_BODY_VALIDATE,

        PTRN_POLL_TEMPLATE_VALIDATE,
        PTRN_POLL_BODY_VALIDATE,
        PTRN_POLL_BODY_UNIQUE_VALIDATE,
        PTRN_POLL_CHOICES_REQUIRED,
        PTRN_POLL_CHOICES_DUPLICATES,
        PTRN_POLL_CUSTOM_REQUIRED,
        PTRN_POLL_CREATE_ERROR,
        PTRN_POLL_EDIT_ERROR,
        PTRN_POLL_UPDATED,
        PTRN_POLL_CREATED;

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
        REGISTRATION_WINDOW,

        MSGR_IN,
        MSGR_OUT_SIMPLE,
        MSGR_OUT_ADVERT,
        MSGR_OUT_WINDOW_PATTERN,
        // TODO needs to be renamed
        MSGR_OUT_WINDOW_PRINT,
        MSGR_POLLS,
        MSGR_POLL,
        MSGR_POLL_WINDOW_CONFIRM,
        MSGR_POLL_WINDOW_PATTERN,
        MSGR_HISTORY,

        MSGR_REQUESTS,

        PTRN_MESSAGE,
        PTRN_POLL,
        PTRN_STYLE,
        PTRN_WINDOW_FOLDER;

        @Override
        public String getKey() {
            return "head." + KeyConverter.convert(this);
        }
    }
}
