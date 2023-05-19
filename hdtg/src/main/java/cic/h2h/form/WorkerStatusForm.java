package cic.h2h.form;

import entity.WorkerStatus;
import frwk.form.SearchForm;

public class WorkerStatusForm extends SearchForm<WorkerStatus>{
	WorkerStatus workerStatus = new WorkerStatus();
	private String woCode, naWorker, frDate, toDate, drCode, opCode, maCode, machiningSts;
	private String shift, factoryUnit;
	private String company;
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getFactoryUnit() {
		return factoryUnit;
	}
	public void setFactoryUnit(String factoryUnit) {
		this.factoryUnit = factoryUnit;
	}
	@Override
	public WorkerStatus getModel() {
		return workerStatus;
	}
	public WorkerStatus getWorkerStatus() {
		return workerStatus;
	}
	public void setWorkerStatus(WorkerStatus workerStatus) {
		this.workerStatus = workerStatus;
	}
	public String getWoCode() {
		return woCode;
	}
	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}
	public String getNaWorker() {
		return naWorker;
	}
	public void setNaWorker(String naWorker) {
		this.naWorker = naWorker;
	}
	public String getFrDate() {
		return frDate;
	}
	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDrCode() {
		return drCode;
	}
	public void setDrCode(String drCode) {
		this.drCode = drCode;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public String getMaCode() {
		return maCode;
	}
	public void setMaCode(String maCode) {
		this.maCode = maCode;
	}
	public String getMachiningSts() {
		return machiningSts;
	}
	public void setMachiningSts(String machiningSts) {
		this.machiningSts = machiningSts;
	}
	
}
