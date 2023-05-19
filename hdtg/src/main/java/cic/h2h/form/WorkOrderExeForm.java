package cic.h2h.form;

import entity.WorkOrder;
import entity.WorkOrderExe;
import frwk.form.SearchForm;

public class WorkOrderExeForm extends SearchForm<WorkOrderExe> {

	private WorkOrderExe workOrderExe = new WorkOrderExe();
	private WorkOrder workOrder = new WorkOrder();

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
		workOrderExe.setWorkOrderId(workOrder);
	}

	private String thoigiandathuchien, thoigianconlai, soluonghoanthanh, thoigiantrungbinh, cham1, cham2;
	private String cusCode, cusName, orderCode, drawingCode, fromDate, toDate, stepName, worker, frDate, tDate,
			updatorSearch, machineSearch, shift, factoryUnit;
	private String woCode;
	private String process, type;
	private String qrManageCode, qrAmount, typeWorkExe;

	public String getQrManageCode() {
		return qrManageCode;
	}

	public void setQrManageCode(String qrManageCode) {
		this.qrManageCode = qrManageCode;
	}

	public String getQrAmount() {
		return qrAmount;
	}

	public void setQrAmount(String qrAmount) {
		this.qrAmount = qrAmount;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	private String manageCode;

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}

	public String getThoigiandathuchien() {
		return thoigiandathuchien;
	}

	public void setThoigiandathuchien(String thoigiandathuchien) {
		this.thoigiandathuchien = thoigiandathuchien;
	}

	public WorkOrderExe getWorkOrderExe() {
		return workOrderExe;
	}

	public void setWorkOrderExe(WorkOrderExe workOrderExe) {
		this.workOrderExe = workOrderExe;
	}

	public String getThoigianconlai() {
		return thoigianconlai;
	}

	public void setThoigianconlai(String thoigianconlai) {
		this.thoigianconlai = thoigianconlai;
	}

	public String getSoluonghoanthanh() {
		return soluonghoanthanh;
	}

	public void setSoluonghoanthanh(String soluonghoanthanh) {
		this.soluonghoanthanh = soluonghoanthanh;
	}

	public String getThoigiantrungbinh() {
		return thoigiantrungbinh;
	}

	public void setThoigiantrungbinh(String thoigiantrungbinh) {
		this.thoigiantrungbinh = thoigiantrungbinh;
	}

	public String getCham1() {
		return cham1;
	}

	public void setCham1(String cham1) {
		this.cham1 = cham1;
	}

	public String getCham2() {
		return cham2;
	}

	public void setCham2(String cham2) {
		this.cham2 = cham2;
	}

	@Override
	public WorkOrderExe getModel() {
		return workOrderExe;
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

	public String getMachineSearch() {
		return machineSearch;
	}

	public void setMachineSearch(String machineSearch) {
		this.machineSearch = machineSearch;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeWorkExe() {
		return typeWorkExe;
	}

	public void setTypeWorkExe(String typeWorkExe) {
		this.typeWorkExe = typeWorkExe;
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
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	private Byte source;

	public Byte getSource() {
		return source;
	}

	public void setSource(Byte source) {
		this.source = source;
	}
	
}
