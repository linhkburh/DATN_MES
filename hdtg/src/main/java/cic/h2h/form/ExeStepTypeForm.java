package cic.h2h.form;

import entity.ExeStepType;
import frwk.form.SearchForm;

public class ExeStepTypeForm extends SearchForm<ExeStepType> {
	ExeStepType stepType = new ExeStepType();

	private String code, name;

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

	
	public ExeStepType getStepType() {
		return stepType;
	}

	public void setStepType(ExeStepType stepType) {
		this.stepType = stepType;
	}

	@Override
	public ExeStepType getModel() {
		
		return stepType;
	}

}
