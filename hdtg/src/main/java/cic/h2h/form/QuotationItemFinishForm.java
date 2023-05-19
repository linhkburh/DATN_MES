package cic.h2h.form;

import java.util.List;

import entity.DLVPackage;
import entity.QcOs;
import entity.QuotationItem;
import frwk.form.SearchForm;

public class QuotationItemFinishForm extends SearchForm<QuotationItem> {
	private QuotationItem quotationItem = new QuotationItem();
	private String orderCode, cusCode, cusName, drawingCode, drawingName;
	private String fromDate, toDate;
	private String manageCode, frDate, tDate;
	private String qcType;
	private List<DLVPackage> lstPackage;
	private List<QcOs> lstQcOs;
	public List<QcOs> getLstQcOs() {
		return lstQcOs;
	}

	public void setLstQcOs(List<QcOs> lstQcOs) {
		this.lstQcOs = lstQcOs;
	}

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DLVPackage> getLstPackage() {
		return lstPackage;
	}

	public void setLstPackage(List<DLVPackage> lstPackage) {
		this.lstPackage = lstPackage;
	}

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
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

	@Override
	public QuotationItem getModel() {
		return quotationItem;
	}

	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getDrawingCode() {
		return drawingCode;
	}

	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}

	public String getDrawingName() {
		return drawingName;
	}

	public void setDrawingName(String drawingName) {
		this.drawingName = drawingName;
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

	public String getQcType() {
		return qcType;
	}

	public void setQcType(String qcType) {
		this.qcType = qcType;
	}
	
}
