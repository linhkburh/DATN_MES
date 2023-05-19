package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;

@Entity
@Table(name = "MACHINE")
public class AstMachine implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "STATUS")
	private Boolean status;

	@Column(name = "AST_NAME")
	private String astName;

	@Column(name = "AST_OWNER")
	private String astOwner;

	@Column(name = "CERTI_NO")
	private String certiNo;

	@Column(name = "AST_STATUS")
	private String astStatus;

	@Column(name = "BRAND")
	private String brand;

	@Column(name = "BRAND_TYPE")
	private String brandType;

	@Column(name = "PRODUCE_PLACE")
	private String producePlace;

	@Column(name = "LENGTH")
	private BigDecimal length;

	@Transient
	private String lengthStr;

	@Column(name = "WIDTH")
	private BigDecimal width;

	@Transient
	private String widthStr;

	@Column(name = "HEIGTH")
	private BigDecimal heigth;

	@Transient
	private String heigthStr;

	@Column(name = "PROD_YEAR")
	private Short prodYear;

	@Column(name = "AST_DESC")
	private String astDesc;

	@Column(name = "LICENSE_PLATE")
	private String licensePlate;

	@Column(name = "FRAME_NO")
	private String frameNo;

	@Column(name = "MACHINE_NO")
	private String machineNo;

	@Column(name = "MODEL")
	private String model;

	@Column(name = "INSUR_STATUS")
	private String insurStatus;

	@Column(name = "REG_STATUS")
	private String regStatus;

	@Column(name = "CREATE_DT")
	private Date createDt;

	@Transient
	private String createDtStr;

	@Column(name = "MAINTAIN_ID")
	private String maintainId;

	@Column(name = "CREATE_ID")
	private String createId;

	@Column(name = "LCH_UPDT")
	private Date lchUpdt;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "SUITA_AST_REG")
	private String suitaAstReg;

	@Column(name = "CODE")
	private String code;
	@JoinColumn(name = "COMPANY_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "astMachine")
	@JsonIgnore
	private List<MachineExeStepType> exeStepTypes = new ArrayList<MachineExeStepType>();

	public List<MachineExeStepType> getExeStepTypes() {
		return exeStepTypes;
	}

	public void setExeStepTypes(List<MachineExeStepType> exeStepTypes) {
		this.exeStepTypes = exeStepTypes;
	}

	public AstMachine() {
		super();
	}

	public AstMachine(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getAstName() {
		return astName;
	}

	public void setAstName(String astName) {
		this.astName = astName;
	}

	public String getAstOwner() {
		return astOwner;
	}

	public void setAstOwner(String astOwner) {
		this.astOwner = astOwner;
	}

	public String getCertiNo() {
		return certiNo;
	}

	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}

	public String getAstStatus() {
		return astStatus;
	}

	public void setAstStatus(String astStatus) {
		this.astStatus = astStatus;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandType() {
		return brandType;
	}

	public void setBrandType(String brandType) {
		this.brandType = brandType;
	}

	public String getProducePlace() {
		return producePlace;
	}

	public void setProducePlace(String producePlace) {
		this.producePlace = producePlace;
	}

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public String getLengthStr() {
		return lengthStr;
	}

	public void setLengthStr(String lengthStr) {
		this.lengthStr = lengthStr;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public String getWidthStr() {
		return widthStr;
	}

	public void setWidthStr(String widthStr) {
		this.widthStr = widthStr;
	}

	public BigDecimal getHeigth() {
		return heigth;
	}

	public void setHeigth(BigDecimal heigth) {
		this.heigth = heigth;
	}

	public String getHeigthStr() {
		return heigthStr;
	}

	public void setHeigthStr(String heigthStr) {
		this.heigthStr = heigthStr;
	}

	public Short getProdYear() {
		return prodYear;
	}

	public void setProdYear(Short prodYear) {
		this.prodYear = prodYear;
	}

	public String getAstDesc() {
		return astDesc;
	}

	public void setAstDesc(String astDesc) {
		this.astDesc = astDesc;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getInsurStatus() {
		return insurStatus;
	}

	public void setInsurStatus(String insurStatus) {
		this.insurStatus = insurStatus;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
		this.createDtStr = Formater.date2str(createDt);
	}

	public String getCreateDtStr() {
		return createDtStr;
	}

	public void setCreateDtStr(String createDtStr) throws Exception {
		this.createDtStr = createDtStr;
		if (!Formater.isNull(createDtStr))
			this.createDt = Formater.str2date(createDtStr);
	}

	public String getMaintainId() {
		return maintainId;
	}

	public void setMaintainId(String maintainId) {
		this.maintainId = maintainId;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public Date getLchUpdt() {
		return lchUpdt;
	}

	public void setLchUpdt(Date lchUpdt) {
		this.lchUpdt = lchUpdt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSuitaAstReg() {
		return suitaAstReg;
	}

	public void setSuitaAstReg(String suitaAstReg) {
		this.suitaAstReg = suitaAstReg;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Transient
	private BigDecimal waitTime;

	public BigDecimal getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(BigDecimal waitTime) {
		this.waitTime = waitTime;
	}
	@JsonIgnore
	@Transient
	private List<WorkOrder> lstWorkOrder;

	public List<WorkOrder> getLstWorkOrder() {
		return lstWorkOrder;
	}

	public void setLstWorkOrder(List<WorkOrder> lstWorkOrder) {
		this.lstWorkOrder = lstWorkOrder;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
