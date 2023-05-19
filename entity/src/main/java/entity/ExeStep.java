/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.util.FormatNumber;
import entity.frwk.SysDictParam;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "EXE_STEP")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExeStep implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "STEP_CODE")
	private String stepCode;
	@Column(name = "STEP_NAME")
	private String stepName;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "INIT_PRICE")
	private BigDecimal initPrice;

	@JoinColumn(name = "DIMENSION")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysDictParam dimension;
	@OneToMany(mappedBy = "previousVersion")
	@JsonIgnore
	private List<ExeStep> exeStepList;
	@JoinColumn(name = "PREVIOUS_VERSION", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private ExeStep previousVersion;
	@JoinColumn(name = "STEP_TYPE", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({ "lstExeStep", "lstExe", "lstExePro" })
	private ExeStepType stepType;
	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCY", referencedColumnName = "ID")
	private SysDictParam currency;
	@Column(name = "PROGRAM")
	private Boolean program;

	public Boolean getProgram() {
		return program;
	}

	public void setProgram(Boolean program) {
		this.program = program;
	}

	public SysDictParam getCurrency() {
		return currency;
	}

	public void setCurrency(SysDictParam currency) {
		this.currency = currency;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public ExeStep clone() {
		ExeStep clone = new ExeStep();
		clone.stepCode = "COPY" + this.stepCode;
		clone.stepName = this.stepName;
		clone.stepType = this.stepType;
		clone.initPrice = this.initPrice;
		clone.currency = this.currency;
		clone.description = this.description;
		return clone;
	}

	public ExeStep copy() {
		return this.clone();
	}

	public ExeStep() {
	}

	public ExeStep(String id) {
		this.id = id;
	}

	public ExeStep(String id, BigDecimal initPrice) {
		this.id = id;
		this.initPrice = initPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public SysDictParam getDimension() {
		return dimension;
	}

	public void setDimension(SysDictParam dimension) {
		this.dimension = dimension;
	}

	public List<ExeStep> getExeStepList() {
		return exeStepList;
	}

	public void setExeStepList(List<ExeStep> exeStepList) {
		this.exeStepList = exeStepList;
	}

	public ExeStep getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(ExeStep previousVersion) {
		this.previousVersion = previousVersion;
	}

	public ExeStepType getStepType() {
		return stepType;
	}

	public void setStepType(ExeStepType stepType) {
		this.stepType = stepType;
	}

	public String getInitPriceStr() {

		return FormatNumber.num2Str(this.initPrice);
	}

	public void setInitPriceStr(String initPriceStr) throws ParseException {
		this.initPrice = FormatNumber.str2num(initPriceStr);
	}

	public String getFullName() {
		// Truong hop co nhieu cong doan, hien thi full
		for (ExeStep step : this.stepType.getLstExeStep()) {
			if (Boolean.TRUE.equals(step.getProgram()))
				continue;
			if (!step.getId().equals(this.id))
				return this.stepType.getName() + " - " + this.stepName;
		}
		// Co 1 cong doan thi hien thi hinh thuc gia cong
		return this.stepType.getName();
	}

}
