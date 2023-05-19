package cic.h2h.form;

import entity.ProcessExe;
import frwk.form.SearchForm;

public class ProcessExeForm extends SearchForm<ProcessExe> {
	private ProcessExe processExe = new ProcessExe();
	private String cusCode, cusName, drawingCode, fromDate, toDate, stepName, worker, frDate, tDate, updatorSearch,
			manageCode, orderCode;

	private String type, amountExsit;

	private String hisType;
	
	public String getHisType() {
		return hisType;
	}

	public void setHisType(String hisType) {
		this.hisType = hisType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public ProcessExe getModel() {
		return processExe;
	}

	public ProcessExe getProcessExe() {
		return processExe;
	}

	public void setProcessExe(ProcessExe processExe) {
		this.processExe = processExe;
	}

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getDrawingCode() {
		return drawingCode;
	}

	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getFrDate() {
		return frDate;
	}

	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}

	public String gettDate() {
		return tDate;
	}

	public void settDate(String tDate) {
		this.tDate = tDate;
	}

	public String getUpdatorSearch() {
		return updatorSearch;
	}

	public void setUpdatorSearch(String updatorSearch) {
		this.updatorSearch = updatorSearch;
	}

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}

	public String getAmountExsit() {
		return amountExsit;
	}

	public void setAmountExsit(String amountExsit) {
		this.amountExsit = amountExsit;
	}

}
