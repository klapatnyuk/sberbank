package ru.klapatnyuk.sberbank.web.i18n;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;

/**
 * @author klapatnyuk
 */
public interface ResourceProvider {

    /**
     * @return a {@link Locale} associated with current provider.
     */
    Locale getLocale();

    /**
     * Changes a locale for the current provider.
     *
     * @param locale new {@link Locale} or {@code null} to set default locale.
     * @return the previously set locale
     */
    Locale setLocale(Locale locale);

    /**
     * Finds a resource with a given name.
     *
     * @param name the resource name.
     * @return an {@link URL} that represents a resource, or {@code null} if resource could not be found.
     */
    URL getResource(String name);

    /**
     * Finds all the resources with the given name within current provider's context.
     *
     * @param name the resource name
     * @return an {@link Enumeration} containing resource URLs or empty enumeration
     * if no resources with the specified name were found.
     */
    Enumeration<URL> getResources(String name);

    /**
     * Returns an input stream for reading a specified resource.
     *
     * @param name the resource name
     * @return an {@link InputStream} for reading the resource, or null if the resource could not be found.
     */
    InputStream getResourceAsStream(String name);

    /**
     * @param key  a resource key
     * @param args parameter values
     * @return a formatted string associated with given key
     * or <i>global key</i> if specified string does not exists.
     */
    String getString(ResourceKey key, Object... args);

    /**
     * @param key  a resource key (textual)
     * @param args parameter values
     * @return a formatted string associated with given key
     * or <i>global key</i> if specified string does not exists.
     */
    String getString(String key, Object... args);

    /**
     * @param key      a resource key
     * @param fallback fallback value
     * @param args     parameter values
     * @return a formatted string associated with the given key
     * or {@code fallback} if specified key does not exists.
     */
    String getStringOrElse(ResourceKey key, String fallback, Object... args);

    /**
     * @param key      a resource key (textual)
     * @param fallback fallback value
     * @param args     parameter values
     * @return a formatted string associated with the given key
     * or {@code fallback} if specified key does not exists.
     */
    String getStringOrElse(String key, String fallback, Object... args);
}
