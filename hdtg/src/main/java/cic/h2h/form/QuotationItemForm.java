package cic.h2h.form;

import entity.Quotation;
import entity.QuotationItem;
import frwk.form.SearchForm;

public class QuotationItemForm extends SearchForm<QuotationItem> {
	private String keyword_code, keyword_name, code, name, orderCode, deliverFromDate, deliverToDate, manageCodeSearch,
			cusCode, cusName, technical, accountManager, creator;
	private String companyId;
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	private Boolean repaired;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	private QuotationItem quotationItem = new QuotationItem();
	private Short status;
	private String fromDate, toDate;

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

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getKeyword_code() {
		return keyword_code;
	}

	public void setKeyword_code(String keyword_code) {
		this.keyword_code = keyword_code;
	}

	public String getKeyword_name() {
		return keyword_name;
	}

	public void setKeyword_name(String keyword_name) {
		this.keyword_name = keyword_name;
	}

	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
	}

	@Override
	public QuotationItem getModel() {
		return quotationItem;
	}

	private String quotationId;

	public String getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}

	private Quotation quotation;

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	private String to;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	private String itemCode;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getDeliverFromDate() {
		return deliverFromDate;
	}

	public void setDeliverFromDate(String deliverFromDate) {
		this.deliverFromDate = deliverFromDate;
	}

	public String getDeliverToDate() {
		return deliverToDate;
	}

	public void setDeliverToDate(String deliverToDate) {
		this.deliverToDate = deliverToDate;
	}

	public String getTechnical() {
		return technical;
	}

	public void setTechnical(String technical) {
		this.technical = technical;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getManageCodeSearch() {
		return manageCodeSearch;
	}

	public void setManageCodeSearch(String manageCodeSearch) {
		this.manageCodeSearch = manageCodeSearch;
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

	private String frDate;

	public String getFrDate() {
		return frDate;
	}

	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}

	private String createDateForm, createDateTo;

	public String getCreateDateForm() {
		return createDateForm;
	}

	public void setCreateDateForm(String createDateForm) {
		this.createDateForm = createDateForm;
	}

	public String getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(String createDateTo) {
		this.createDateTo = createDateTo;
	}

	public Boolean getRepaired() {
		return repaired;
	}

	public void setRepaired(Boolean repaired) {
		this.repaired = repaired;
	}
	// Da chuyen nguoi
	private Boolean toPolishing;
	// Da chuyen QC
	private Boolean toQc;

	public Boolean getToPolishing() {
		return toPolishing;
	}

	public void setToPolishing(Boolean toPolishing) {
		this.toPolishing = toPolishing;
	}

	public Boolean getToQc() {
		return toQc;
	}

	public void setToQc(Boolean toQc) {
		this.toQc = toQc;
	}
	
	

}
