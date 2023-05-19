package entity;

import java.io.Serializable;

public class ProStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private String workerName;
	private Long drawTotal;
	private Long timeTotal;
	private Long drawTotalDone;
	private Long planTime;
	private Long realTime;
	private Long slowPlan;
	private Long remainingDraw;
	private Long remainingTimePlan;
	private Long remainingTimeReality;
	private String status;
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public Long getDrawTotal() {
		return drawTotal;
	}
	public void setDrawTotal(Long drawTotal) {
		this.drawTotal = drawTotal;
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
	public Long getRemainingDraw() {
		return remainingDraw;
	}
	public void setRemainingDraw(Long remainingDraw) {
		this.remainingDraw = remainingDraw;
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
	public Long getDrawTotalDone() {
		return drawTotalDone;
	}
	public void setDrawTotalDone(Long drawTotalDone) {
		this.drawTotalDone = drawTotalDone;
	}
	
}
