package frwk.editors.spring;

import java.beans.PropertyEditorSupport;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import common.util.Formater;

@Component(value = "dateEditor")
public class DateEditor extends PropertyEditorSupport {
	private static Logger lg = Logger.getLogger(DateEditor.class);

	@Override
	public String getAsText() {
		Date date = (Date) getValue();
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0)
			return Formater.date2str(date);
		else
			return Formater.date2strDateTime(date);
	}

	/**
	 * Sets the property value by parsing a given String. May raise java.lang.IllegalArgumentException if either the
	 * String is badly formatted or if this kind of property can't be expressed as text.
	 *
	 * @param text The string to be parsed.
	 */
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		try {
			// "dd/mm/yyyy".length()
			if (text != null && text.trim().length() > 10)
				setValue(Formater.str2DateTime(text));
			else
				setValue(Formater.str2date(text));
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
}
