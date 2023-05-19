package entity;

import java.io.Serializable;

public class MachineStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private String machineCode;
	private Long workAmount;
	private Long timeTotal;
	private Long planTime;
	private Long realTime;
	private Long slowPlan;
	private Long remainingTimePlan;
	private Long remainingTimeReality;
	private String status;
	public String getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	public Long getWorkAmount() {
		return workAmount;
	}
	public void setWorkAmount(Long workAmount) {
		this.workAmount = workAmount;
	}
	public Long getTimeTotal() {
		return timeTotal;
	}
	public void setTimeTotal(Long timeTotal) {
		this.timeTotal = timeTotal;
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
	public Long getSlowPlan() {
		return slowPlan;
	}
	public void setSlowPlan(Long slowPlan) {
		this.slowPlan = slowPlan;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
