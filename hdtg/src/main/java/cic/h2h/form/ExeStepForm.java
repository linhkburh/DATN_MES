package cic.h2h.form;

import entity.ExeStep;
import frwk.form.SearchForm;

public class ExeStepForm extends SearchForm<ExeStep> {

	private ExeStep exeStep = new ExeStep();
	private String stepTypeId;
	private Boolean isProgram;
	public Boolean getIsProgram() {
		return isProgram;
	}

	public void setIsProgram(Boolean isProgram) {
		this.isProgram = isProgram;
	}

	public String getStepTypeId() {
		return stepTypeId;
	}

	public void setStepTypeId(String stepTypeId) {
		this.stepTypeId = stepTypeId;
	}

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

	public ExeStep getExeStep() {
		return exeStep;
	}

	public void setExeStep(ExeStep exeStep) {
		this.exeStep = exeStep;
	}

	@Override
	public ExeStep getModel() {
		return exeStep;
	}
	private Boolean fixFree;

	public Boolean getFixFree() {
		return fixFree;
	}

	public void setFixFree(Boolean fixFree) {
		this.fixFree = fixFree;
	}

}
