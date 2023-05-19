package cic.h2h.form;

import entity.QuotationItem;
import entity.WorkOrder;
import entity.frwk.SysUsers;
import frwk.form.SearchForm;

public class WorkOderForm extends SearchForm<WorkOrder> {
	private QuotationItem quotationItem;
	private WorkOrder workOrder = new WorkOrder();
	private String orderCode, cusName, workCode, drawingCode, stageCode, machineCode, machiningSts, frDate, toDate;
	private Short workSts;
	private String managerCode; 
	private String itemName,creator, to;

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	private String companyId;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getManagerCode() {
		return managerCode;
	}
	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@Override
	public WorkOrder getModel() {
		
		return workOrder;
	}
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
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
	public Short getWorkSts() {
		return workSts;
	}
	public void setWorkSts(Short workSts) {
		this.workSts = workSts;
	}
	public QuotationItem getQuotationItem() {
		return quotationItem;
	}
	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
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
	private SysUsers sessionUser;

	public SysUsers getSessionUser() {
		return sessionUser;
	}
	public void setSessionUser(SysUsers sessionUser) {
		this.sessionUser = sessionUser;
	}
	
}
