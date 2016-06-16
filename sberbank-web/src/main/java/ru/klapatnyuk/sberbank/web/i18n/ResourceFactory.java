package ru.klapatnyuk.sberbank.web.i18n;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author klapatnyuk
 */
public class ResourceFactory {

    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final char PATH_SEPARATOR_CHAR = '/';

    private static Map<String, ResourceProvider> providers = null;

    private static Locale globalLocale = null;

    /**
     * Returns a new or cached instance of {@link PackageResourceProvider} for the package to
     * which {@code neighbourClass} belongs. This method returns a provider even if
     * specified package does not contain a resource bundle.
     *
     * @param neighbourClass the class that specifies package to get provider for.
     * @return an instance of {@link PackageResourceProvider}
     * @throws IllegalArgumentException if {@code neighbourClass} is {@code null}
     */
    public static ResourceProvider getProvider(Class<?> neighbourClass) {
        ResourceProvider provider;
        if (neighbourClass == null) {
            throw new IllegalArgumentException();
        }
        String basePath = neighbourClass.getPackage().getName()
                .replace(PACKAGE_SEPARATOR_CHAR, PATH_SEPARATOR_CHAR) + PATH_SEPARATOR_CHAR;
        if (providers != null && providers.containsKey(basePath)) {
            provider = providers.get(basePath);
        } else {
            URL resourceURL = neighbourClass.getClassLoader()
                    .getResource(basePath + PackageResourceProvider.BUNDLE_NAME + ".properties");

            if (providers == null) {
                providers = new HashMap<>();
            }

            provider = new PackageResourceProvider(basePath, resourceURL != null);
            providers.put(basePath, provider);
        }
        return provider;
    }

    /**
     * Sets new global locale and forces existing provider(s), if any, to reload their
     * resource bundles. Providers created after invocation of {@code setGlobalLocale}
     * will be constructed using current global locale value.
     *
     * @param globalLocale new global locale or {@code null} to reset global locale to default.
     * @return previously set global locale or {@code null} if global locale wasn't set.
     */
    public static Locale setGlobalLocale(final Locale globalLocale) {
        Locale oldGlobalLocale = ResourceFactory.globalLocale;
        if (!Objects.equals(globalLocale, oldGlobalLocale)) {
            // force existing providers to reload their resource bundles upon next request
            if (providers != null) {
                providers.forEach((k, p) -> {
                    if (p != null) {
                        p.setLocale(globalLocale == null ? Locale.getDefault() : globalLocale);
                    }
                });
            }
            // switch global locale - future providers will be created using this locale
            ResourceFactory.globalLocale = globalLocale;
        }
        return oldGlobalLocale;
    }

    /**
     * @return current global {@link Locale}
     */
    public static Locale getGlobalLocale() {
        return globalLocale;
    }

    /* prevents instantiation */
    private ResourceFactory() {
    }

}
