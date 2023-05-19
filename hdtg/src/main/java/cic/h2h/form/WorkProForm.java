package cic.h2h.form;

import entity.WorkPro;
import frwk.form.SearchForm;

public class WorkProForm extends SearchForm<WorkPro> {
	
	private WorkPro workPro = new WorkPro();
	private String programer, naWorker, frDate, toDate, drCode, stepType, orderCode, cusName;
	@Override
	public WorkPro getModel() {
		
		return workPro;
	}
	public WorkPro getWorkPro() {
		return workPro;
	}
	public void setWorkPro(WorkPro workPro) {
		this.workPro = workPro;
	}
	
	
	public String getProgramer() {
		return programer;
	}
	public void setProgramer(String programer) {
		this.programer = programer;
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
	
	
	
	public String getStepType() {
		return stepType;
	}
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	
}
