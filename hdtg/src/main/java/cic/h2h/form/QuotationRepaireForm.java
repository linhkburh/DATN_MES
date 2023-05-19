package cic.h2h.form;

import entity.QuotationRepaire;
import frwk.form.SearchForm;

public class QuotationRepaireForm extends SearchForm<QuotationRepaire>{
	private QuotationRepaire quotationRepaire = new QuotationRepaire();
	private String  worker, manageCode, startTime, endTime, amount, amountOK, ngAmount, ngDescription, machine;
	private String drawingCode, manageCodeSearch, fromDate, toDate, frDate, tDate, orderCode, workerSearch, updatorSearch;
	@Override
	public QuotationRepaire getModel() {
		return quotationRepaire;
	}
	public QuotationRepaire getQuotationRepaire() {
		return quotationRepaire;
	}
	public void setQuotationRepaire(QuotationRepaire quotationRepaire) {
		this.quotationRepaire = quotationRepaire;
	}
	
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	
	public String getManageCode() {
		return manageCode;
	}
	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmountOK() {
		return amountOK;
	}
	public void setAmountOK(String amountOK) {
		this.amountOK = amountOK;
	}
	public String getNgAmount() {
		return ngAmount;
	}
	public void setNgAmount(String ngAmount) {
		this.ngAmount = ngAmount;
	}
	public String getNgDescription() {
		return ngDescription;
	}
	public void setNgDescription(String ngDescription) {
		this.ngDescription = ngDescription;
	}
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public String getDrawingCode() {
		return drawingCode;
	}
	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}
	public String getManageCodeSearch() {
		return manageCodeSearch;
	}
	public void setManageCodeSearch(String manageCodeSearch) {
		this.manageCodeSearch = manageCodeSearch;
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
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getWorkerSearch() {
		return workerSearch;
	}
	public void setWorkerSearch(String workerSearch) {
		this.workerSearch = workerSearch;
	}
	public String getUpdatorSearch() {
		return updatorSearch;
	}
	public void setUpdatorSearch(String updatorSearch) {
		this.updatorSearch = updatorSearch;
	}
	
	
}
