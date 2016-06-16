package ru.klapatnyuk.sberbank.web;

import ru.klapatnyuk.sberbank.web.i18n.ResourceKey;

/**
 * @author klapatnyuk
 */
public interface KeyConverter {

    static String convert(ResourceKey key) {
        return key.toString().toLowerCase().replace("_", ".");
    }
}
