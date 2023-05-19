package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonProperty;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;

@Entity
@Table(name = "WORK_ORDER_EXE")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkOrderExe extends ParentItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "WORK_ORDER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnoreProperties({ "quotationItemExe", "workOrderExes", "astMachine", "creator", "qi" })
	private WorkOrder workOrderId;
	/**
	 * So luong hoan thanh
	 */
	@Column(name = "AMOUNT")
	private BigDecimal amount;
	@Column(name = "NG_AMOUNT")
	private Long ngAmount;
	@Column(name = "BROKEN_AMOUNT")
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
	@JsonIgnoreProperties({ "company", "department", "lstMail" })
	private SysUsers sysUser;

	@Column(name = "FROM_ITEM")
	private BigDecimal fromItem;

	@Column(name = "TO_ITEM")
	private BigDecimal toItem;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = "SETUP_TIME")
	private BigDecimal setupTime;
	@JoinColumn(name = "UPDATOR", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({ "company", "department", "lstMail" })
	private SysUsers updator;
	@JoinColumn(name = "MACHINE_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties("company")
	private AstMachine astMachine;

	@JoinColumn(name = "COMPANY")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name = "REPAIRE_NG")
	private Boolean ngRepaire;

	@JoinColumn(name = "ERROR_CAUSE_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysDictParam errorCause;

	public SysDictParam getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(SysDictParam errorCause) {
		this.errorCause = errorCause;
	}

	public AstMachine getAstMachine() {
		return astMachine;
	}

	public void setAstMachine(AstMachine astMachine) {
		this.astMachine = astMachine;
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

	@Override
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

	public String getBrokenAmountStr() {

		return FormatNumber.num2Str(this.brokenAmount);
	}

	public void setBrokenAmountStr(String brokenAmountStr) throws ParseException {
		this.brokenAmount = FormatNumber.str2Long(brokenAmountStr);
	}

	public String getNgAmountStr() {

		return FormatNumber.num2Str(this.ngAmount);
	}

	public void setNgAmountStr(String ngAmountStr) throws ParseException {
		this.ngAmount = FormatNumber.str2Long(ngAmountStr);
	}

	public WorkOrderExe() {
		super();
	}

	public WorkOrderExe(WorkOrder workOrder) {
		this.workOrderId = workOrder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlTransient
	public WorkOrder getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(WorkOrder workOrderId) {
		this.workOrderId = workOrderId;
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

	@Override
	public BigDecimal getNumOfFinishChildren() {
		return this.amount;
	}

	@Override
	public BigDecimal getNumOfChildren() {
		return this.amount;
	}

	@Override
	@Transient
	public BigDecimal getEstTime() {
		if (Boolean.TRUE.equals(this.ngRepaire))
			return null;
		BigDecimal unitExeTime = this.getWorkOrderId().getQuotationItemExe().getUnitExeTime();
		if (unitExeTime == null)
			return null;
		return this.amount.multiply(unitExeTime);
	}

	@Override
	@Transient
	public BigDecimal getTargetTime() {
		BigDecimal targetTime = this.getWorkOrderId().getQuotationItemExe().getQuotationExeTime();
		if (targetTime == null)
			return null;
		return this.amount.multiply(targetTime);
	}

	@Override
	@Transient
	public BigDecimal getTotalEstTime() {
		return getEstTime();
	}

	@Override
	public List<ParentItem> getLstChildren() {
		return null;
	}

	// Thoi diem bat dau
	@Transient
	@JsonProperty
	private String startTimeStr;

	@Override
	public Date getStartTime() {
		return startTime;
	}

	@Override
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		this.startTimeStr = Formater.dateTime2str(startTime);
	}

	@Override
	public String getStartTimeStr() {
		return Formater.dateTime2str(startTime);
	}

	@Override
	public void setStartTimeStr(String startTimeStr) throws ParseException {
		this.startTimeStr = startTimeStr;
		if (!Formater.isNull(startTimeStr))
			this.startTime = Formater.str2DateTime(startTimeStr);
	}

	// Thoi diem ket thuc

	@Override
	@JsonProperty
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		this.endTimeStr = Formater.dateTime2str(endTime);
	}

	@Transient
	@JsonProperty
	private String endTimeStr;

	@Override
	public String getEndTimeStr() {
		return Formater.dateTime2str(endTime);
	}

	@Override
	public void setEndTimeStr(String endTimeStr) throws ParseException {
		this.endTimeStr = endTimeStr;
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

	@Override
	public BigDecimal getExeTime() {
		BigDecimal exeTime = new BigDecimal((endTime.getTime() - startTime.getTime()) / 1000 / 60);
		if (setupTime != null)
			exeTime = exeTime.subtract(setupTime);
		return exeTime;
	}

	@Transient
	private String exeTimeStr;

	@Override
	public String getExeTimeStr() {
		return Formater.num2str(getExeTime());
	}

	@Override
	public Short getWorkSts() {
		return ParentItem.WORK_STS_FINISH;
	}

	public BigDecimal getSetupTime() {
		return setupTime;
	}

	public void setSetupTime(BigDecimal setupTime) {
		this.setupTime = setupTime;
		this.setupTimeStr = FormatNumber.num2Str(setupTime);
	}

	public String getSetupTimeStr() {
		return FormatNumber.num2Str(setupTime);
	}

	public void setSetupTimeStr(String setupTimeStr) throws ParseException {
		this.setupTimeStr = setupTimeStr;
		this.setupTime = FormatNumber.str2num(setupTimeStr);
	}

	@Override
	@Transient
	public ParentItem.TIME_UNIT getTimeUnit() {
		return ParentItem.TIME_UNIT.MINUTE;
	}

	@Override
	public BigDecimal getNumOfFinishItem() {
		return this.amount;
	}

	@Transient
	public BigDecimal getLatePercentP() {
		BigDecimal estTime = getEstTime();
		if (estTime == null || estTime.compareTo(new BigDecimal(0)) == 0)
			return null;
		return getLateTime().multiply(new BigDecimal(100)).divide(getEstTime(), 3, RoundingMode.HALF_UP);
	}

	public BigDecimal getLate() {
		if (getEstTime() == null)
			return getExeTime();
		return getExeTime().subtract(getEstTime());
	}

	@Override
	public BigDecimal getExeSetupTime() {
		return setupTime;
	}

	public Boolean getNgRepaire() {
		return ngRepaire;
	}

	public void setNgRepaire(Boolean ngRepaire) {
		this.ngRepaire = ngRepaire;
	}

	@Override
	public Long getBrokenAmount() {
		return brokenAmount;
	}

	public void setBrokenAmount(Long brokenAmount) {
		this.brokenAmount = brokenAmount;
	}
}
