package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysUsers;

@Entity
@Table(name = "QUOTATION_REPAIRE")
@XmlAccessorType(XmlAccessType.FIELD)
/**
 * Sua hang
 * 
 * @author nguye
 *
 */
public class QuotationRepaire implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "QUOTATION_ITEM_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	@XmlTransient
	@JsonIgnore
	private QuotationItem quotationItem;

	@Column(name = "AMOUNT")
	private BigDecimal amount;
	@Column(name = "NG_AMOUNT")
	/**
	 * So luong huy (do la chuc nang sua hang)
	 */
	private Long ngAmount;
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
	@Column(name = "EXE_TIME")
	private Long exeTime;
	/**
	 * Cong nhan thuc hien
	 */
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = true)
	private SysUsers sysUser;
	@JoinColumn(name = "UPDATOR", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({ "company", "department", "lstMail" })
	private SysUsers updator;
	@Column(name = "FROM_ITEM")
	private BigDecimal fromItem;

	@Column(name = "TO_ITEM")
	private BigDecimal toItem;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@JoinColumn(name = "MACHINE_ID", referencedColumnName = "ID")
	@ManyToOne
	private AstMachine astMachine;

	public AstMachine getAstMachine() {
		return astMachine;
	}

	public void setAstMachine(AstMachine astMachine) {
		this.astMachine = astMachine;
	}

	@Transient
	private String setupTimeStr;

	public String getNgDescription() {
		return ngDescription;
	}

	public void setNgDescription(String ngDescription) {
		this.ngDescription = ngDescription;
	}

	public Long getNgAmount() {
		return ngAmount;
	}

	public void setNgAmount(Long ngAmount) {
		this.ngAmount = ngAmount;
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

	public String getNgAmountStr() {

		return FormatNumber.num2Str(this.ngAmount);
	}

	public void setNgAmountStr(String ngAmountStr) throws ParseException {
		this.ngAmount = FormatNumber.str2Long(ngAmountStr);
	}

	public QuotationRepaire() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlTransient
	@JsonIgnore
	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
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
		return startTimeStr;
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
		return endTimeStr;
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

	public Long getExeTime() {
		return exeTime;
	}

	public void setExeTime(Long exeTime) {
		this.exeTime = exeTime;
	}

	@Transient
	private String exeTimeStr;

	public String getExeTimeStr() {
		return FormatNumber.num2Str(this.exeTime);
	}

	public SysUsers getUpdator() {
		return updator;
	}

	public void setUpdator(SysUsers updator) {
		this.updator = updator;
	}
}
