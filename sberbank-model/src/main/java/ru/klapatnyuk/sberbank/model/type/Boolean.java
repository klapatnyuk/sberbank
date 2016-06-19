package ru.klapatnyuk.sberbank.model.type;

import ru.klapatnyuk.sberbank.model.type.api.EnumType;

/**
 * @author klapatnyuk
 */
public enum Boolean implements EnumType {
    Y(true),
    N(false);

    private boolean value;

    Boolean(boolean value) {
        this.value = value;
    }

    public static boolean find(String source) {
        if (source == null || source.isEmpty()) {
            return getDefault();
        }
        for (Boolean constant : values()) {
            if (constant.toString().toLowerCase().equals(source.toLowerCase())) {
                return constant.value;
            }
        }
        return getDefault();
    }

    public static boolean getDefault() {
        return false;
    }
}
