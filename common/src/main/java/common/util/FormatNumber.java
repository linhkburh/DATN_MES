package common.util;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import sun.awt.image.PixelConverter.Bgrx;

public class FormatNumber {
	private static DecimalFormat frgGroupingChar, frgNoGroupingChar;
	private static DecimalFormat vnGroupingChar, vnNoGroupingChar;
	static {
		// Ngoai te 123,456,789.00
		frgGroupingChar = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		frgGroupingChar.setMinimumFractionDigits(0);
		frgGroupingChar.setMaximumFractionDigits(2);
		DecimalFormatSymbols symbols = frgGroupingChar.getDecimalFormatSymbols();
		// Khong set dau , se chet o cac moi truong khac nhau?
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		frgGroupingChar.setGroupingUsed(true);

		// Ngoai te 123456789.00
		frgNoGroupingChar = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		frgNoGroupingChar.setMinimumFractionDigits(0);
		frgNoGroupingChar.setMaximumFractionDigits(2);
		DecimalFormatSymbols symbolsx = frgNoGroupingChar.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator('');
		symbolsx.setDecimalSeparator('.');
		frgNoGroupingChar.setGroupingUsed(false);

		// tieng viet 123456789,00
		vnNoGroupingChar = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		vnNoGroupingChar.setMinimumFractionDigits(2);
		vnNoGroupingChar.setMaximumFractionDigits(2);
		vnNoGroupingChar.getDecimalFormatSymbols().setDecimalSeparator(',');
		vnNoGroupingChar.setGroupingUsed(false);

		// tieng viet 123.456.789,00
		vnGroupingChar = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		vnGroupingChar.setMinimumFractionDigits(0);
		vnGroupingChar.setMaximumFractionDigits(2);
		symbols = vnGroupingChar.getDecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		vnGroupingChar.setDecimalFormatSymbols(symbols);
		vnGroupingChar.setGroupingUsed(true);
	}

	public static String formatVnNoGroupingChar(BigDecimal num) {
		if (num == null)
			return "";
		return vnNoGroupingChar.format(num.doubleValue());
	}

	public static BigDecimal parseVnNoGroupingChar(String sNumber) throws ParseException {
		if (Formater.isNull(sNumber))
			return null;
		return BigDecimal.valueOf(vnNoGroupingChar.parse(sNumber).doubleValue());
	}

	public static String formatVnGroupingChar(BigDecimal num) {
		if (num == null)
			return "";
		return vnGroupingChar.format(num.doubleValue());
	}

	public static BigDecimal parseVnGroupingChar(String sNumber) throws ParseException {
		if (Formater.isNull(sNumber))
			return null;
		return BigDecimal.valueOf(vnGroupingChar.parse(sNumber).doubleValue());
	}

	public static String formatFrgGroupingChar(BigDecimal num) {
		if (num == null)
			return "";
		return frgGroupingChar.format(num.doubleValue());
	}

	public static String formatFrgNoGroupingChar(BigDecimal num) {
		if (num == null)
			return "";
		return frgNoGroupingChar.format(num.doubleValue());
	}

	public static BigDecimal parseFrgGroupingChar(String sNumber) throws ParseException {
		if (Formater.isNull(sNumber))
			return null;
		return BigDecimal.valueOf(frgGroupingChar.parse(sNumber).doubleValue());
	}

	public static String num2Str(BigDecimal num) {
		if (num == null)
			return "";
		return getFormat().format(num.doubleValue());
	}

	public static String num2Str(Long num) {
		if (num == null)
			return "";
		return getFormat().format(num);
	}

	public static String num2StrShort(Short num) {
		if (num == null)
			return "";
		return getFormat().format(num.shortValue());
	}

	public static BigDecimal str2num(String snum) throws ParseException {
		if (Formater.isNull(snum))
			return null;
		return BigDecimal.valueOf(getFormat().parse(snum).doubleValue());
	}

	public static Long str2Long(String snum) throws ParseException {
		if (Formater.isNull(snum))
			return null;
		return getFormat().parse(snum).longValue();
	}

	public static Short str2numShort(String snum) throws ParseException {
		if (Formater.isNull(snum))
			return null;
		return Short.valueOf(getFormat().parse(snum).shortValue());
	}

	private static DecimalFormat getFormat() {
		Locale locale = LocaleContextHolder.getLocale();
		if(!"vi".equals(locale.toLanguageTag()))
			return frgGroupingChar;
		else 
			return vnGroupingChar;
	}

	public static BigDecimal str2numOrNull(String value) {
		try {
			return str2num(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static String num2Str(Double num) {
		if (num == null)
			return "";
		return getFormat().format(num);
	}

}
