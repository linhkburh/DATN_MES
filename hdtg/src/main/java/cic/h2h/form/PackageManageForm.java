package cic.h2h.form;

import entity.DLVPackage;
import frwk.form.SearchForm;

public class PackageManageForm extends SearchForm<DLVPackage>{
	private DLVPackage dLVPackage = new DLVPackage();
	private String packageCode, cusCode, cusName, orderCode, frDate, tDate, quotationItemCode, quotationItemName, creator;
	private String managerCode;
	public String getManagerCode() {
		return managerCode;
	}
	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
	}
	private Byte status;
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	@Override
	public DLVPackage getModel() {
		return dLVPackage;
	}
	public DLVPackage getdLVPackage() {
		return dLVPackage;
	}
	public void setdLVPackage(DLVPackage dLVPackage) {
		this.dLVPackage = dLVPackage;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
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

}
