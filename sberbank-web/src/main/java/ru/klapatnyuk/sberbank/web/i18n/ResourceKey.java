package ru.klapatnyuk.sberbank.web.i18n;

/**
 * @author klapatnyuk
 */
public interface ResourceKey {

    static String convert(ResourceKey key) {
        return key.toString().toLowerCase().replace("_", ".");
    }

    /**
     * @return a textual resource key
     */
    String getKey();
}
