package common.adapter;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import common.util.Formater;

public class DateAdapter extends XmlAdapter<String, Date> {
	@Override
	public String marshal(Date v) throws Exception {
		return Formater.date2ddsmmsyyyspHHmmss(v);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		return Formater.str2ddsmmsyyyspHHmmss(v);
	}

}
