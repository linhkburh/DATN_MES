/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysUsers;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "QUOTATION_ITEM_EXE")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuotationItemExe extends ParentItem implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "UNIT_EXE_TIME")
	private BigDecimal unitExeTime;
	@Column(name = "QUOTATION_EXE_TIME")
	private BigDecimal quotationExeTime;

	@JoinColumn(name = "EXE_STEP_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ExeStep exeStepId;
	@JoinColumn(name = "QUOTATION_ITEM_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JsonBackReference
	@XmlTransient
	private QuotationItem quotationItemId;
	@Column(name = "INIT_PRICE")
	private BigDecimal initPrice;
	@JoinColumn(name = "worker", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SysUsers worker;
	@Column(name = "STATUS")
	private Short status;
	@Column(name = "DIS_ORDER")
	private Short disOrder;

	public Short getDisOrder() {
		return disOrder;
	}

	public void setDisOrder(Short disOrder) {
		this.disOrder = disOrder;
	}

	public Short getStatus() {
		return status;
	}

	public BigDecimal getQuotationExeTime() {
		return quotationExeTime;
	}

	public String getQuotationExeTimeStr() {
		return FormatNumber.num2Str(this.quotationExeTime);
	}

	public void setQuotationExeTimeStr(String quotationExeTimeStr) throws ParseException {
		this.quotationExeTime = FormatNumber.str2num(quotationExeTimeStr);
	}

	public void setQuotationExeTime(BigDecimal quotationExeTime) {
		this.quotationExeTime = quotationExeTime;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Formula("(select count(1) from work_order wo where wo.quotation_item_exe_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfChildren;

	@Override
	public BigDecimal getNumOfChildren() {
		return numOfChildren;
	}

	public BigDecimal getUnitExeTime() {
		return unitExeTime;
	}

	public void setUnitExeTime(BigDecimal unitExeTime) {
		this.unitExeTime = unitExeTime;
	}

	public String getUnitExeTimeStr() {
		return FormatNumber.num2Str(this.unitExeTime);
	}

	public void setUnitExeTimeStr(String unitExeTimeStr) throws ParseException {
		this.unitExeTime = FormatNumber.str2num(unitExeTimeStr);
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public String getInitPriceStr() {
		return FormatNumber.num2Str(this.initPrice);
	}

	public void setInitPriceStr(String initPriceStr) throws ParseException {
		this.initPrice = FormatNumber.str2num(initPriceStr);
	}

	public QuotationItemExe(ExeStep exeStepId, BigDecimal unitExeTime) {
		this.exeStepId = exeStepId;
		this.unitExeTime = unitExeTime;

	}

	public BigDecimal getPrice() {
		if (unitExeTime == null)
			return null;
		return this.unitExeTime.multiply(this.initPrice).divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP);
	}

	public QuotationItemExe() {
	}

	public QuotationItemExe(String id) {
		this.id = id;
	}

	public String getId() {
		if (Formater.isNull(id))
			id = null;
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ExeStep getExeStepId() {
		return exeStepId;
	}

	public void setExeStepId(ExeStep exeStepId) {
		this.exeStepId = exeStepId;
	}

	public QuotationItem getQuotationItemId() {
		return quotationItemId;
	}

	public void setQuotationItemId(QuotationItem quotationItemId) {
		this.quotationItemId = quotationItemId;
	}

	@Transient
	public String getPriceStr() {
		return FormatNumber.num2Str(getPrice());
	}

	public SysUsers getWorker() {
		return worker;
	}

	public void setWorker(SysUsers worker) {
		this.worker = worker;
	}

	public String getOrderCode() {
		if (this.quotationItemId == null || this.quotationItemId.getQuotationId() == null)
			return null;
		return this.quotationItemId.getQuotationId().getCode();
	}

	/**
	 * So luong chi tiet chua phan cong
	 * 
	 * @return
	 */
	@Transient
	public BigDecimal getRemain() {
		if (Boolean.TRUE.equals(this.exeStepId.getProgram()))
			return null;
		BigDecimal remain = new BigDecimal(this.quotationItemId.getQuality().doubleValue());
		remain = remain.subtract(this.getAssignAmount());
		return remain;
	}

	/**
	 * So luong chi tiet chua phan cong
	 * 
	 * @return
	 */
	@Transient
	public String getRemainStr() {
		return FormatNumber.num2Str(this.getRemain());
	}

	@OneToMany(mappedBy = "quotationItemExe", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnore
	private List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
	@OneToMany(mappedBy = "quotationItemExe", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<WorkPro> workPros = new ArrayList<WorkPro>();

	@JsonIgnoreProperties("quotationItemExe")
	@JsonIgnore
	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public void setWorkOrders(List<WorkOrder> workOrders) {
		this.workOrders = workOrders;
	}

	public List<WorkPro> getWorkPros() {
		return workPros;
	}

	public void setWorkPros(List<WorkPro> workPros) {
		this.workPros = workPros;
	}

	/**
	 * Tinh so luong da phan cong, so luong da hoan thanh, chu san xuat
	 */
	public void summary() {
		this.assignAmount = new BigDecimal("0");
		for (WorkOrder wo : this.workOrders) {
			if (wo.getNumOfFinishChildren() != null)
				this.numOfFinishChildren = this.numOfFinishChildren.add(wo.getNumOfFinishChildren());
			if (wo.getAmount() != null)
				this.assignAmount = this.assignAmount.add(wo.getAmount());
		}
		if (this.numOfFinishChildren == null)
			this.waitAmount = this.quotationItemId.getQuality();
		else
			this.waitAmount = this.quotationItemId.getQuality().subtract(this.numOfFinishChildren);
	}

	/**
	 * So luong chi tiet chua thuc hien
	 */
	@Transient
	public BigDecimal waitAmount;

	public BigDecimal getWaitAmount() {
		if (waitAmount == null)
			summary();
		return waitAmount;
	}

	@Transient
	public BigDecimal assignAmount;

	public BigDecimal getAssignAmount() {
		if (assignAmount == null)
			summary();
		return assignAmount;
	}

	public void setAssignAmount(BigDecimal assignAmount) {
		this.assignAmount = assignAmount;
	}

	@Formula("MES_EXE.numOfFinishChildren('qie',id)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfFinishChildren;

	@Override
	public BigDecimal getNumOfFinishChildren() {
		return numOfFinishChildren;
	}

	@Transient
	@JsonIgnore
	public String getCode() {
		return this.getQuotationItemId().getManageCode() + "-" + this.getExeStepId().getStepType().getCode() + "-"
				+ this.getExeStepId().getStepCode();
	}

	@Override
	public List<? extends ParentItem> getLstChildren() {
		return this.workOrders;
	}

	@Override
	public BigDecimal getTotalEstTime() {
		if (this.unitExeTime == null)
			return null;
		return this.quotationItemId.getQuality().multiply(this.unitExeTime).divide(new BigDecimal(60), 3,
				RoundingMode.HALF_UP);
	}

	@Override
	@Transient
	public BigDecimal getAmount() {
		return this.quotationItemId.getAmount();
	}

	public static class SortQie implements Comparator<QuotationItemExe> {
		@Override
		public int compare(QuotationItemExe before, QuotationItemExe after) {
			if (before.getDisOrder() == null && after.getDisOrder() == null)
				return 0;
			if (before.getDisOrder() == null)
				return -1;
			if (after.getDisOrder() == null)
				return 1;
			return before.getDisOrder().compareTo(after.getDisOrder());
		}

	}

	@JsonIgnore
	public List<QuotationItemExe> getPredecessor() {
		List<QuotationItemExe> predecessors = new ArrayList<QuotationItemExe>();
		for (QuotationItemExe qie : this.quotationItemId.getQuotationItemAllExeList()) {
			if (Boolean.TRUE.equals(qie.exeStepId.getProgram()))
				continue;
			if (qie.getDisOrder() >= this.disOrder)
				continue;
			predecessors.add(qie);
		}
		Collections.sort(predecessors, new QuotationItemExe.SortQie());
		int iFirstOsIdx = -1;
		for (int i = 0; i < predecessors.size(); i++) {
			QuotationItemExe qie = predecessors.get(i);
			if ("OS".equals(qie.getExeStepId().getStepType().getCode()))
				iFirstOsIdx = i;
		}
		predecessors = predecessors.subList(iFirstOsIdx, predecessors.size() - 1);
		return predecessors;
	}

	@JsonProperty("manageCode")
	@Transient
	public String getManageCode() {
		return this.quotationItemId.getManageCode();
	}

	@JsonProperty("drawCode")
	@Transient
	public String getDrawCode() {
		return this.quotationItemId.getCode();
	}

	@JsonProperty("toPartnerAmount")
	@Transient
	public String getToPartnerAmount() {
		return FormatNumber.num2Str(this.quotationItemId.getToPartnerAmount());
	}

	/**
	 * Tim doi ung
	 * 
	 * @return
	 */

	@Transient
	@JsonIgnore
	public QuotationItemExe getCorresponding() {
		for (QuotationItemExe qie : this.quotationItemId.getQuotationItemAllExeList()) {
			if(qie.exeStepId==null || qie.exeStepId.getStepType()==null)
				continue;
			System.out.print(qie.exeStepId.getStepType().getId());
			// Khac hinh thuc gia cong
			if (!this.exeStepId.getStepType().getId().equals(qie.exeStepId.getStepType().getId()))
				continue;
			// Ban ghi hien tai
			if (this.id.equals(qie.getId()))
				continue;
			// Cung ma
			if (this.exeStepId.getStepCode().equals(qie.exeStepId.getStepCode()))
				return qie;
		}
		return null;
	}

}
