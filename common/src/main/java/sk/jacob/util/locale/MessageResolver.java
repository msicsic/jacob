package sk.jacob.util.locale;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class MessageResolver {
    private static final Map<String, ResourceBundle> RB_CACHE = new ConcurrentHashMap<>();

    public static String getMessage(String messageCode, Locale locale) {
        String bundleName = messageCode.substring(0, messageCode.indexOf("."));
        return getBundle(bundleName, locale).getString(messageCode);
    }

    private static ResourceBundle getBundle(String bundleName, Locale locale) {
        String bundleCode = bundleName.concat(locale.toString());
        if(RB_CACHE.containsKey(bundleCode) == false) {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
            RB_CACHE.put(bundleCode, bundle);
        }
        return RB_CACHE.get(bundleCode);
    }
}
