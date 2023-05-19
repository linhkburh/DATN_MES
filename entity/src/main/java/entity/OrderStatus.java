package entity;

import java.io.Serializable;

public class OrderStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private String custometCode;
	private String custometName;
	private Long numberOfDraw;
	private Long totalOder;
	private Long totalOderMade;
	private Long totalOderReality;
	private Long totalOderlow;
	private Long totalOderlowPercent;
	private Long totalOderRemaining;
	private Long totalOderRemainingReality;
	private String exeStepSts;
	private String orderSts;

	public OrderStatus() {
		super();
	}

	public String getCustometCode() {
		return custometCode;
	}

	public void setCustometCode(String custometCode) {
		this.custometCode = custometCode;
	}

	public String getCustometName() {
		return custometName;
	}

	public void setCustometName(String custometName) {
		this.custometName = custometName;
	}

	public Long getNumberOfDraw() {
		return numberOfDraw;
	}

	public void setNumberOfDraw(Long numberOfDraw) {
		this.numberOfDraw = numberOfDraw;
	}

	public Long getTotalOder() {
		return totalOder;
	}

	public void setTotalOder(Long totalOder) {
		this.totalOder = totalOder;
	}

	public Long getTotalOderMade() {
		return totalOderMade;
	}

	public void setTotalOderMade(Long totalOderMade) {
		this.totalOderMade = totalOderMade;
	}

	public Long getTotalOderReality() {
		return totalOderReality;
	}

	public void setTotalOderReality(Long totalOderReality) {
		this.totalOderReality = totalOderReality;
	}

	public Long getTotalOderlow() {
		return totalOderlow;
	}

	public void setTotalOderlow(Long totalOderlow) {
		this.totalOderlow = totalOderlow;
	}

	public Long getTotalOderlowPercent() {
		return totalOderlowPercent;
	}

	public void setTotalOderlowPercent(Long totalOderlowPercent) {
		this.totalOderlowPercent = totalOderlowPercent;
	}

	public Long getTotalOderRemaining() {
		return totalOderRemaining;
	}

	public void setTotalOderRemaining(Long totalOderRemaining) {
		this.totalOderRemaining = totalOderRemaining;
	}

	public Long getTotalOderRemainingReality() {
		return totalOderRemainingReality;
	}

	public void setTotalOderRemainingReality(Long totalOderRemainingReality) {
		this.totalOderRemainingReality = totalOderRemainingReality;
	}

	public String getOrderSts() {
		return orderSts;
	}

	public void setOrderSts(String orderSts) {
		this.orderSts = orderSts;
	}

	public String getExeStepSts() {
		return exeStepSts;
	}

	public void setExeStepSts(String exeStepSts) {
		this.exeStepSts = exeStepSts;
	}

}
