package entity;

import java.io.Serializable;

public class WorkStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private String workCode;
	private Long itemAmount;
	private Long itemAverageTime;
	private Long totalTime;
	private Long processedAmount;
	private Long planTime;
	private Long realTime;
	private Long slowPlanMin;
	private Long slowPlanPercent;
	private Long remainingAmount;
	private Long remainingTimePlan;
	private Long remainingTimeReality;
	private String exeStepSts;
	private String workSts;
	public String getWorkCode() {
		return workCode;
	}
	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	public Long getItemAmount() {
		return itemAmount;
	}
	public void setItemAmount(Long itemAmount) {
		this.itemAmount = itemAmount;
	}
	public Long getItemAverageTime() {
		return itemAverageTime;
	}
	public void setItemAverageTime(Long itemAverageTime) {
		this.itemAverageTime = itemAverageTime;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	public Long getProcessedAmount() {
		return processedAmount;
	}
	public void setProcessedAmount(Long processedAmount) {
		this.processedAmount = processedAmount;
	}
	public Long getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Long planTime) {
		this.planTime = planTime;
	}
	public Long getRealTime() {
		return realTime;
	}
	public void setRealTime(Long realTime) {
		this.realTime = realTime;
	}
	public Long getSlowPlanMin() {
		return slowPlanMin;
	}
	public void setSlowPlanMin(Long slowPlanMin) {
		this.slowPlanMin = slowPlanMin;
	}
	public Long getSlowPlanPercent() {
		return slowPlanPercent;
	}
	public void setSlowPlanPercent(Long slowPlanPercent) {
		this.slowPlanPercent = slowPlanPercent;
	}
	public Long getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(Long remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public Long getRemainingTimePlan() {
		return remainingTimePlan;
	}
	public void setRemainingTimePlan(Long remainingTimePlan) {
		this.remainingTimePlan = remainingTimePlan;
	}
	public Long getRemainingTimeReality() {
		return remainingTimeReality;
	}
	public void setRemainingTimeReality(Long remainingTimeReality) {
		this.remainingTimeReality = remainingTimeReality;
	}
	public String getExeStepSts() {
		return exeStepSts;
	}
	public void setExeStepSts(String exeStepSts) {
		this.exeStepSts = exeStepSts;
	}
	public String getWorkSts() {
		return workSts;
	}
	public void setWorkSts(String workSts) {
		this.workSts = workSts;
	}
	
}
