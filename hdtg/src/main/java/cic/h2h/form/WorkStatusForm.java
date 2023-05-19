package cic.h2h.form;

import entity.WorkStatus;
import frwk.form.SearchForm;

public class WorkStatusForm extends SearchForm<WorkStatus> {

	WorkStatus workStatus = new WorkStatus();

	String orderCode, cusName, workCode, drawingCode, stageCode, machineCode,
			machiningSts, workSts, quotationId;

	public WorkStatus getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(WorkStatus workStatus) {
		this.workStatus = workStatus;
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

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getDrawingCode() {
		return drawingCode;
	}

	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}

	public String getStageCode() {
		return stageCode;
	}

	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getMachiningSts() {
		return machiningSts;
	}

	public void setMachiningSts(String machiningSts) {
		this.machiningSts = machiningSts;
	}

	public String getWorkSts() {
		return workSts;
	}

	public void setWorkSts(String workSts) {
		this.workSts = workSts;
	}

	public String getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}

	@Override
	public WorkStatus getModel() {
		// TODO Auto-generated method stub
		return workStatus;
	}

}
