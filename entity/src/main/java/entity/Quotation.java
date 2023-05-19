package entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "QUOTATION")
@XmlRootElement(name = "QUOTATION")
@XmlAccessorType(XmlAccessType.FIELD)
public class Quotation extends ParentItem implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "QUOTATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quotationDate;
	@Column(name = "QUOTATION_END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quotationEndDate;

	public Date getQuotationEndDate() {
		return quotationEndDate;
	}

	public void setQuotationEndDate(Date quotationEndDate) {
		this.quotationEndDate = quotationEndDate;
	}

	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "APPROVE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveDate;

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "STATUS")
	private Short status;
	@Column(name = "CREATOR")
	private String creator;
	@Column(name = "APPROVER")
	private String approver;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "quotationId", fetch = FetchType.LAZY)
	@XmlTransient
	// Kho vi kieu list
	@JsonIgnoreProperties("quotationId")
	private List<QuotationItem> quotationItemList = new ArrayList<QuotationItem>();
	@Column(name = "DONE")
	private Short done;
	@Lob
	@Column(name = "NOTE")
	private String note;
	@Formula("(select sum(qi.TOTAL_PRICE) from quotation_item qi where qi.quotation_id = id)")

	@Basic(fetch = FetchType.LAZY)
	private BigDecimal price;
	@JoinColumn(name = "DEPARTMENT")
	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;
	@JoinColumn(name = "COMPANY")
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;
	@JoinColumn(name = "CUSTOMER_ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Customer customer;
	@JoinColumn(name = "CURRENCY")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysDictParam currency;
	@Column(name = "RATE_MACHINE")
	private BigDecimal ratMachine;
	@Column(name = "CODE")
	private String code;
	@Formula("MES_EXE.numOfFinishChildren('q',id)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfFinishChildren;

	public BigDecimal getNumOfFinishChildren() {
		return numOfFinishChildren;
	}

	@Formula("(select count(1) from quotation_item qi where qi.quotation_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfChildren;

	@Override
	public BigDecimal getNumOfChildren() {
		return numOfChildren;
	}

	@Transient
	private BigDecimal totalEstTime;

	@Override
	public BigDecimal getTotalEstTime() {
		return totalEstTime;
	}

	public void setTotalEstTime(BigDecimal totalEstTime) {
		this.totalEstTime = totalEstTime;
	}

	@Transient
	private Short exeStatus;
	@Transient
	private Short orderSts;

	public BigDecimal getRatMachine() {
		return ratMachine;
	}

	public void setRatMachine(BigDecimal ratMachine) {
		this.ratMachine = ratMachine;
	}

	public void setRatMachineStr(String ratMachineStr) throws ParseException {
		this.ratMachine = FormatNumber.str2num(ratMachineStr);

	}

	public String getRatMachineStr() {
		return FormatNumber.num2Str(this.ratMachine);
	}

	@Transient
	private BigDecimal exchangePrice;

	public SysDictParam getCurrency() {
		return currency;
	}

	public void setCurrency(SysDictParam currency) {
		this.currency = currency;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Quotation() {
	}

	public Quotation(String id) {
		this.id = id;
	}

	public Quotation(SysUsers su) {
		this.createDate = Calendar.getInstance().getTime();
		this.creator = su.getId();
		this.company = su.getCompany();
		this.department = su.getDepartment();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public List<QuotationItem> getQuotationItemList() {
		return quotationItemList;
	}

	public void setQuotationItemList(List<QuotationItem> quotationItemList) {
		this.quotationItemList = quotationItemList;
	}

	public Short getDone() {
		return done;
	}

	public void setDone(Short done) {
		this.done = done;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Transient
	public BigDecimal getPrice() {
		return price;
	}

	@Transient
	public String getPriceStr() {
		return FormatNumber.num2Str(getPrice());
	}

	public String getQuotationDateStr() {
		return Formater.date2str(this.quotationDate);
	}

	public void setQuotationDateStr(String quotationDateStr) throws Exception {
		this.quotationDate = Formater.str2date(quotationDateStr);
	}

	public String getQuotationEndDateStr() {
		return Formater.dateTime2str(this.quotationEndDate);
	}

	public void setQuotationEndDateStr(String quotationEndDateStr) throws Exception {
		this.quotationEndDate = Formater.str2DateTime(quotationEndDateStr);
	}

	public BigDecimal getExchangePrice() {
		return exchangePrice;
	}

	public void setExchangePrice(BigDecimal exchangePrice) {
		this.exchangePrice = exchangePrice;
	}

	@Transient
	public String getExchangePriceStr() {
		return FormatNumber.num2Str(this.exchangePrice);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Short getExeStatus() {
		return exeStatus;
	}

	public void setExeStatus(Short exeStatus) {
		this.exeStatus = exeStatus;
	}

	public Short getOrderSts() {
		return orderSts;
	}

	public void setOrderSts(Short orderSts) {
		this.orderSts = orderSts;
	}

	@Override
	public List<? extends ParentItem> getLstChildren() {
		return this.quotationItemList;
	}

	@Transient
	private String trangThaiDonHang, trangThaiSX;

	public String getTrangThaiDonHang() {
		return trangThaiDonHang;
	}

	public void setTrangThaiDonHang(String trangThaiDonHang) {
		this.trangThaiDonHang = trangThaiDonHang;
	}

	public String getTrangThaiSX() {
		return trangThaiSX;
	}

	public void setTrangThaiSX(String trangThaiSX) {
		this.trangThaiSX = trangThaiSX;
	}

	@Override
	@Transient
	public BigDecimal getAmount() {
		return new BigDecimal(this.quotationItemList.size());
	}

	public void setSetupTime(BigDecimal setupTime) {
		this.setupTime = setupTime;

	}

	@Transient
	private BigDecimal setupTime;

	@Override
	public BigDecimal getSetupTime() {
		return setupTime;
	}

	@Transient
	private BigDecimal estTime;

	@Override
	public BigDecimal getEstTime() {
		return estTime;
	}

	public void setEstTime(BigDecimal estTime) {
		this.estTime = estTime;
	}

	/**
	 * Thoi diem bat dau
	 */
	@Transient
	private Date startTime;
	/**
	 * Thoi diem ket thuc
	 */
	@Transient
	private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Thoi gian gia cong thuc te
	 */
	@Transient
	private BigDecimal exeTime;
	@Override
	public BigDecimal getExeTime() {
		return exeTime;
	}
	
	public void setExeTime(BigDecimal exeTime) {
		this.exeTime = exeTime;
	}
}
