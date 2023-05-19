package entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import common.util.FormatNumber;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;

public abstract class ParentItem extends Working {
	public enum TIME_UNIT {
		HOUR, MINUTE
	}

	/**
	 * Trang thai cong viec chua chuan bi (chua tao LSX)
	 */
	public static final Short WORK_STS_PREPARING = -2;
	/**
	 * Trang thai cong viec chua thuc hien
	 */
	public static final Short WORK_STS_PENDDING = -1;
	/**
	 * Trang thai cong viec dang thuc hien
	 */
	public static final Short WORK_STS_DOING = 0;
	/**
	 * Trang thai cong viec da thuc hien
	 */
	public static final Short WORK_STS_FINISH = 1;
	/**
	 * Thoi gian theo ke hoach da thuc hien
	 */
	@Transient
	/**
	 * Khoi luong (do bang thoi gian) da duoc thuc hien (so gio da chay)
	 */
	private BigDecimal estTime;

	@Transient
	/**
	 * Thoi gian bao gia
	 */
	private BigDecimal targetTime;
	/**
	 * Thoi gian gia cong thuc te
	 */
	private BigDecimal exeTime;
	/**
	 * Thoi diem bat dau
	 */
	private Date startTime;

	/**
	 * Thoi gian setup thuc te
	 */
	private BigDecimal exeSetupTime;

	/**
	 * Thoi gian setup du kien
	 */
	private BigDecimal setupTime;

	/**
	 * Thoi diem ket thuc
	 */
	private Date endTime;

	/**
	 * Tong thoi gian du kien (ke hoach)
	 */
	private BigDecimal totalEstTime;
	/**
	 * So luong chi tiet da hoan thanh
	 */
	private BigDecimal numOfFinishItem;

	private Short workSts;

	private boolean sumary = false;
	/**
	 * Tong so NG
	 */
	private Long ngAmount = 0l;
	/**
	 * Tong so NG da sua
	 */
	@Transient
	private long repairedNgAmount;

	public long getRepairedNgAmount() {
		return repairedNgAmount;
	}

	public String getRepairedNgAmountStr() {
		return FormatNumber.num2Str(repairedNgAmount);
	}

	/**
	 * So luong NG do QC trong giai doan san xuat
	 */
	private Long qcNgAmount;
	/**
	 * So luong chi tiet huy
	 */
	private Long brokenAmount;

	// So luong chi tiet
	public abstract BigDecimal getAmount();

