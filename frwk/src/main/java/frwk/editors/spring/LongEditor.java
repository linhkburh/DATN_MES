package frwk.editors.spring;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import common.util.FormatNumber;


@Component(value="longEditor")
public class LongEditor extends PropertyEditorSupport {
	private static Logger lg = Logger.getLogger(TimestampEditor.class);
	@Override
	public String getAsText() {
		return FormatNumber.num2Str((Long) getValue());
	}
	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		try {
			setValue(FormatNumber.str2Long(text));
		} catch (ParseException e) {
			lg.error(e.getMessage(), e);
		}
	}
}
