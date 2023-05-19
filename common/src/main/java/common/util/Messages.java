package common.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Messages {public static final String BUNDLE_MESSAGE_NAME = "messages";

private static final Logger logger = Logger.getLogger(Messages.class);

private final ResourceBundle resourceBundle;

private Messages(String baseName, Locale locale) {
	resourceBundle = ResourceBundle.getBundle(baseName, locale);
}

public static Messages buildMessages(String baseName, Locale locale) {
	return new Messages(baseName, locale);
}

public boolean container(final String key) {
	return resourceBundle.containsKey(key);
}

public String get(final String key, final Object... arguments) {
	if (key == null || key.isEmpty())
		return StringUtils.EMPTY;

	String keyTrim = key.trim();
	try {
		String value = resourceBundle.getString(keyTrim).trim();
		return arguments == null || arguments.length == 0 ? value
				: MessageFormat.format(value, arguments);
	} catch (MissingResourceException mre) {
		logger.error("Message key not found: " + keyTrim);
		return '!' + keyTrim + '!';
	}
}
}
