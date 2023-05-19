package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;
import entity.frwk.SysUsers;

@Entity
@Table(name = "WORK_PRO")
public class WorkPro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "QUOTATION_ITEM_EXE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnore
	private QuotationItemExe quotationItemExe;

	@Column(name = "CODE")
	private String code;

	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "DURATION")
	private BigDecimal duration;

	@JoinColumn(name = "PROGRAMER", referencedColumnName = "ID")
	@ManyToOne(optional = true)
	private SysUsers programer;

	public WorkPro() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlTransient
	public QuotationItemExe getQuotationItemExe() {
		return quotationItemExe;
	}

	public void setQuotationItemExe(QuotationItemExe quotationItemExe) {
		this.quotationItemExe = quotationItemExe;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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
	@Transient
	public String getStartTimeStr() {
		return Formater.dateTime2str(startTime);
	}

	public void setStartTimeStr(String startTimeStr) throws Exception {
		this.startTime = Formater.str2DateTime(startTimeStr);
	}
	@Transient
	public String getEndTimeStr() {
		return Formater.dateTime2str(endTime);
	}

	public void setEndTimeStr(String endTimeStr) throws Exception {
		this.endTime = Formater.str2DateTime(endTimeStr);

	}

	public BigDecimal getDuration() {
		return duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public SysUsers getProgramer() {
		return programer;
	}

	public void setProgramer(SysUsers programer) {
		this.programer = programer;
	}

	@Transient
	public String getDrawingCode() {
		if (this.quotationItemExe.getQuotationItemId() == null)
			return null;
		return this.quotationItemExe.getQuotationItemId().getCode();
	}

	@Transient
	public String getCustomerName() {
		if (this.quotationItemExe.getQuotationItemId() == null)
			return null;
		return this.quotationItemExe.getQuotationItemId().getQuotationId().getCustomer().getOrgName();
	}
	@Transient
	public String getOpCode() {
		if(this.quotationItemExe.getExeStepId()==null)
			return null;
		return this.quotationItemExe.getExeStepId().getStepCode();
	}

	@Transient
	public String getEstimateTime() {
		
		return this.quotationItemExe.getExeTimeStr();
	}
}
