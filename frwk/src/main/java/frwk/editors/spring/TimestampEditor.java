package frwk.editors.spring;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import common.util.Formater;

@Component(value = "timestampEditor")
public class TimestampEditor extends PropertyEditorSupport {
	private static Logger lg = Logger.getLogger(TimestampEditor.class);

	@Override
	public String getAsText() {
		TemporalType timeStamp = (TemporalType) getValue();
		if (timeStamp == TemporalType.TIMESTAMP)
			return Formater.date2strDateTime((Date) getValue());
		return "";
	}

	/**
	 * Sets the property value by parsing a given String. May raise java.lang.IllegalArgumentException if either the
	 * String is badly formatted or if this kind of property can't be expressed as text.
	 *
	 * @param text The string to be parsed.
	 */
	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		try {
			Date d = Formater.str2DateTime(text);
			setValue(d);
		} catch (Exception e) {
			setValue(text);
			lg.error(e.getMessage(), e);
		}
	}
}
