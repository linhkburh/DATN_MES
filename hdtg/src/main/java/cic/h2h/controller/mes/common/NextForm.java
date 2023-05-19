package cic.h2h.controller.mes.common;

import entity.QuotationItem;
import entity.QuotationItemProcess;
import frwk.form.ModelForm;

public class NextForm extends ModelForm<QuotationItem>{
	private String title;
	private String qrTitle;
	public String getQrTitle() {
		return qrTitle;
	}
	public void setQrTitle(String qrTitle) {
		this.qrTitle = qrTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private  QuotationItem quotationItem;
	private QuotationItemProcess quotationItemProcess=new QuotationItemProcess();
	public QuotationItemProcess getQuotationItemProcess() {
		return quotationItemProcess;
	}
	public void setQuotationItemProcess(QuotationItemProcess quotationItemProcess) {
		this.quotationItemProcess = quotationItemProcess;
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

	
}
