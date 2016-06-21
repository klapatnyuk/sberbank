package ru.klapatnyuk.sberbank.web.constant;

/**
 * @author klapatnyuk
 */
public interface ValidatePattern {

    int LOGIN_LENGTH = 255;
    int PASSWORD_LENGTH = 255;
    int SUBJECT_LENGTH = 255;

    int FLAT_NUMBER_LENGTH = 4;
    String FLAT_NUMBER = "^(?:0*[1-9][0-9]*)$";

    String USAGE_LIMIT = "^[0-9]+$";
    String DURATION_MONTH = "^(?:0*[1-9][0-9]*)$";
    String INTEGER = "^-?[0-9]+$";
    String FLOAT = "^-?[0-9]+[\\.\\,][0-9]*$";

    int ACODE_FRAGMENT_COUNT = 4;
    int ACODE_FRAGMENT_LENGTH = 4;
    int ACODE_LENGTH = ACODE_FRAGMENT_COUNT * ACODE_FRAGMENT_LENGTH + (ACODE_FRAGMENT_COUNT - 1);
    String ACODE_FRAGMENT = "([A-Z0-9]{" + ACODE_FRAGMENT_LENGTH + "})";
    String ACODE_DELIMITER = "-";
    String ACTIVATION_CODE = "^" + ACODE_FRAGMENT + ACODE_DELIMITER + ACODE_FRAGMENT + ACODE_DELIMITER + ACODE_FRAGMENT + ACODE_DELIMITER
            + ACODE_FRAGMENT + "$";

    int PATTERN_FOLDER_LENGTH = 255;

    int TAGLIKE_STRINGS_LENGTH = 255;
}
