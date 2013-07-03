package sk.jacob.util.locale;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageResolver {
    public static String getMessage(String messageCode, Locale locale) {
        String bundleName = messageCode.substring(0, messageCode.indexOf("."));
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        return bundle.getString(messageCode);
    }
}
