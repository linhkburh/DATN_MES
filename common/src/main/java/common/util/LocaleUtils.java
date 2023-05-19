package common.util;

import java.util.Locale;

public class LocaleUtils {
private LocaleUtils() {

}

public final static String VI_LANGUAGE = "vi";
public final static String EN_LANGUAGE = "en";

public final static Locale VI_VN = new Locale(VI_LANGUAGE, "VN");
public final static Locale EN_US = new Locale(EN_LANGUAGE, "US");

public static Locale fullLocale(Locale locale) {
	if (VI_LANGUAGE.equalsIgnoreCase(locale.getLanguage()))
		return VI_VN;
	else if (EN_LANGUAGE.equalsIgnoreCase(locale.getLanguage()))
		return EN_US;

	return org.apache.commons.lang3.LocaleUtils.toLocale(locale
			.getLanguage());
}

public static boolean checkLanguage(final Locale locale,
		final String language) {
	if (language.equalsIgnoreCase(locale.getLanguage()))
		return true;

	return false;
}
}