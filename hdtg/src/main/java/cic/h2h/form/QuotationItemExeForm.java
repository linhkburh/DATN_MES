package cic.h2h.form;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import common.util.FormatNumber;
import common.util.Formater;
import entity.QuotationItem;
import entity.QuotationItemExe;
import frwk.form.SearchForm;

public class QuotationItemExeForm extends SearchForm<QuotationItemExe> {
	private BigDecimal woDuration, itemExeTime;
	private boolean makeWoByQi;

	public boolean isMakeWoByQi() {
		return makeWoByQi;
	}

	public void setMakeWoByQi(boolean makeWoByQi) {
		this.makeWoByQi = makeWoByQi;
	}
	private QuotationItem quotationItem;

	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
	}

	public BigDecimal getWoDuration() {
		return woDuration;
	}

	public void setWoDuration(BigDecimal woDuration) {
		this.woDuration = woDuration;
		this.woDurationStr = FormatNumber.num2Str(this.woDuration);
	}

	private String woDurationStr;

	public String getWoDurationStr() {
		return woDurationStr;
	}

	public void setWoDurationStr(String woDurationStr) throws ParseException {
		this.woDurationStr = woDurationStr;
		this.woDuration = FormatNumber.str2num(this.woDurationStr);
	}

	public BigDecimal getItemExeTime() {
		return itemExeTime;
	}

	public void setItemExeTime(BigDecimal itemExeTime) {
		this.itemExeTime = itemExeTime;
		this.itemExeTimeStr = FormatNumber.num2Str(this.itemExeTime);
	}

	private String itemExeTimeStr;

	public String getItemExeTimeStr() {
		return itemExeTimeStr;
	}

	public void setItemExeTimeStr(String itemExeTimeStr) throws ParseException {
		this.itemExeTimeStr = itemExeTimeStr;
		this.itemExeTime = FormatNumber.str2num(this.itemExeTimeStr);
	}

	private Long numOfMachine;

	public Long getNumOfMachine() {
		return numOfMachine;
	}

	public void setNumOfMachine(Long numOfMachine) {
		this.numOfMachine = numOfMachine;
	}

	private String exeStepCode, workerName;
	private QuotationItemExe quotationItemExe = new QuotationItemExe();

	public QuotationItemExe getQuotationItemExe() {
		return quotationItemExe;
	}

	public void setQuotationItemExe(QuotationItemExe quotationItemExe) {
		this.quotationItemExe = quotationItemExe;
	}

	@Override
	public QuotationItemExe getModel() {
		return quotationItemExe;
	}

	public String getExeStepCode() {
		return exeStepCode;
	}

	public void setExeStepCode(String exeStepCode) {
		this.exeStepCode = exeStepCode;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	private String orderCode, cusName, drawingCode, cusCode;

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
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

	public String getDrawingCode() {
		return drawingCode;
	}

	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}

	private Byte assignStatus;

	public Byte getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(Byte assignStatus) {
		this.assignStatus = assignStatus;
	}

	private Date fromDate, toDate;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getFromDateStr() {
		return Formater.date2str(this.fromDate);
	}

	public void setFromDateStr(String fromDateStr) throws Exception {
		this.fromDate = Formater.str2date(fromDateStr);
	}

	public String getToDateStr() {
		return Formater.date2str(this.toDate);
	}

	public void setToDateStr(String toDateStr) throws Exception {
		this.toDate = Formater.str2date(toDateStr);
	}
	private String manageCode;

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}
	// Ten chi tiet
	private String qiName;

	public String getQiName() {
		return qiName;
	}

	public void setQiName(String qiName) {
		this.qiName = qiName;
	}
}
