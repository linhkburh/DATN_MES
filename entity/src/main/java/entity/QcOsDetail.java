package entity;
// Generated Feb 24, 2023 2:22:43 PM by Hibernate Tools 5.2.12.Final

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * QcOsDetail generated by hbm2java
 */
@Entity
@Table(name = "QC_OS_DETAIL")
public class QcOsDetail implements java.io.Serializable {

	private String id;
	@JsonIgnoreProperties({ "department", "company", "technicalId", "planner", "acId", "quotationItemMaterialList",
		"lstProcessExe", "lstQuotationRepaire", "quotationId", "quotationItemAllExeList","quotationItemExeList","quotationItemProList","siggle","factoryUnit" })
	private QuotationItem quotationItem;
	@JsonIgnoreProperties({ "quotationItemId"})
	private QuotationItemExe quotationItemExe;
	@JsonIgnoreProperties({ "quotationItemExe"})
	private WorkOrder workOrder;
	@JsonIgnoreProperties("qcOsDetails")
	private QcOs qcOs;
	private long amount;
	@JsonIgnoreProperties({ "workOrder","workOrderExe","qcChkOutSrcDetails","creator","worker"})
	private QcChkOutSrc qcChkOutSrc;
	private long doneAmount;
	private long partnerDoneAmount;
	/*
	 * so luong da nhan
	 * */
	@Formula("(select nvl(sum(d.ng_amount + d.broken_amount + d.amount),0) from QC_IN_DETAIL d where d.qc_os_detail_id=id)")
	public long getPartnerDoneAmount() {
		return partnerDoneAmount;
	}
	
	public void setPartnerDoneAmount(long partnerDoneAmount) {
		this.partnerDoneAmount = partnerDoneAmount;
	}
	/*
	 * so luong da chuyen gia cong ngoai qc_os_detail_id
	 * */
	@Formula("(select nvl(sum(amount),0) from QC_IN_DETAIL d where d.qc_os_detail_id=id)")
	public long getDoneAmount() {
		return doneAmount;
	}

	public void setDoneAmount(long doneAmount) {
		this.doneAmount = doneAmount;
	}

	public QcOsDetail() {
	}

	public QcOsDetail(String id, QuotationItem quotationItem, QcOs qcOs, long amount) {
		this.id = id;
		this.quotationItem = quotationItem;
		this.qcOs = qcOs;
		this.amount = amount;
	}

	public QcOsDetail(QcChkOutSrc qcChkOutSrc, long amount) {
		this.qcChkOutSrc = qcChkOutSrc;
		this.workOrder = qcChkOutSrc.getWorkOrder();
		this.amount = amount;
		this.quotationItem = qcChkOutSrc.getWorkOrder().getQuotationItemExe().getQuotationItemId();
	}

	public QcOsDetail(String id, QuotationItem quotationItem, QuotationItemExe quotationItemExe, QcOs qcOs,
			long amount) {
		this.id = id;
		this.quotationItem = quotationItem;
		this.quotationItemExe = quotationItemExe;
		this.qcOs = qcOs;
		this.amount = amount;
	}

	public QcOsDetail(QuotationItem quotationItem, QcOs qcOs, long amount) {
		this.quotationItem = quotationItem;
		this.qcOs = qcOs;
		this.amount = amount;
		qcOs.getQcOsDetails().add(this);
	}

	public QcOsDetail(WorkOrder workOrder, QuotationItem quotationItem, QcOs qcOs, long amount) {
		this.workOrder = workOrder;
		this.qcOs = qcOs;
		this.amount = amount;
		qcOs.getQcOsDetails().add(this);
	}

	public QcOsDetail(QcChkOutSrc qcChkOutSrc, QcOs os, long amount) {
		this.qcChkOutSrc = qcChkOutSrc;
		this.workOrder = qcChkOutSrc.getWorkOrder();
		this.qcOs = os;
		this.amount = amount;
		this.quotationItem = workOrder.getQuotationItemExe().getQuotationItemId();
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUOTATION_ITEM_ID")
	public QuotationItem getQuotationItem() {
		return this.quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUOTATION_ITEM_EXE_ID")
	public QuotationItemExe getQuotationItemExe() {
		return this.quotationItemExe;
	}

	public void setQuotationItemExe(QuotationItemExe quotationItemExe) {
		this.quotationItemExe = quotationItemExe;
	}

	@JoinColumn(name = "WORK_ORDER_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public WorkOrder getWorkOrder() {
		return this.workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	@JoinColumn(name = "QC_CHK_OUT_SRC_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public QcChkOutSrc getQcChkOutSrc() {
		return qcChkOutSrc;
	}

	public void setQcChkOutSrc(QcChkOutSrc qcChkOutSrc) {
		this.qcChkOutSrc = qcChkOutSrc;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QC_OS_ID", nullable = false)
	public QcOs getQcOs() {
		return this.qcOs;
	}

	public void setQcOs(QcOs qcOs) {
		this.qcOs = qcOs;
	}

	@Column(name = "AMOUNT", nullable = false, precision = 20, scale = 0)
	public long getAmount() {
		return this.amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
}
