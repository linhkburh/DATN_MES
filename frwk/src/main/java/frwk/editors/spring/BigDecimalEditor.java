package frwk.editors.spring;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import common.util.FormatNumber;

@Component(value="bigDecimalEditor")
public class BigDecimalEditor extends PropertyEditorSupport {
	private static Logger lg = Logger.getLogger(BigDecimalEditor.class);
	@Override
	public String getAsText() {
		return FormatNumber.num2Str((BigDecimal) getValue());
	}
	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		try {
			setValue(FormatNumber.str2num(text));
		} catch (ParseException e) {
			lg.error(e.getMessage(), e);
		}
	}
}
