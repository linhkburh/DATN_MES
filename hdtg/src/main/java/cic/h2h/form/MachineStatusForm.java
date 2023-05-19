package cic.h2h.form;

import entity.MachineStatus;
import frwk.form.SearchForm;

public class MachineStatusForm extends SearchForm<MachineStatus>{
	MachineStatus machineStatus = new  MachineStatus();
	private String maCode, maName, machiningSts;
	
	@Override
	public MachineStatus getModel() {
		// TODO Auto-generated method stub
		return machineStatus;
	}

	public MachineStatus getMachineStatus() {
		return machineStatus;
	}

	public void setMachineStatus(MachineStatus machineStatus) {
		this.machineStatus = machineStatus;
	}

	public String getMaCode() {
		return maCode;
	}

	public void setMaCode(String maCode) {
		this.maCode = maCode;
	}

	public String getMaName() {
		return maName;
	}

	public void setMaName(String maName) {
		this.maName = maName;
	}

	public String getMachiningSts() {
		return machiningSts;
	}

	public void setMachiningSts(String machiningSts) {
		this.machiningSts = machiningSts;
	}
	

}
