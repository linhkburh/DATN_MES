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
import java.util.Arrays;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "QUOTATION_ITEM")
@XmlRootElement(name = "drawing")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuotationItem extends ParentItem implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "MANAGE_CODE", nullable = false, length = 40)
	private String manageCode;
	@Column(name = "ROOT_MANAGE_CODE", length = 40)
	private String rootManageCode;
	@Column(name = "DELIVER_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deliverDate;
	@Column(name = "NAME")
	private String name;
	@JoinColumn(name = "DRAWING_TYPE", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private SysDictParam drawingType;
	@Column(name = "PREPARE_TIME")
	private Long prepareTime;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "PREPARE_COST")
	private BigDecimal prepareCost;
	@Column(name = "MASS")
	private BigDecimal mass;
	@Column(name = "SHELF_COST")
	private BigDecimal shelfCost;
	@Column(name = "OUTSOURCE_CODE")
	private BigDecimal outsourceCode;
	@Column(name = "TRANSPORT_COST")
	private BigDecimal transportCost;
	@Column(name = "TRIAL_ITEM")
	private Long trialItem;
	@Column(name = "FIRE_COST")
	private BigDecimal fireCost;
	@Column(name = "OTHER_COST")
	private BigDecimal otherCost;
	@Column(name = "GRINDING_COST")
	private BigDecimal grinDingCost;
	@Column(name = "POLISH_COST")
	private BigDecimal polishCost;
	@Column(name = "QUALITY")
	private BigDecimal quality;

	@Column(name = "EXTRA_QUATITY")
	private BigDecimal extraQuatity;
	/**
	 * Thoi gian du kien qc 1 chi tiet
	 */
	@Column(name="QC_ITM_EST_TIME")
	private Long qcItmEstTime;
	/**
	 * Thoi gian du kien nguoi 1 chi tiet
	 */
	@Column(name="PLSH_ITM_EST_TIME")
	private Long plshItmEstTime;


	public BigDecimal getExtraQuatity() {
		return extraQuatity;
	}

	public void setExtraQuatity(BigDecimal extraQuatity) {
		this.extraQuatity = extraQuatity;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "quotationItemId", fetch = FetchType.LAZY)
	private List<QuotationItemMaterial> quotationItemMaterialList = new ArrayList<QuotationItemMaterial>();
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "quotationItem", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProcessExe> lstProcessExe = new ArrayList<ProcessExe>();

	@Formula("(select nvl(sum(qcd.amount),0) from qc_os qo, qc_os_detail qcd where qo.id = qcd.qc_os_id and qcd.quotation_item_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private Long totalToOs;

	public Long getTotalToOs() {
		return totalToOs;
	}

	public void setTotalToOs(Long totalToOs) {
		this.totalToOs = totalToOs;
	}

	@JsonIgnore
	public List<ProcessExe> getLstProcessExe() {
		return lstProcessExe;
	}

	public void setLstProcessExe(List<ProcessExe> lstProcessExe) {
		this.lstProcessExe = lstProcessExe;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "quotationItemId", fetch = FetchType.LAZY)
	@JsonIgnore
	@XmlTransient
	private List<QuotationItemExe> quotationItemAllExeList = new ArrayList<QuotationItemExe>();
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "quotationItem", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("quotationItem")

	@JsonIgnore
	@XmlTransient
	private List<QuotationRepaire> lstQuotationRepaire = new ArrayList<QuotationRepaire>();

	@JsonIgnore
	public List<QuotationRepaire> getLstQuotationRepaire() {
		return lstQuotationRepaire;
	}

	public void setLstQuotationRepaire(List<QuotationRepaire> lstQuotationRepaire) {
		this.lstQuotationRepaire = lstQuotationRepaire;
	}

	@Transient
	@JsonIgnoreProperties("quotationItemId")

	@XmlTransient
	@JsonProperty
	private List<QuotationItemExe> quotationItemProList = new ArrayList<QuotationItemExe>();
	@Transient
	@JsonIgnoreProperties("quotationItemId")
	@JsonProperty
	@XmlTransient
	private List<QuotationItemExe> quotationItemExeList = new ArrayList<QuotationItemExe>();
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	// Mong muon, ky vong
	@JsonIgnoreProperties("quotationItemList")
	private Quotation quotationId;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "REDUCE")
	private BigDecimal reduce;
	@Column(name = "INIT_PRICE")
	private BigDecimal opInitPrice;
	@Column(name = "STATUS")
	private Short status;

	@Column(name = "WORK_ORDER_NUMBER")
	private String workOderNumber;

	public Short getStatus() {
		return status;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getDeliverDateStr() {
		return Formater.date2str(this.deliverDate);
	}

	public void setDeliverDateStr(String deliverDateStr) throws Exception {
		this.deliverDate = Formater.str2date(deliverDateStr);
	}

	@Formula("(select count(1) from quotation_item_exe qie, exe_step s where qie.quotation_item_id = id and qie.exe_step_id = s.id and nvl(s.program, 0) = 0)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfChildren;

	public BigDecimal getNumOfChildren() {
		if (numOfChildren == null)
			return new BigDecimal("0");
		return numOfChildren;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getOpInitPrice() {
		return opInitPrice;
	}

	public void setOpInitPrice(BigDecimal opInitPrice) {
		this.opInitPrice = opInitPrice;
	}

	public String getOpInitPriceStr() {
		return FormatNumber.num2Str(this.opInitPrice);
	}

	public void setOpInitPriceStr(String opInitPriceStr) throws ParseException {
		this.opInitPrice = FormatNumber.str2num(opInitPriceStr);
	}

	@Column(name = "TOTAL_PRICE")
	private BigDecimal totalPrice;

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "CREATOR")
	private String creator;
	@JoinColumn(name = "DEPARTMENT")
	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;
	@JoinColumn(name = "COMPANY")
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;

	@JoinColumn(name = "TECHNICAL_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysUsers technicalId;
	/**
	 * Can bo ke hoach
	 */
	@JoinColumn(name = "PLAN_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysUsers planner;
	@JoinColumn(name = "AC_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysUsers acId;

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

	public QuotationItem() {
	}

	public QuotationItem(String id) {
		this.id = id;
	}

	public QuotationItem(Quotation quotation) {
		this.quotationId = quotation;
		this.createDate = Calendar.getInstance().getTime();
		this.company = quotation.getCompany();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysDictParam getDrawingType() {
		return drawingType;
	}

	public void setDrawingType(SysDictParam drawingType) {
		this.drawingType = drawingType;
	}

	public Long getPrepareTime() {
		return prepareTime;
	}

	public void setPrepareTime(Long prepareTime) {
		this.prepareTime = prepareTime;
	}

	public BigDecimal getPrepareCost() {
		return prepareCost;
	}

	public void setPrepareCost(BigDecimal prepareCost) {
		this.prepareCost = prepareCost;
	}

	public BigDecimal getMass() {
		return mass;
	}

	public void setMass(BigDecimal mass) {
		this.mass = mass;
	}

	public BigDecimal getShelfCost() {
		return shelfCost;
	}

	public void setShelfCost(BigDecimal shelfCost) {
		this.shelfCost = shelfCost;
	}

	public BigDecimal getOutsourceCode() {
		return outsourceCode;
	}

	public void setOutsourceCode(BigDecimal outsourceCode) {
		this.outsourceCode = outsourceCode;
	}

	public BigDecimal getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(BigDecimal transportCost) {
		this.transportCost = transportCost;
	}

	public Long getTrialItem() {
		return trialItem;
	}

	public void setTrialItem(Long trialItem) {
		this.trialItem = trialItem;
	}

	public BigDecimal getFireCost() {
		return fireCost;
	}

	public void setFireCost(BigDecimal fireCost) {
		this.fireCost = fireCost;
	}

	public BigDecimal getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(BigDecimal otherCost) {
		this.otherCost = otherCost;
	}

	public BigDecimal getQuality() {
		return quality;
	}

	public void setQuality(BigDecimal quality) {
		this.quality = quality;
	}

	public void setQualityStr(String qualityStr) throws ParseException {
		this.quality = FormatNumber.str2num(qualityStr);
	}

	public String getQualityStr() {
		if (this.quality == null)
			return "0";
		return FormatNumber.num2Str(this.quality);
	}

	public List<QuotationItemMaterial> getQuotationItemMaterialList() {
		return quotationItemMaterialList;
	}

	public void setQuotationItemMaterialList(List<QuotationItemMaterial> quotationItemMaterialList) {
		this.quotationItemMaterialList = quotationItemMaterialList;
	}

	public List<QuotationItemExe> getQuotationItemAllExeList() {
		return quotationItemAllExeList;
	}

	public void setQuotationItemAllExeList(List<QuotationItemExe> quotationItemAllExeList) {
		this.quotationItemAllExeList = quotationItemAllExeList;
	}

	public List<QuotationItemExe> getQuotationItemProList() {
		return quotationItemProList;
	}

	public void setQuotationItemProList(List<QuotationItemExe> quotationItemProList) {
		this.quotationItemProList = quotationItemProList;
	}

	public List<QuotationItemExe> getQuotationItemExeList() {
		return quotationItemExeList;
	}

	public void setQuotationItemExeList(List<QuotationItemExe> quotationItemExeList) {
		this.quotationItemExeList = quotationItemExeList;
	}

	@XmlTransient
	public Quotation getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Quotation quotationId) {
		this.quotationId = quotationId;
		if (quotationId == null)
			return;
		this.company = quotationId.getCompany();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShelfCostStr() {
		return FormatNumber.num2Str(this.shelfCost);
	}

	public void setShelfCostStr(String shelfCostStr) throws ParseException {
		this.shelfCost = FormatNumber.str2num(shelfCostStr);
	}

	public String getPrepareCostStr() {
		return FormatNumber.num2Str(this.prepareCost);
	}

	public void setPrepareCostStr(String prepareCostStr) throws ParseException {
		this.prepareCost = FormatNumber.str2num(prepareCostStr);
	}

	public String getOutsourceCodeStr() {
		return FormatNumber.num2Str(this.outsourceCode);
	}

	public void setOutsourceCodeStr(String outsourceCodeStr) throws ParseException {
		this.outsourceCode = FormatNumber.str2num(outsourceCodeStr);
	}

	public String getTransportCostStr() {
		return FormatNumber.num2Str(this.transportCost);
	}

	public void setTransportCostStr(String transportCostStr) throws ParseException {
		this.transportCost = FormatNumber.str2num(transportCostStr);
	}

	public String getFireCostStr() {
		return FormatNumber.num2Str(this.fireCost);
	}

	public void setFireCostStr(String fireCostStr) throws ParseException {
		this.fireCost = FormatNumber.str2num(fireCostStr);
	}

	public String getOtherCostStr() {
		return FormatNumber.num2Str(this.otherCost);
	}

	public void setOtherCostStr(String otherCostStr) throws ParseException {
		this.otherCost = FormatNumber.str2num(otherCostStr);
	}

	public BigDecimal calulatePrice() {
		this.price = new BigDecimal(0);
		if (this.getMaterialPrice() != null)
			this.price = this.price.add(this.getMaterialPrice());
		if (this.fireCost != null)
			this.price = this.price.add(fireCost);

		if (this.mass != null)
			this.price = this.price.add(mass);

		if (this.outsourceCode != null)
			this.price = this.price.add(outsourceCode);

		if (this.prepareCost != null)
			this.price = this.price.add(prepareCost);

		if (this.shelfCost != null)
			this.price = this.price.add(shelfCost);

		if (this.transportCost != null)
			this.price = this.price.add(transportCost);

		if (this.otherCost != null)
			this.price = this.price.add(otherCost);
		if (this.polishCost != null)
			this.price = this.price.add(polishCost);
		if (this.grinDingCost != null)
			this.price = this.price.add(grinDingCost);
		BigDecimal program = new BigDecimal(0);
		for (QuotationItemExe itemExe : this.quotationItemAllExeList) {
			BigDecimal itemExePrice = itemExe.getPrice();
			if (itemExePrice != null) {
				// Khong phai lap trinh
				if (!Boolean.TRUE.equals(itemExe.getExeStepId().getProgram()))
					this.price = this.price.add(itemExePrice);
				else {
					program = program.add(itemExePrice);
				}
			}
		}
		// Nhan voi so luong
		if (this.price != null && this.quality != null)
			this.totalPrice = this.price.multiply(this.quality);
		// Chi phi san xuat + lap trinh
		this.totalPrice = this.totalPrice.add(program);
		return this.totalPrice;
	}

	@Transient
	private BigDecimal getMaterialPrice() {
		BigDecimal mPrice = new BigDecimal(0);
		for (QuotationItemMaterial materialRls : this.quotationItemMaterialList) {
			BigDecimal materialPrice = materialRls.calulatePrice();
			if (materialPrice != null)
				mPrice = mPrice.add(materialPrice);
		}
		return mPrice;

	}

	@Transient
	private BigDecimal stepPrice;

	public BigDecimal getStepPrice() {
		if (this.stepPrice == null) {
			if (Formater.isNull(this.quotationItemAllExeList))
				this.stepPrice = null;
			else {
				BigDecimal stepPrice = new BigDecimal(0);
				for (QuotationItemExe itemExe : this.quotationItemAllExeList) {
					BigDecimal itemExePrice = itemExe.getPrice();
					if (itemExePrice != null) {
						// Khong phai lap trinh, can nhan voi so luong
						if (!Boolean.TRUE.equals(itemExe.getExeStepId().getProgram())) {
							itemExePrice = itemExePrice.multiply(itemExe.getQuotationItemId().getQuality());
						}
						stepPrice = stepPrice.add(itemExePrice);
					}

				}
				this.stepPrice = stepPrice;
			}
		}
		return this.stepPrice;
	}

	@Transient
	public String getStepPriceStr() {
		return FormatNumber.num2Str(this.stepPrice);

	}

	public BigDecimal getGrinDingCost() {
		return grinDingCost;
	}

	public void setGrinDingCost(BigDecimal grinDingCost) {
		this.grinDingCost = grinDingCost;
	}

	public String getGrinDingCostStr() {
		return FormatNumber.num2Str(this.grinDingCost);
	}

	public void setGrinDingCost(String grinDingCostStr) throws ParseException {
		this.grinDingCost = FormatNumber.str2num(grinDingCostStr);
	}

	public BigDecimal getPolishCost() {
		return polishCost;
	}

	public void setPolishCost(BigDecimal polishCost) {
		this.polishCost = polishCost;
	}

	public String getPolishCostStr() {
		return FormatNumber.num2Str(this.polishCost);
	}

	public void setPolishCostStr(String polishCostStr) throws ParseException {
		this.polishCost = FormatNumber.str2num(polishCostStr);
	}

	@Transient
	public String getPriceStr() {
		return FormatNumber.num2Str(this.price);
	}

	@Transient
	public String getTotalPriceStr() {
		return FormatNumber.num2Str(this.totalPrice);
	}

	@Column(name = "SURFACE_TREATMENT")
	private String surfaceTreatment;

	public String getSurfaceTreatment() {
		return surfaceTreatment;
	}

	public void setSurfaceTreatment(String surfaceTreatment) {
		this.surfaceTreatment = surfaceTreatment;
	}

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		if (Formater.isNull(manageCode))
			this.manageCode = null;
		else
			this.manageCode = manageCode.trim();
	}

	public BigDecimal getReduce() {
		return reduce;
	}

	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}

	public String getReduceStr() {
		return FormatNumber.num2Str(this.reduce);
	}

	public void setReduceStr(String reduceStr) throws ParseException {
		this.reduce = FormatNumber.str2num(reduceStr);
	}

	@Formula("MES_EXE.numOfFinishChildren('qi',id)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfFinishChildren;

	public BigDecimal getNumOfFinishChildren() {
		if (numOfFinishChildren == null)
			return new BigDecimal("0");
		return numOfFinishChildren;
	}

	@Transient
	private BigDecimal finalOpUnitPrice;

	public BigDecimal getFinalOpUnitPrice() {
		return finalOpUnitPrice;
	}

	public void setFinalOpUnitPrice(BigDecimal finalOpUnitPrice) {
		this.finalOpUnitPrice = finalOpUnitPrice;
	}

	@Override
	@JsonIgnore
	public List<? extends ParentItem> getLstChildren() {
		if (!Formater.isNull(this.quotationItemExeList))
			return this.quotationItemExeList;
		for (QuotationItemExe qie : this.quotationItemAllExeList) {
			if (this.quotationItemExeList == null)
				this.quotationItemExeList = new ArrayList<QuotationItemExe>(Arrays.asList(qie));
			else
				this.quotationItemExeList.add(qie);
		}
		return this.quotationItemExeList;
	}

	public SysUsers getTechnicalId() {
		return technicalId;
	}

	public void setTechnicalId(SysUsers technicalId) {
		this.technicalId = technicalId;
	}

	public SysUsers getAcId() {
		return acId;
	}

	public void setAcId(SysUsers acId) {
		this.acId = acId;
	}

	public String getWorkOderNumber() {
		return workOderNumber;
	}

	public void setWorkOderNumber(String workOderNumber) {
		this.workOderNumber = workOderNumber;
	}

	@Transient
	@JsonIgnore
	private List<AstMachine> lstAsignedMachine;

	public List<AstMachine> getLstAsignedMachine() {
		return lstAsignedMachine;
	}

	public void setLstAsignedMachine(List<AstMachine> lstAsignedMachine) {
		this.lstAsignedMachine = lstAsignedMachine;
	}

	@Transient
	private String trangThaiBanVe, trangThaiSX;

	public String getTrangThaiBanVe() {
		return trangThaiBanVe;
	}

	public void setTrangThaiBanVe(String trangThaiBanVe) {
		this.trangThaiBanVe = trangThaiBanVe;
	}

	public String getTrangThaiSX() {
		return trangThaiSX;
	}

	public void setTrangThaiSX(String trangThaiSX) {
		this.trangThaiSX = trangThaiSX;
	}

	/**
	 * Truong luu du thua, phuc vu tim kiem nhanh
	 */
	@Column(name = "NUM_OF_FINISH_ITEM")
	private BigDecimal nmOfFnsItem;

	public BigDecimal getNmOfFnsItem() {
		return nmOfFnsItem;
	}

	public void setNmOfFnsItem(BigDecimal nmOfFnsItem) {
		this.nmOfFnsItem = nmOfFnsItem;
	}

	@Column(name = "SETUP_TIME")
	private BigDecimal bookingSetupTime;

	public BigDecimal getBookingSetupTime() {
		return bookingSetupTime;
	}

	public void setBookingSetupTime(BigDecimal bookingSetupTime) {
		this.bookingSetupTime = bookingSetupTime;
	}

	public String getBookingSetupTimeStr() {
		return FormatNumber.num2Str(this.bookingSetupTime);
	}

	public void setBookingSetupTimeStr(String bookingSetupTimeStr) throws ParseException {
		this.bookingSetupTime = FormatNumber.str2num(bookingSetupTimeStr);
	}

	@Override
	@Transient
	public BigDecimal getAmount() {
		return this.getQuality();
	}

	@Transient
	private List<ProductionLine> lstProductionLine;

	public List<ProductionLine> getLstProductionLine() {
		return lstProductionLine;
	}

	public void setLstProductionLine(List<ProductionLine> lstProductionLine) {
		this.lstProductionLine = lstProductionLine;
	}

	@Transient
	private String slowHours, slowPercent, remainingAmount;

	@JsonIgnore
	public String getSlowHours() {
		return Formater.num2str(getExeTime().subtract(getEstTime()));
	}

	@JsonIgnore
	public String getSlowPercent() {
		BigDecimal cham = getExeTime().subtract(getEstTime());
		if (cham.compareTo(new BigDecimal(0)) > 0) {
			if (getEstTime().compareTo(new BigDecimal(0)) == 0)
				return "";
			return Formater.num2str(cham.divide(getEstTime(), 3, RoundingMode.HALF_UP));
		} else {
			if (getEstTime() == null || getEstTime().compareTo(new BigDecimal(0)) == 0)
				return "0";
			else
				return Formater.num2str(cham.divide(getEstTime(), 3, RoundingMode.HALF_UP));
		}
	}

	@Override
	public BigDecimal getNumOfFinishItem() {
		if (nmOfFnsItem == null)
			return new BigDecimal(0);
		return nmOfFnsItem;
	}

	@JsonIgnore
	public String getRemainingAmount() {
		if (nmOfFnsItem == null)
			return FormatNumber.num2Str(getQuality());
		else
			return FormatNumber.num2Str(getQuality().subtract(nmOfFnsItem));
	}

	/**
	 * Group LSX theo day chuyen
	 * 
	 * @param qi
	 * @throws ResourceException
	 */
	public void groupWoByProductLine() throws ResourceException {
		for (QuotationItemExe qie : this.getQuotationItemAllExeList()) {
			if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
				continue;
			for (WorkOrder wo : qie.getWorkOrders())
				wo.addToProductLine();
		}
		this.setupTimeAvg = this.bookingSetupTime == null || this.lstProductionLine == null ? null
				: this.bookingSetupTime.divide(new BigDecimal(this.lstProductionLine.size()), 3, RoundingMode.HALF_UP);
	}

	@Transient
	public BigDecimal setupTimeAvg;

	public String getSetupTimeAvgStr() {
		return FormatNumber.num2Str(this.setupTimeAvg);
	}

	/**
	 * Thoi gian gia cong 1 chi tiet, tinh theo phut, kg tinh thoi gian lap trinh
	 */

	@JsonIgnore
	public BigDecimal getItemEstTime() {
		BigDecimal itemEstTime = new BigDecimal(0);
		for (QuotationItemExe itemExe : this.quotationItemAllExeList) {
			// Bo qua lap trinh
			if (Boolean.TRUE.equals(itemExe.getExeStepId().getProgram()))
				continue;
			if (itemExe.getUnitExeTime() == null)
				continue;
			itemEstTime = itemEstTime.add(itemExe.getUnitExeTime());
		}
		return itemEstTime;
	}

	@Transient
	@XmlTransient
	private List<WorkOrder> workOrders = new ArrayList<WorkOrder>();

	@Transient
	@XmlTransient
	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public void setWorkOrders(List<WorkOrder> workOrders) {
		this.workOrders = workOrders;
	}

	/**
	 * So luong chuyen nguoi
	 */
	@Formula("(select nvl(sum(qip.quality),0) from quotation_item_process qip where qip.quotation_item_id = id and qip.type='CL')")
	@Basic(fetch = FetchType.LAZY)
	private Long polishingQuatity;
	/**
	 * So luong nguoi da hoan thanh
	 */
	@Formula("(select nvl(sum(pe.total_amount),0) from process_exe pe where pe.quotation_item_id = id and pe.type='CL')")
	@Basic(fetch = FetchType.LAZY)
	private Long polishingDone;

	/**
	 * So luong nguoi da repaire
	 */
	@Formula("(select nvl(sum(pe.total_amount),0) from process_exe pe where pe.quotation_item_id = id and pe.type='CL' and pe.REPAIRE_NG=1)")
	@Basic(fetch = FetchType.LAZY)
	private Long polishingRepaireDone;

	/**
	 * So luong NG phat sinh cua nguoi
	 */
	@Formula("(select nvl(sum(pe.NG_AMOUNT),0) from process_exe pe where pe.quotation_item_id = id and pe.type='CL' and nvl(pe.REPAIRE_NG,0)=0)")
	@Basic(fetch = FetchType.LAZY)
	private Long polishingRepaire;

	/**
	 * So luong chuyen qc
	 */
	@Formula("(select nvl(sum(qip.quality),0) from quotation_item_process qip where qip.quotation_item_id = id and qip.type='QC')")
	@Basic(fetch = FetchType.LAZY)
	private Long qcQuatity;
	/**
	 * So luong qc da hoan thanh
	 */
	@Formula("(select nvl(sum(pe.total_amount),0) from process_exe pe where pe.quotation_item_id = id and pe.type='QC')")
	@Basic(fetch = FetchType.LAZY)
	private Long qcDone;

	public Long getPolishingRepaire() {
		return polishingRepaire;
	}

	public Long getPolishingRepaireDone() {
		return polishingRepaireDone;
	}

	public Long getPolishingQuatity() {
		return polishingQuatity;
	}

	public Long getPolishingDone() {
		return polishingDone;
	}

	public Long getQcQuatity() {
		return qcQuatity;
	}

	public Long getQcDone() {
		return qcDone;
	}

	public Long getQcPendding() {
		return qcQuatity = qcDone;
	}

	public Long getPolishingPendding() {
		return polishingQuatity = polishingDone;
	}

	/**
	 * So luong chi tiet can sua, qc chuyen lai san xuat tai cong doan qc
	 */
	@Formula("(select nvl(sum(i.ng_amount), 0)  from qc_in i where i.quotation_item_id = id) + (select nvl(sum(pe.RP_AMOUNT), 0) from process_exe pe where pe.quotation_item_id = id and pe.type = 'QC')")
	@Basic(fetch = FetchType.LAZY)
	private Long qcRepaire;

	public Long getQcRepaire() {
		return qcRepaire;
	}

	/**
	 * So luong sx da sua thanh cong
	 */
	@Formula("(select nvl(sum(qr.amount),0) from QUOTATION_REPAIRE qr where qr.quotation_item_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private Long repairedAmount;

	public Long getRepairedAmount() {
		return repairedAmount;
	}

	/**
	 * So luong chi tiet sx da thuc hien sua
	 */
	@Formula("(select nvl(sum(qr.total_amount),0) from QUOTATION_REPAIRE qr where qr.quotation_item_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private Long repairingAmount;

	public Long getRepairingAmount() {
		return repairingAmount;
	}

	@XmlElement(name = "qrContent")
	@JsonIgnore
	public String getQrContent() {
		String qrCode = this.quotationId.getCode() + "|" + this.quotationId.getCustomer().getOrgName() + "|" + this.code
				+ "|" + (Formater.isNull(this.rootManageCode)?this.manageCode:this.rootManageCode) + "|" + Formater.date2str(this.deliverDate) + "|"
				+ FormatNumber.num2Str(qcRepaire);
		return qrCode;
	}

	@Transient
	private long qcOk;
	@Transient
	private long polishingOk;
	@Transient
	private long polishingNg;

	public long getQcOk() {
		return qcOk;
	}

	public void setQcOk(long qcOk) {
		this.qcOk = qcOk;
	}

	public long getPolishingOk() {
		return polishingOk;
	}

	public void setPolishingOk(long polishingOk) {
		this.polishingOk = polishingOk;
	}

	public long getPolishingNg() {
		return polishingNg;
	}

	public void setPolishingNg(long polishingNg) {
		this.polishingNg = polishingNg;
	}

	@Transient
	private BigDecimal itemTime;

	public BigDecimal getItemTime() {
		return itemTime;
	}

	public void sumary() {
		if (isSumary())
			return;
		super.sumary();
		for (ProcessExe pex : lstProcessExe) {
			if ("QC".equals(pex.getType())) {
				qcOk += pex.getAmount().longValue();
			} else if ("CL".equals(pex.getType())) {
				polishingOk += pex.getAmount().longValue();
				if (pex.getBrokenAmount() != null)
					polishingNg += pex.getBrokenAmount();
				polishingDestroyAmout += pex.getBrokenAmount() != null ? pex.getBrokenAmount() : 0;
			}
		}

		for (QuotationItemExe qie : this.quotationItemAllExeList) {
			if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
				continue;
			if (itemTime == null)
				itemTime = qie.getUnitExeTime();
			else if (qie.getUnitExeTime() != null)
				itemTime = itemTime.add(qie.getUnitExeTime());
		}
		if (itemTime != null)
			itemTime = itemTime.divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP);
	}

	@Formula("(select sum(nvl(pe.NG_AMOUNT,0)) from process_exe pe where pe.quotation_item_id = id) + (select nvl(sum(i.broken_amount),0) from qc_in_detail i, qc_os_detail o where i.qc_os_detail_id = o.id and o.quotation_item_id= id)")
	/**
	 * So luong bi huy boi Qc (giai doan QC + giai doan GCN)
	 */
	@Basic(fetch = FetchType.LAZY)
	private Long qcDestroyAmout;

	public Long getQcDestroyAmout() {
		return qcDestroyAmout;
	}
	@Formula("(select sum(nvl(pe.RP_AMOUNT,0)) from process_exe pe where pe.quotation_item_id = id) + (select nvl(sum(i.ng_amount),0) from qc_in_detail i, qc_os_detail o where i.qc_os_detail_id = o.id and o.quotation_item_id= id)")
	@Basic(fetch = FetchType.LAZY)
	private Long qcNgAmount;
	@Override
	public Long getQcNgAmount() {
		return qcNgAmount;
	}

	@Transient
	/**
	 * So luong bi huy trong giai doan nguoi
	 */
	private long polishingDestroyAmout;

	public long getPolishingDestroyAmout() {
		sumary();
		return polishingDestroyAmout;
	}

	/**
	 * Hang le, hang lo
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SINGGLE")
	private SysDictParam siggle;

	public SysDictParam getSiggle() {
		return siggle;
	}

	public void setSiggle(SysDictParam siggle) {
		this.siggle = siggle;
	}

	public SysUsers getPlanner() {
		return planner;
	}

	public void setPlanner(SysUsers planner) {
		this.planner = planner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FACTORY_UNIT")
	private BssFactoryUnit factoryUnit;

	public BssFactoryUnit getFactoryUnit() {
		return factoryUnit;
	}

	public void setFactoryUnit(BssFactoryUnit factoryUnit) {
		this.factoryUnit = factoryUnit;
	}

	@Transient
	private String qcode;

	public String getQcode() {
		return qcode;
	}

	public void setQcode(String qcode) {
		this.qcode = qcode;
	}

	/**
	 * Da chuyen doi tac TP
	 */
	@Formula("(select nvl(sum(d.amount),0) from qc_os_detail d where d.quotation_item_id = id and d.WORK_ORDER_ID is null)")
	@Basic(fetch = FetchType.LAZY)
	private long toPartnerAmount;
	/**
	 * Doi tac da tra hang
	 */
	@Formula("(select nvl(sum(qii.total_amount),0) from qc_in qii where qii.quotation_item_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private long partnerDone;

	public long getToPartnerAmount() {
		return toPartnerAmount;
	}

	public long getPartnerDone() {
		return partnerDone;
	}

	public Long getQcItmEstTime() {
		return qcItmEstTime;
	}

	public void setQcItmEstTime(Long qcItmEstTime) {
		this.qcItmEstTime = qcItmEstTime;
	}

	public Long getPlshItmEstTime() {
		return plshItmEstTime;
	}

	public void setPlshItmEstTime(Long plshItmEstTime) {
		this.plshItmEstTime = plshItmEstTime;
	}

	public String getRootManageCode() {
		return rootManageCode;
	}

	public void setRootManageCode(String rootManageCode) {
		this.rootManageCode = rootManageCode;
	}
	@Transient
	@JsonIgnore
	@XmlTransient
	public Long getTodoRepaire() {
		if(repairedAmount==null)
			return qcRepaire;
		return qcRepaire-repairedAmount;
	}
	
	
}
