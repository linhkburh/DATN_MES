package cic.h2h.form;

import entity.QuotationItemProcess;
import frwk.form.SearchForm;

public class QuotationItemProcessForm extends SearchForm<QuotationItemProcess> {

	private QuotationItemProcess quotationItemProcess = new QuotationItemProcess();

	private String cusCode, cusName, orderCode, frDate, tDate, quotationItemCode, quotationItemName, creator;
	private String managerCode, user, type, to;

	private Byte status;
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	public String getQuotationItemCode() {
		return quotationItemCode;
	}

	public void setQuotationItemCode(String quotationItemCode) {
		this.quotationItemCode = quotationItemCode;
	}

	public String getQuotationItemName() {
		return quotationItemName;
	}

	public void setQuotationItemName(String quotationItemName) {
		this.quotationItemName = quotationItemName;
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

	public QuotationItemProcess getQuotationItemProcess() {
		return quotationItemProcess;
	}

	public void setQuotationItemProcess(QuotationItemProcess quotationItemProcess) {
		this.quotationItemProcess = quotationItemProcess;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public QuotationItemProcess getModel() {
		return quotationItemProcess;
	}

}
