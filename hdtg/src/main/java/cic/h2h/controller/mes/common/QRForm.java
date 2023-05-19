package cic.h2h.controller.mes.common;

import frwk.form.ModelForm;

public class QRForm extends ModelForm<String> {
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String getModel() {
		return null;
	}
	private String to;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

}
