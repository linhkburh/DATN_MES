package entity;

import java.io.Serializable;

public class WorkerStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private String wokerName;
	private String workCode;
	private Long itemAmount;
	private Long itemAverageTime;
	private Long timeTotal;
	private Long realTime;
	private Long slowMins;
	private Long slowPercent;
	private String status;
	public String getWokerName() {
		return wokerName;
	}
	public void setWokerName(String wokerName) {
		this.wokerName = wokerName;
	}
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
	public Long getTimeTotal() {
		return timeTotal;
	}
	public void setTimeTotal(Long timeTotal) {
		this.timeTotal = timeTotal;
	}
	public Long getRealTime() {
		return realTime;
	}
	public void setRealTime(Long realTime) {
		this.realTime = realTime;
	}
	public Long getSlowMins() {
		return slowMins;
	}
	public void setSlowMins(Long slowMins) {
		this.slowMins = slowMins;
	}
	public Long getSlowPercent() {
		return slowPercent;
	}
	public void setSlowPercent(Long slowPercent) {
		this.slowPercent = slowPercent;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
