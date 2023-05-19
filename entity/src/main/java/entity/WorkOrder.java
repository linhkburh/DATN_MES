package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysUsers;

@Entity
@Table(name = "WORK_ORDER")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkOrder extends ParentItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "QUOTATION_ITEM_EXE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("workOrders")
	private QuotationItemExe quotationItemExe;

	@JoinColumn(name = "MACHINE_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private AstMachine astMachine;
	@JoinColumn(name = "CREATOR", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private SysUsers creator;

	@JsonIgnoreProperties("workOrderId")
	@OneToMany(mappedBy = "workOrderId", fetch = FetchType.LAZY)
	private List<WorkOrderExe> workOrderExes = new ArrayList<WorkOrderExe>();

	@OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnore
	private List<QcChkOutSrcDetail> qcChkOutSrcDetails = new ArrayList<QcChkOutSrcDetail>();
	@OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnore
	/**
	 * Lich su xu ly gia cong ngoai
	 */
	private List<QcChkOutSrc> lstQcChkOutSrc = new ArrayList<QcChkOutSrc>();

	public List<QcChkOutSrc> getLstQcChkOutSrc() {
		return lstQcChkOutSrc;
	}

	public void setLstQcChkOutSrc(List<QcChkOutSrc> lstQcChkOutSrc) {
		this.lstQcChkOutSrc = lstQcChkOutSrc;
	}

	@Override
	public BigDecimal getNumOfChildren() {
		return this.amount;
	}

	@JsonIgnore
	@Transient
	private List<WorkOrder> lstWorkOrder = new ArrayList<WorkOrder>();

	@Column(name = "CODE")
	private String code;

	@Column(name = "FROM_ITEM")
	private BigDecimal fromItem;

	@Column(name = "TO_ITEM")
	private BigDecimal toItem;

	@Column(name = "AMOUNT")
	private BigDecimal amount;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "STATUS")
	private Short status;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SysUsers getCreator() {
		return creator;
	}

	public void setCreator(SysUsers creator) {
		this.creator = creator;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Transient
	private String amountStr;

	@Transient
	public String getTotalTimeDelay() {
		if (this.astMachine == null || this.getTotalEstTime() == null)
			return null;
		return FormatNumber.num2Str(this.waitTime.add(this.getTotalEstTime()));
	}

	@Column(name = "WAIT_TIME")
	private BigDecimal waitTime;

	public BigDecimal getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(BigDecimal waitTime) {
		this.waitTime = waitTime;
	}

	@Transient
	public String getWaitTimeStr() {
		return FormatNumber.num2Str(this.waitTime);
	}

	public void setWaitTimeStr(String waitTimeStr) throws ParseException {
		this.waitTime = FormatNumber.str2num(waitTimeStr);
	}

	public WorkOrder(String id) {
		this.id = id;
	}

	public WorkOrder() {
		super();
	}

	public WorkOrder(QuotationItemExe quotationItemExe, AstMachine astMachine) {
		this.quotationItemExe = quotationItemExe;
		this.astMachine = astMachine;
		this.amount = new BigDecimal("0");
		this.code = quotationItemExe.getCode() + "-" + astMachine.getCode();
		// Ma day chuyen bang ma = ma ban ve + ma may, do he thong tu chon
		this.productionLineId = this.getQuotationItemExe().getQuotationItemId().getManageCode() + "-"
				+ astMachine.getCode();
		if (this.astMachine.getWaitTime() == null)
			this.waitTime = new BigDecimal(0);
		else
			this.waitTime = this.astMachine.getWaitTime().divide(new BigDecimal(60), 3, RoundingMode.FLOOR);
	}

	public WorkOrder(QuotationItemExe quotationItemExe, AstMachine astMachine, int productionLineIdx) {
		this.quotationItemExe = quotationItemExe;
		this.astMachine = astMachine;
		this.amount = new BigDecimal("0");
		if (productionLineIdx < 10)
			this.code = quotationItemExe.getCode() + "-" + "0" + productionLineIdx;
		else
			this.code = quotationItemExe.getCode() + "-" + productionLineIdx;
		// Ma day chuyen bang ma = ma ban ve + ma may, do he thong tu chon
		if (productionLineIdx < 10)
			this.productionLineId = this.getQi().getManageCode() + "-" + "0" + productionLineIdx;
		else
			this.productionLineId = this.getQi().getManageCode() + "-" + productionLineIdx;
		if (this.astMachine.getWaitTime() == null)
			this.waitTime = new BigDecimal(0);
		else
			this.waitTime = this.astMachine.getWaitTime().divide(new BigDecimal(60), 3, RoundingMode.FLOOR);
	}

	public String getId() {
		if (Formater.isNull(id))
			id = null;
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public QuotationItemExe getQuotationItemExe() {
		return quotationItemExe;
	}

	public void setQuotationItemExe(QuotationItemExe quotationItemExe) {
		this.quotationItemExe = quotationItemExe;
	}

	public AstMachine getAstMachine() {
		return astMachine;
	}

	public void setAstMachine(AstMachine astMachine) {
		this.astMachine = astMachine;
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
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
		this.amountStr = FormatNumber.num2Str(amount);
	}

	public String getAmountStr() {
		return FormatNumber.num2Str(this.amount);
	}

	public void setAmountStr(String amountStr) throws ParseException {
		this.amountStr = amountStr;
		this.amount = FormatNumber.str2num(amountStr);

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<WorkOrderExe> getWorkOrderExes() {
		return workOrderExes;
	}

	public void setWorkOrderExes(List<WorkOrderExe> workOrderExes) {
		this.workOrderExes = workOrderExes;
	}

	public List<WorkOrder> getLstWorkOrder() {
		return lstWorkOrder;
	}

	public void setLstWorkOrder(List<WorkOrder> lstWorkOrder) {
		this.lstWorkOrder = lstWorkOrder;
	}

	@Transient
	private Short workSts;

	@Transient
	@Override
	@JsonIgnore
	public Short getWorkSts() {
		if (workSts != null)
			return workSts;
		if (numOfFinishChildren.doubleValue() == 0)
			workSts = WORK_STS_PENDDING;
		else if (numOfFinishChildren.longValue() == amount.longValue())
			workSts = WORK_STS_FINISH;
		else {
			workSts = WORK_STS_DOING;
		}
		return workSts;
	}

	/**
	 * So luong da hoan thanh
	 */
	@Formula("MES_EXE.numOfFinishChildren('wo',id)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal numOfFinishChildren;

	public BigDecimal getNumOfFinishChildren() {
		return numOfFinishChildren;
	}

	@Transient
	public String getNextItemStr() {
		if (this.numOfFinishChildren != null)
			return FormatNumber.num2Str(this.numOfFinishChildren.add(new BigDecimal("1")));
		return "1";
	}

	@Override
	public List<? extends ParentItem> getLstChildren() {
		return this.workOrderExes;
	}

	@XmlElement(name = "qrContent")
	public String getQrContent() {
		return this.code;
	}

	/**
	 * Id day chuyen san xuat, = code trong truong hop lsx gen ra kg duoc customize may khac
	 */
	@Column(name = "PRODUCTION_LINE_ID")
	private String productionLineId;

	public String getProductionLineId() {
		return productionLineId;
	}

	public void setProductionLineId(String productionLineId) {
		this.productionLineId = productionLineId;
	}

	@Override
	@Transient
	public BigDecimal getTotalEstTime() {
		if (this.quotationItemExe.getUnitExeTime() == null)
			return null;
		return this.amount.multiply(this.quotationItemExe.getUnitExeTime()).divide(new BigDecimal(60), 3,
				RoundingMode.HALF_UP);
	}

	public void addToProductLine() {
		QuotationItem qi = this.getQi();
		if (qi.getLstProductionLine() == null) {
			qi.setLstProductionLine(new ArrayList<ProductionLine>(Arrays.asList(new ProductionLine(this))));
			return;
		}
		// ProductLine da ton tai
		for (ProductionLine pl : qi.getLstProductionLine()) {
			if (pl.getProductionLineId().equals(this.getProductionLineId())) {
				pl.getLstWorkOrder().add(this);
				return;
			}

		}
		// ProductLine chua ton tai, them product line moi
		qi.getLstProductionLine().add(new ProductionLine(this));
	}

	@Transient
	private String planTime, slowHours, slowPercent, remainingAmount, remainingTime;

	public String getPlanTime() {
		if (this.quotationItemExe.getUnitExeTime() == null)
			return null;
		return Formater.num2str(this.amount.multiply(this.quotationItemExe.getUnitExeTime()).divide(new BigDecimal(60),
				3, RoundingMode.HALF_UP));
	}

	public String getSlowHours() {
		return Formater.num2str(getExeTime().subtract(getEstTime()));
	}

	public String getSlowPercent() {
		BigDecimal cham = getExeTime().subtract(getEstTime());
		if (cham.compareTo(new BigDecimal(0)) > 0) {
			if (getEstTime().compareTo(new BigDecimal(0)) == 0)
				return "0";
			return Formater.num2str(cham.divide(getEstTime(), 3, RoundingMode.HALF_UP));
		} else {
			if (getEstTime() == null || getEstTime().compareTo(new BigDecimal(0)) == 0)
				return "0";
			else
				return Formater.num2str(cham.divide(getEstTime(), 3, RoundingMode.HALF_UP));
		}
	}

	public String getRemainingAmount() {
		if (getNumOfFinishItem() == null)
			return Formater.num2str(getAmount());
		else
			return FormatNumber.num2Str(getAmount().subtract(getNumOfFinishItem()));
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public void setSlowHours(String slowHours) {
		this.slowHours = slowHours;
	}

	public void setSlowPercent(String slowPercent) {
		this.slowPercent = slowPercent;
	}

	public void setRemainingAmount(String remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}

	@Formula("MES_EXE.getReadyQcAmount(id)")
	/**
	 * So luong san sang chuyen QC (= min cac op truoc - so luong da chuyen sang QC)
	 */
	@Basic(fetch = FetchType.LAZY)
	private long readyOsAmount;

	@JsonIgnore
	public long getReadyOsAmount() {
		return readyOsAmount;
	}

	/**
	 * So luong da chuyen gia cong ngoai (pass qua kiem tra cua qc nhung chua thuc hien chuyen GCN)
	 */
	@Formula("(select nvl(sum(s.amount),0) from qc_chk_out_src s where s.work_order_id=id)")
	@Basic(fetch = FetchType.LAZY)
	private long toOsedAmount;

	public long getToOsedAmount() {
		return toOsedAmount;
	}

	/**
	 * So luong da chuyen gia cong ngoai (pass qua kiem tra cua qc)
	 */
	@Formula("(select nvl(sum(s.amount),0) from qc_chk_out_src s where s.work_order_id=id)")
	@Basic(fetch = FetchType.LAZY)
	private long qcChkAmount;

	@JsonIgnore
	public QuotationItem getQi() {
		return this.quotationItemExe.getQuotationItemId();
	}

	/**
	 * Kiem tra LSX co thuoc production line xu ly hang huy
	 * 
	 * @return
	 */
	public boolean isRecoverProductionLine() {
		if (this.quotationItemExe == null)
			return false;
		// Khong ton tai hang huy
		if ((this.getQi().getQcDestroyAmout() == null || this.getQi().getQcDestroyAmout() == 0)
				&& this.getQi().getPolishingDestroyAmout() == 0)
			return false;
		// San xuat vuot qua so luong
		if (this.numOfFinishChildren != null) {
			if (this.numOfFinishChildren.compareTo(this.amount) > 0)
				return true;
			for (WorkOrder wo : this.getQi().getWorkOrders()) {
				// Ton tai LSX khac sx vuot qua so luong
				if (this.numOfFinishChildren.compareTo(wo.amount) > 0) {
					// Neu cung production line
					if (wo.productionLineId.equals(this.productionLineId))
						return true;
					return false;
				}
			}
		}

		// LSX dau tien sx vuot qua
		return true;
	}

	public List<QcChkOutSrcDetail> getQcChkOutSrcDetails() {
		return qcChkOutSrcDetails;
	}

	public void setQcChkOutSrcDetails(List<QcChkOutSrcDetail> qcChkOutSrcDetails) {
		this.qcChkOutSrcDetails = qcChkOutSrcDetails;
	}

	@Transient
	/**
	 * NG QC-SX
	 */
	private Long qcNgAmount = 0l;

	@Override
	public Long getQcNgAmount() {
		return qcNgAmount;
	}

	public void setQcNgAmount(Long qcNgAmount) {
		this.qcNgAmount = qcNgAmount;
	}

	@Transient
	/**
	 * Huy QC-SX
	 */
	@JsonIgnore
	private long qcBrokenAmount = 0;

	public long getQcBrokenAmount() {
		sumary();
		return qcBrokenAmount;
	}

	@JsonIgnore
	public String getQcBrokenAmountStr() {
		return FormatNumber.num2Str(getQcBrokenAmount());
	}

	@Transient
	private double maxTotalAmount = 0d;

	@JsonIgnore
	public double getMaxTotalAmount() {
		sumary();
		return maxTotalAmount;
	}

	@Override
	public void sumary() {
		if (isSumary())
			return;
		super.sumary();
		for (QcChkOutSrcDetail o : qcChkOutSrcDetails) {
			if (o.getNgAmount() != null)
				this.qcNgAmount += o.getNgAmount();
			if (o.getBrokenAmount() != null)
				this.qcBrokenAmount += o.getBrokenAmount();
		}
		if (this.amount != null)
			maxTotalAmount += this.amount.doubleValue();
		totalExeAmount = caculateTotalExeAmount();
		if (totalExeAmount != null)
			maxTotalAmount -= totalExeAmount.doubleValue();
		if (this.getBrokenAmount() != null)
			maxTotalAmount += this.getBrokenAmount();
		maxTotalAmount += qcBrokenAmount;
		maxTotalAmount += this.getQcDestroyAmout();
	}

	/**
	 * So luong da san xuat (bo qua sua NG cua sx)
	 * 
	 * @return
	 */
	@JsonIgnore
	public BigDecimal getTotalExeAmount() {
		sumary();
		return totalExeAmount;
	}

	@Transient
	/**
	 * Tong so chi tiet da thuc hien
	 */
	private BigDecimal totalExeAmount;

	private BigDecimal caculateTotalExeAmount() {
		BigDecimal totalExeAmount = new BigDecimal(0);
		for (WorkOrderExe woe : this.workOrderExes) {
			if (woe.getTotalAmount() == null)
				continue;
			if (Boolean.TRUE.equals(woe.getNgRepaire()))
				continue;
			if (woe.getTotalAmount() != null)
				totalExeAmount = totalExeAmount.add(woe.getTotalAmount());

		}
		return totalExeAmount;
	}

	/**
	 * So luong huy boi QC (sau sx)
	 * 
	 * @return
	 */
	public long getQcDestroyAmout() {
		if (isRecoverProductionLine())
			return this.getQi().getQcDestroyAmout();
		return 0;

	}

	/**
	 * So luong huy boi QC (sau sx)
	 * 
	 * @return
	 */
	public long getPolishingDestroyAmout() {
		if (isRecoverProductionLine())
			return this.getQi().getPolishingDestroyAmout();
		return 0;

	}

	public String getMaxTotalAmountStr() {
		return FormatNumber.num2Str(new BigDecimal(this.getMaxTotalAmount()));
	}

	/**
	 * So luong ban dau tru so luong da hoan thanh
	 * 
	 * @return
	 */
	public BigDecimal getOutStandingAmount() {
		if (getNumOfFinishItem() == null)
			return getAmount();
		return getAmount().subtract(getNumOfFinishItem());
	}

	public String getOutStandingAmountStr() {
		return FormatNumber.num2Str(getOutStandingAmount());
	}

	/**
	 * So luong chi tiet con phai thuc hien = so luong chi tiet chua thuc hien + so luong chi tiet huy
	 * 
	 * @return
	 */
	public BigDecimal getTodoAmount() {
		BigDecimal todoAmount = this.amount;
		// So luong hoan thanh (ca sua NG va sx moi)
		if (getNumOfFinishItem() != null)
			todoAmount = todoAmount.subtract(getNumOfFinishItem());
		// Tong so NG (da sua + chua sua)
		if (getNgAmount() != null)
			todoAmount = todoAmount.subtract(new BigDecimal(getNgAmount()));
		// So luong da sua
		todoAmount = todoAmount.add(new BigDecimal(getRepairedNgAmount()));
		// QC huy trong san xuat
		todoAmount = todoAmount.add(new BigDecimal(getQcBrokenAmount()));
		// QC huy trong giai doan QC hoac gia cong ngoai
		todoAmount = todoAmount.add(new BigDecimal(getQcDestroyAmout()));
		// Huy trong g/d nguoi
		todoAmount = todoAmount.add(new BigDecimal(getPolishingDestroyAmout()));
		return todoAmount;
	}

	public BigDecimal getTotalBrokenAmount() {
		BigDecimal totalBrokenAmount = new BigDecimal(0);
		if (getBrokenAmount() != null)
			totalBrokenAmount = totalBrokenAmount.add(new BigDecimal(getBrokenAmount()));
		totalBrokenAmount = totalBrokenAmount.add(new BigDecimal(getQcBrokenAmount()));
		totalBrokenAmount = totalBrokenAmount.add(new BigDecimal(getQcDestroyAmout()));
		return totalBrokenAmount;
	}

	public Long getTodoNgAmount() {
		// So luong NG san xuat
		Long todoNgAmount = getNgAmount();
		// NG san xuat da sua
		todoNgAmount -= getRepairedNgAmount();
		// NG qc san xuat
		todoNgAmount += getQcNgAmount();
		return todoNgAmount;
	}

	@JsonIgnore
	public String getTotalExeAmountStr() {
		return FormatNumber.num2Str(getTotalExeAmount());
	}

	/**
	 * Tim LSX gia cong ngoai gan nhat
	 * 
	 * @return
	 */
	@JsonIgnore
	public WorkOrder getOsWorkOrder() {
		WorkOrder osWo = null;
		for (QuotationItemExe qie : this.getQi().getQuotationItemAllExeList()) {
			// Cong doan gia cong truoc hoac la cong doan hien tai
			if (qie.getDisOrder() == null || qie.getDisOrder() <= this.quotationItemExe.getDisOrder())
				continue;
			// Khong phai OS
			if ("OS".equals(qie.getExeStepId().getStepType().getCode()))
				continue;
			for (WorkOrder wo : qie.getWorkOrders()) {
				// Khong cung production line
				if (!wo.productionLineId.equals(this.productionLineId))
					continue;
				if (osWo == null)
					osWo = wo;
				else {
					// OS gan hon
					if (wo.quotationItemExe.getDisOrder() < osWo.quotationItemExe.getDisOrder())
						osWo = wo;
				}
			}
		}
		return osWo;
	}

	/**
	 * So luong qc da thuc hien kiem tra de chuyen gia cong ngoai
	 * 
	 * @return
	 * @throws ResourceException
	 */

	public long getQcChkAmount() throws ResourceException {
		return qcChkAmount;
	}

	/**
	 * so luong da chuyen GCN ban thanh pham
	 */
	@Formula("(select nvl(sum(qo.amount),0) from qc_os qo where qo.WORK_ORDER_ID = id)")
	@Basic(fetch = FetchType.LAZY)
	private Long totalToOs;

	public Long getTotalToOs() {
		return totalToOs;
	}

	public void setTotalToOs(Long totalToOs) {
		this.totalToOs = totalToOs;
	}

	/**
	 * Da chuyen doi tac
	 */
	@Formula("(select nvl(sum(d.amount),0) from qc_os_detail d where d.work_order_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private long toPartnerAmount;
	/**
	 * Doi tac da tra hang
	 */
	@Formula("(select nvl(sum(qii.amount),0) from qc_in qii where qii.work_order_id = id)")
	@Basic(fetch = FetchType.LAZY)
	private long partnerDone;

	public long getToPartnerAmount() {
		return toPartnerAmount;
	}

	public long getPartnerDone() {
		return partnerDone;
	}

	public void extraWaitTimeForMachine() throws ResourceException {
		if (this.quotationItemExe.getUnitExeTime() == null)
			throw new ResourceException("Công đoạn %s-%s, chưa được lưu thông tin thời gian gia công",
					new Object[] { this.quotationItemExe.getExeStepId().getStepType().getName(),
							this.quotationItemExe.getExeStepId().getStepName() });
		BigDecimal newWaitTime = this.amount.multiply(this.quotationItemExe.getUnitExeTime())
				.add(this.astMachine.getWaitTime());
		this.astMachine.setWaitTime(newWaitTime);
	}

}
