package ru.klapatnyuk.sberbank.web.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author klapatnyuk
 */
public class PackageResourceProvider implements ResourceProvider {

    public static final String BUNDLE_NAME = "strings";

    private final String path;

    private final boolean hasBundle;

    private ResourceBundle bundle;

    private Locale locale;

    /**
     * Package-private ctor - factory use only.
     * @see ResourceFactory#getProvider(Class)
     */
    PackageResourceProvider(String path, boolean hasBundle) {
        this.path = path;
        this.hasBundle = hasBundle;
        this.locale = ResourceFactory.getGlobalLocale() == null ?
                Locale.getDefault() : ResourceFactory.getGlobalLocale();
    }

    /**
     * @return {@code true} if this provider has a resource bundle, otherwise {@code false}.
     */
    public boolean hasBundle() {
        return hasBundle;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public URL getResource(String name) {
        return ClassLoader.getSystemClassLoader().getResource(path + name);
    }

    @Override
    public Enumeration<URL> getResources(String name) {
        Enumeration<URL> resources;
        try {
            resources = ClassLoader.getSystemClassLoader().getResources(path + name);
        } catch (IOException e) {
            resources = Collections.emptyEnumeration();
        }
        return resources;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(path + name);
    }

    @Override
    public Locale setLocale(Locale locale) {
        Locale oldLocale = this.locale;
        if (!Objects.equals(locale, oldLocale)) {
            this.locale = locale == null ? Locale.getDefault() : locale;

            // force reload bundle when locale changes
            bundle = null;
        }
        return oldLocale;
    }

    @Override
    public String getString(ResourceKey key, Object... args) {
        return getString(key.getKey(), args);
    }

    @Override
    public String getString(String key, Object... args) {
        return getStringOrElse(key, null, args);
    }

    @Override
    public String getStringOrElse(ResourceKey key, String fallback, Object... args) {
        return getStringOrElse(key.getKey(), fallback, args);
    }

    @Override
    public String getStringOrElse(String key, String fallback, Object... args) {
        String pattern = getStringInternal(key, fallback);
        if (args != null && args.length > 0) {
            MessageFormat formatter = new MessageFormat(pattern, getLocale());
            try {
                return formatter.format(args, new StringBuffer(), null).toString();
            } catch (IllegalArgumentException e) {
                // ignore formatter errors
            }
        }
        return pattern;
    }

    private String getStringInternal(String key, String fallback) {
        String result = null;
        if (key != null && !key.isEmpty() && !key.trim().isEmpty()) {
            key = key.trim();
            if (getBundle() != null) {
                try {
                    result = bundle.getString(key);
                } catch (MissingResourceException e) {
                    // ok - will return global key instead of actual resource
                }
            }
            if (result == null) {
                result = fallback == null ? buildGlobalKey(key) : fallback;
            }
        } else {
            throw new IllegalArgumentException("Resource key cannot be empty");
        }
        return result;
    }

    private ResourceBundle getBundle() {
        if (hasBundle() && bundle == null) {
            bundle = ResourceBundle.getBundle(path + BUNDLE_NAME, locale);
        }
        return bundle;
    }

    private String buildGlobalKey(String key) {
        return "#" + path + BUNDLE_NAME + ":" + key + '#';
    }

}