	/**
	 * Thoi gian trung binh chi tiet du kien (theo ke hoach)
	 * 
	 * @return
	 * @throws ResourceException
	 */
	@JsonIgnore
	public String getAvageEstTimeStr() {
		if (this instanceof QuotationItem) {
			BigDecimal avageEstTime = new BigDecimal(0);
			QuotationItem qi = (QuotationItem) this;
			for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
				if (qie.getUnitExeTime() != null)
					avageEstTime = avageEstTime.add(qie.getUnitExeTime());
			}

			return FormatNumber.num2Str(avageEstTime);
		}
		if (this instanceof QuotationItemExe)
			return ((QuotationItemExe) this).getUnitExeTimeStr();
		if (this instanceof WorkOrder)
			return ((WorkOrder) this).getQuotationItemExe().getAvageEstTimeStr();
		if (this instanceof WorkOrderExe)
			return ((WorkOrderExe) this).getWorkOrderId().getAvageEstTimeStr();
		return null;
	}

	public Long getBrokenAmount() {
		sumary();
		return brokenAmount;
	}

	/**
	 * Lay theo thoi diem hoan thanh lenh san xuat cuoi cung
	 */
	@Override
	public Date getEndTime() {
		sumary();
		return endTime;

	}

	@Override
	public BigDecimal getEstTime() {
		sumary();
		return estTime;

	}

	@Override
	public String getEstTimeStr() {
		return Formater.num2str(getEstTime());
	}

	/**
	 * Thoi gian trung binh thuc te 1 chi tiet
	 * 
	 * @return
	 */
	public String getExeAvageTimeStr() {
		if (getNumOfFinishItem() == null || getNumOfFinishItem().doubleValue() == 0)
			return null;
		if (this instanceof WorkOrder)
			return FormatNumber.num2Str(
					getExeTime().divide(getNumOfFinishItem(), 3, RoundingMode.HALF_UP).multiply(new BigDecimal(60)));
		if (getExeTime() == null)
			return null;
		return FormatNumber.num2Str(getExeTime().divide(getNumOfFinishItem(), 3, RoundingMode.HALF_UP));
	}

	public BigDecimal getExeSetupTime() {
		return exeSetupTime;
	}

	@Override
	public BigDecimal getExeTime() {
		sumary();
		return exeTime;

	}

	@Override
	public String getExeTimeStr() {
		return Formater.num2str(getExeTime());

	}

	/**
	 * Phan tram cham
	 * 
	 * @return
	 */
	public BigDecimal getLatePercent() {
		BigDecimal estTime = getEstTime();
		if (estTime == null || estTime.compareTo(new BigDecimal(0)) == 0)
			return new BigDecimal(0);
		return getLateTime().multiply(new BigDecimal(100)).divide(estTime, 3, RoundingMode.HALF_UP);
	}

	public String getLatePercentStr() {
		return FormatNumber.num2Str(getLatePercent());
	}

	/**
	 * Thoi gian cham
	 * 
	 * @return
	 */
	public BigDecimal getLateTime() {
		if (getEstTime() == null)
			return getExeTime();
		return getExeTime().subtract(getEstTime());
	}

	public String getLateTimeStr() {
		return FormatNumber.num2Str(getLateTime());
	}

	@JsonIgnore
	public abstract List<? extends ParentItem> getLstChildren();

	public Long getNgAmount() {
		sumary();
		return ngAmount;
	}

	public String getNgAmountStr() {
		return FormatNumber.num2Str(getNgAmount());
	}

	// Tong so con
	public abstract BigDecimal getNumOfChildren();

	// Tong so con da thuc hien xong
	public abstract BigDecimal getNumOfFinishChildren();

	public BigDecimal getNumOfFinishItem() {
		sumary();
		return numOfFinishItem;
	}

	public String getNumOfFinishItemStr() {
		return FormatNumber.num2Str(getNumOfFinishItem());
	}

	public Long getQcNgAmount() {
		sumary();
		return qcNgAmount;
	}

	public BigDecimal getRemainingTime() {
		if (getTotalEstTime() == null)
			return null;
		if (getExeTime() == null)
			return getTotalEstTime();
		return getTotalEstTime().subtract(getExeTime());

	}

	public String getRemainingTimeStr() {
		return FormatNumber.num2Str(getRemainingTime());
	}

	public String getRemainTimeStr() {
		if (getTotalEstTime() == null)
			return null;
		return FormatNumber.num2Str(getTotalEstTime().subtract(getExeTime()));

	}

	public BigDecimal getSetupTime() {
		return setupTime;
	}

	public Date getStartTime() {
		sumary();
		return startTime;
	}

	@JsonIgnore
	public String getStatusDescription() throws Exception {
		Short workSts = getWorkSts();
		if (WORK_STS_PREPARING.equals(workSts))
			return "Chưa tạo xong lệnh sản xuất";
		else if (WORK_STS_PENDDING.equals(workSts))
			return "Chưa thực hiện";
		else if (WORK_STS_DOING.equals(workSts))
			return "Đang thực hiện";
		return "Đã hoàn thành";
	}

	public BigDecimal getTargetTime() {
		sumary();
		return targetTime;
	}

	public TIME_UNIT getTimeUnit() {
		return TIME_UNIT.HOUR;
	}

	public BigDecimal getTotalEstTime() {
		sumary();
		return totalEstTime;
	}

	public String getTotalEstTimeStr() {
		return Formater.num2str(getTotalEstTime());
	}

	@JsonIgnore
	public Short getWorkSts() {
		if (workSts != null)
			return workSts;
		if (getNumOfChildren().doubleValue() == 0)
			workSts = WORK_STS_PENDDING;
		else if (getNumOfChildren().longValue() == getNumOfFinishChildren().longValue())
			workSts = WORK_STS_FINISH;
		else {
			workSts = runingOrPending();
		}
		return workSts;
	}

	public boolean isSumary() {
		return sumary;
	}

	public void setSumary(boolean sumary) {
		this.sumary = sumary;
	}

	private Short runingOrPending() {
		for (ParentItem child : getLstChildren()) {
			if (child.getWorkSts() != WORK_STS_PENDDING)
				return WORK_STS_DOING;
		}
		return WORK_STS_PENDDING;
	}

	public void setNgAmount(Long ngAmount) {
		this.ngAmount = ngAmount;
	}

	public void setQcNgAmount(long qcNgAmount) {
		this.qcNgAmount = qcNgAmount;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void sumary() {
		if (sumary)
			return;
		sumary = true;
		List<? extends ParentItem> lstChildren = getLstChildren();
		if (lstChildren == null || lstChildren.isEmpty()) {
			exeTime = new BigDecimal(0);
			totalEstTime = new BigDecimal(0);
			estTime = new BigDecimal(0);
			targetTime = new BigDecimal(0);
			return;
		}
		for (ParentItem child : lstChildren) {
			// Bo qua lap trinh
			if (child instanceof QuotationItemExe
					&& Boolean.TRUE.equals(((QuotationItemExe) child).getExeStepId().getProgram()))
				continue;
			if (child instanceof QuotationItem) {
				BigDecimal childSetupTime = child.getSetupTime();
				if (childSetupTime != null) {
					BigDecimal childSetupTimeInHour = childSetupTime.divide(new BigDecimal(60), 3,
							RoundingMode.HALF_UP);
					if (this.setupTime == null)
						this.setupTime = childSetupTimeInHour;
					else
						this.setupTime = this.setupTime.add(childSetupTimeInHour);
				}
			}
			// qcNgAmount
			if (this instanceof QuotationItemExe || this instanceof Quotation) {
				if (child.getQcNgAmount() != null) {
					if (qcNgAmount == null)
						qcNgAmount = child.getQcNgAmount();
					else
						qcNgAmount += child.getQcNgAmount();
				}
			}

			// brokenAmount
			if (brokenAmount == null)
				brokenAmount = child.getBrokenAmount();
			else if (child.getBrokenAmount() != null)
				brokenAmount += child.getBrokenAmount();

			// startTime
			if (startTime == null)
				startTime = child.getStartTime();
			else if (child.getStartTime() != null && child.getStartTime().before(startTime))
				startTime = child.getStartTime();

			// endTime
			if (endTime == null)
				endTime = child.getEndTime();
			else if (child.getEndTime() != null && child.getEndTime().after(endTime))
				endTime = child.getEndTime();

			// estTime
			if (estTime == null)
				estTime = child.getEstTime();
			else {
				BigDecimal childEstTime = child.getEstTime();
				if (childEstTime != null)
					estTime = estTime.add(childEstTime);
			}
			// targetTime
			if (targetTime == null)
				targetTime = child.getEstTime();
			else {
				BigDecimal childTargetTime = child.getEstTime();
				if (childTargetTime != null)
					targetTime = targetTime.add(childTargetTime);
			}

			// exeTime
			if (exeTime == null)
				exeTime = child.getExeTime();
			else {
				BigDecimal childexeTime = child.getExeTime();
				if (childexeTime != null)
					exeTime = exeTime.add(childexeTime);
			}

			// exeSetupTime
			if (exeSetupTime == null)
				exeSetupTime = child.getExeSetupTime();
			else {
				BigDecimal childExeSetupTime = child.getExeSetupTime();
				if (childExeSetupTime != null)
					exeSetupTime = exeSetupTime.add(childExeSetupTime);
			}

			// totalEstTime
			if (!(this instanceof WorkOrderExe || this instanceof WorkOrder || this instanceof QuotationItemExe)) {
				if (totalEstTime == null) {
					totalEstTime = child.getTotalEstTime();
				} else {
					BigDecimal childTotalEstTime = child.getTotalEstTime();
					if (childTotalEstTime != null)
						totalEstTime = totalEstTime.add(childTotalEstTime);
				}
			}

			// So luong hong
			if (this instanceof WorkOrder) {
				if (Boolean.TRUE.equals(((WorkOrderExe) child).getNgRepaire())) {
					// So luong NG da sua
					repairedNgAmount += ((WorkOrderExe) child).getAmount().longValue();
				} else {
					// So luong NG
					Long childNgAmount = child.getNgAmount();
					if (childNgAmount != null)
						ngAmount = ngAmount + childNgAmount;
				}

			} else {
				Long childNgAmount = child.getNgAmount();
				if (childNgAmount != null)
					ngAmount = ngAmount + childNgAmount;
			}

			// So luong chi tiet da hoan thanh
			// Qi kg tinh theo cong thuc bang tong cua con duoc
			if (!(this instanceof QuotationItem)) {
				BigDecimal childNumOfFinishItem = child.getNumOfFinishItem();
				if (childNumOfFinishItem != null) {
					if (numOfFinishItem == null)
						numOfFinishItem = childNumOfFinishItem;
					else
						numOfFinishItem = numOfFinishItem.add(childNumOfFinishItem);
				}
			}

		}
		// Quy doi tu phut -> gio
		// Cung su dung don vi thoi gian
		if (getTimeUnit() == lstChildren.get(0).getTimeUnit())
			return;

		if (estTime == null)
			estTime = new BigDecimal(0);
		else
			estTime = estTime.divide(new BigDecimal(60), 3, RoundingMode.HALF_UP);
		if (exeTime == null)
			exeTime = new BigDecimal(0);
		else
			exeTime = exeTime.divide(new BigDecimal(60), 3, RoundingMode.HALF_UP);
	}

	public String getBrokenAmountStr() {
		return FormatNumber.num2Str(getBrokenAmount());
	}

	public static class CustomIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			if (super.hasIgnoreMarker(m))
				return true;
			return m.getDeclaringClass() == ParentItem.class;
		}
	}

	public static ObjectMapper objectMapper;
	static {
		objectMapper = JsonUtils.objectMapper1();
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.setAnnotationIntrospector(new CustomIntrospector());

	}
}
