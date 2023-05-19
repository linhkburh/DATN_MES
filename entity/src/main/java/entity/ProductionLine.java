package entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.util.FormatNumber;

public class ProductionLine {
	@XmlTransient
	@JsonIgnore
	private QuotationItem qi;

	public QuotationItem getQi() {
		return qi;
	}

	@XmlElement
	@JsonIgnoreProperties("quotationItemExe")
	private List<WorkOrder> lstWorkOrder;
	private String productionLineId;

	public String getProductionLineId() {
		return productionLineId;
	}

	public void setProductionLineId(String productionLineId) {
		this.productionLineId = productionLineId;
	}

	@XmlElement
	private int orderNum;

	public int getOrderNum() {
		return orderNum;
	}

	public ProductionLine() {

	}

	public ProductionLine(WorkOrder workOrder) {
		this.qi = workOrder.getQuotationItemExe().getQuotationItemId();
		this.productionLineId = workOrder.getProductionLineId();
		this.lstWorkOrder = new ArrayList<WorkOrder>(Arrays.asList(workOrder));
		if (this.qi.getLstProductionLine() == null)
			this.orderNum = 1;
		else
			this.orderNum = qi.getLstProductionLine().size() + 1;
	}

	@XmlElement
	public int getAmount() {
		return this.lstWorkOrder.get(0).getAmount().intValue();
	}

	public List<WorkOrder> getLstWorkOrder() {
		return lstWorkOrder;
	}

	/**
	 * Tong thoi gian du kien (ke hoach)
	 */
	public BigDecimal getTotalEstTime() {
		int amount = getAmount();
		BigDecimal itemEstTime = qi.getItemEstTime();
		return new BigDecimal(amount).multiply(itemEstTime).divide(new BigDecimal(60), 3, RoundingMode.HALF_UP);

	}

	public String getTotalEstTimeStr() {
		return FormatNumber.num2Str(getTotalEstTime());
	}

	@JsonProperty
	public String getSetupTimeAvgStr() {
		return this.qi.getSetupTimeAvgStr();
	}
}
