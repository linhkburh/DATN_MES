package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysUsers;

@Entity
@Table(name = "PROCESS_EXE")
@XmlAccessorType(XmlAccessType.FIELD)
/**
 * Thuc hien nguoi QC
 * @author nguye
 *
 */
public class ProcessExe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "QUOTATION_ITEM_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnore
	private QuotationItem quotationItem;

	@Column(name = "AMOUNT")
	private BigDecimal amount;
	@Column(name = "NG_AMOUNT")
	/**
	 * So luong huy (kg phai sua)
	 */
	private Long brokenAmount;
	@Column(name = "TOTAL_AMOUNT")
	private BigDecimal totalAmount;
	// Ly do loi
	@Column(name = "NG_DESCRIPTION")
	private String ngDescription;
	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	/**
	 * Cong nhan thuc hien
	 */
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SysUsers sysUser;

	@Column(name = "FROM_ITEM")
	private BigDecimal fromItem;

	@Column(name = "TO_ITEM")
	private BigDecimal toItem;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@JoinColumn(name = "UPDATOR", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SysUsers updator;

	@Column(name = "EXE_TIME")
	private BigDecimal exeTime;

	@Column(name = "RP_AMOUNT")
	private BigDecimal ngAmount;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "REPAIRE_NG")
	private Boolean ngRepaire;
	@JsonIgnore
	@JoinColumn(name = "COMPANY")
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	public SysUsers getUpdator() {
		return updator;
	}

	public void setUpdator(SysUsers updator) {
		this.updator = updator;
	}

	@Transient
	private String setupTimeStr;

	public String getNgDescription() {
		return ngDescription;
	}

	public void setNgDescription(String ngDescription) {
		this.ngDescription = ngDescription;
	}

	public Long getBrokenAmount() {
		return brokenAmount;
	}

	public void setBrokenAmount(Long brokenAmount) {
		this.brokenAmount = brokenAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTotalAmountStr() {
		return FormatNumber.num2Str(this.totalAmount);
	}

	public void setTotalAmountStr(String totalAmountStr) throws ParseException {
		this.totalAmount = FormatNumber.str2num(totalAmountStr);
	}

	public String getBrokenAmountStr() {

		return FormatNumber.num2Str(this.brokenAmount);
	}

	public void setBrokenAmountStr(String brokenAmountStr) throws ParseException {
		this.brokenAmount = FormatNumber.str2Long(brokenAmountStr);
	}

	public ProcessExe() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAmountStr() {
		return FormatNumber.num2Str(amount);
	}

	public void setAmountStr(String amountStr) throws ParseException {
		if (!Formater.isNull(amountStr))
			this.amount = FormatNumber.str2num(amountStr);
	}

	public BigDecimal getFromItem() {
		return fromItem;
	}

	public void setFromItem(BigDecimal fromItem) {
		this.fromItem = fromItem;
	}

	public BigDecimal getToItem() {
		return toItem;
	}

	public void setToItem(BigDecimal toItem) {
		this.toItem = toItem;
	}

	// Thoi diem bat dau
	@Transient
	private String startTimeStr;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		this.startTimeStr = Formater.dateTime2str(startTime);
	}

	public String getStartTimeStr() {
		return Formater.dateTime2str(startTime);
	}

	public void setStartTimeStr(String startTimeStr) throws ParseException {
		this.startTimeStr = startTimeStr;
		if (!Formater.isNull(startTimeStr))
			this.startTime = Formater.str2DateTime(startTimeStr);
	}

	// Thoi diem ket thuc
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		this.endTimeStr = Formater.dateTime2str(endTime);
	}

	@Transient
	private String endTimeStr;

	public String getEndTimeStr() {
		return Formater.dateTime2str(endTime);
	}

	public void setEndTimeStr(String endTimeStr) throws ParseException {
		this.endTimeStr = endTimeStr;
		if (!Formater.isNull(endTimeStr))
			this.endTime = Formater.str2DateTime(endTimeStr);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SysUsers getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUsers sysUser) {
		this.sysUser = sysUser;
	}

	@Transient
	private String exeTimeStr;

	public String getExeTimeStr() {
		return Formater.num2str(getExeTime());
	}

	public BigDecimal getExeTime() {
		return new BigDecimal((endTime.getTime() - startTime.getTime()) / 1000 / 60);
	}

	@Transient
	public ParentItem.TIME_UNIT getTimeUnit() {
		return ParentItem.TIME_UNIT.MINUTE;
	}

	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
		this.company = quotationItem.getCompany();
	}

	public void setExeTime(BigDecimal exeTime) {
		this.exeTime = exeTime;
	}

	public BigDecimal getNgAmount() {
		return ngAmount;
	}

	public void setNgAmount(BigDecimal ngAmount) {
		this.ngAmount = ngAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNgAmountStr() {
		return FormatNumber.num2Str(this.ngAmount);
	}

	public void setNgAmountStr(String ngAmountStr) throws ParseException {
		this.ngAmount = FormatNumber.str2num(ngAmountStr);
	}

	public Boolean getNgRepaire() {
		return ngRepaire;
	}

	public void setNgRepaire(Boolean ngRepaire) {
		this.ngRepaire = ngRepaire;
	}

	public void sumary() {
		qiTotalAmount = "CL".equals(this.getType()) ? this.quotationItem.getPolishingQuatity()
				: this.quotationItem.getQcQuatity();
		qiDoneAmount = "CL".equals(this.getType()) ? this.quotationItem.getPolishingDone()
				: this.quotationItem.getQcDone();
		qiOustandingAmount = qiTotalAmount - qiDoneAmount;
		qiNgAmount = "CL".equals(this.getType()) ? this.quotationItem.getPolishingRepaire()
				: this.quotationItem.getQcRepaire();
		if ("CL".equals(this.getType())) {
			qiNgAmount -= this.quotationItem.getPolishingRepaireDone();
		}
		qiBrokenAmount = qiDoneAmount - qiNgAmount;
	}

	@Transient
	private long qiTotalAmount;
	@Transient
	private long qiDoneAmount;
	@Transient
	private long qiOustandingAmount;
	@Transient
	private long qiNgAmount;
	@Transient
	private long qiBrokenAmount;

	public long getQiTotalAmount() {
		return qiTotalAmount;
	}

	public long getQiDoneAmount() {
		return qiDoneAmount;
	}

	public long getQiOustandingAmount() {
		return qiOustandingAmount;
	}

	public long getQiNgAmount() {
		return qiNgAmount;
	}

	public long getQiBrokenAmount() {
		return qiBrokenAmount;
	}
	/*
	 * thoi gian du kien thuc hien tat ca chi tiet con lai
	 * */
	public Long getTotalEstTime() {
		Long itmEstTime = getEstTime();
		if (itmEstTime == null)
			return null;
		return itmEstTime.longValue() * this.qiOustandingAmount;
	}
	/*
	 * thoi gian du kien thuc hien 1 chi tiet
	 * */
	public Long getEstTime() {
		return  "QC".equals(this.type) ? this.quotationItem.getQcItmEstTime()
				: "CL".equals(this.type) ? this.quotationItem.getPlshItmEstTime() : null;
	}
	/*
	 * thoi gian du kien thuc hien theo so luong nhap
	 * */
	public Long getTotalAmountEstTime() {
		Long itmEstTime = getEstTime();
		if (itmEstTime == null)
			return null;
		return itmEstTime.longValue() * this.getTotalAmount().longValue();
	}
	public BigDecimal getLatePercentP() {
		BigDecimal late = getLate();
		if(late==null)
			return null;
		if(this.getTotalAmountEstTime()==null)
			return null;
		return late.multiply(new BigDecimal(100)).divide(new BigDecimal(this.getTotalAmountEstTime()), 3, RoundingMode.HALF_UP);
	}
	public BigDecimal getLate() {
		if(this.getTotalAmountEstTime() != null)
			return new BigDecimal(this.getExeTime().longValue()-this.getTotalAmountEstTime());
		else 
			return null;
	}
}
