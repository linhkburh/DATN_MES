package cic.h2h.form;

import entity.ExeStep;
import frwk.form.SearchForm;

public class ReportForm extends SearchForm<ExeStep> {

	ExeStep exeStep = new ExeStep();

	private String code, name;

	public ExeStep getExeStep() {
		return exeStep;
	}

	public void setExeStep(ExeStep exeStep) {
		this.exeStep = exeStep;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ExeStep getModel() {
		
		return exeStep;
	}

}
