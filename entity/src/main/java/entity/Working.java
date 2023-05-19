package entity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Transient;

import common.util.FormatNumber;
import common.util.Formater;

public abstract class Working{
	@Transient
	private BigDecimal estTime;
	// Thoi gian thuc hien
	private BigDecimal exeTime;
	// Thoi diem bat dau
	private Date startTime;
	// Thoi diem ket thuc
	private Date endTime;
	// Thoi gian theo ke hoac ung voi luong cong viec da thuc hien
	private BigDecimal assignTime;
	private BigDecimal totalEstTime;

	public abstract BigDecimal getEstTime();

	public String getEstTimeStr() {
		return FormatNumber.num2Str(this.estTime);
	}

	@Transient
	private String startTimeStr;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		this.startTimeStr = Formater.dateTime2str(startTime);
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) throws ParseException {
		this.startTimeStr = startTimeStr;
		if (!Formater.isNull(startTimeStr))
			this.startTime = Formater.str2DateTime(startTimeStr);
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		this.endTimeStr = Formater.dateTime2str(endTime);
	}

	@Transient
	private String endTimeStr;

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) throws ParseException {
		this.endTimeStr = endTimeStr;
		if (!Formater.isNull(endTimeStr))
			this.endTime = Formater.str2DateTime(endTimeStr);
	}

	public BigDecimal getExeTime() {
		return exeTime;
	}

	public void setExeTime(BigDecimal exeTime) {
		this.exeTime = exeTime;
		this.exeTimeStr = FormatNumber.num2Str(this.exeTime);
	}

	private BigDecimal workedTime;

	public BigDecimal getWorkedTime() {
		return workedTime;
	}

	public void setWorkedTime(BigDecimal workedTime) {
		this.workedTime = workedTime;
	}

	@Transient
	private String exeTimeStr;

	public String getExeTimeStr() {
		return exeTimeStr;
	}

	public void setExeTimeStr(String exeTimeStr) throws ParseException {
		this.exeTimeStr = exeTimeStr;
		this.exeTime = FormatNumber.str2num(this.exeTimeStr);
	}

	/**
	 * Thoi gian trung binh
	 */
	@Transient
	private BigDecimal average;
	@Transient
	private String averageStr;

	public BigDecimal getAverage() {
		return average;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
		this.averageStr = FormatNumber.num2Str(this.average);
	}

	public String getAverageStr() {
		return averageStr;
	}

	public void setAverageStr(String averageStr) throws ParseException {
		this.averageStr = averageStr;
		this.average = FormatNumber.str2num(this.averageStr);
	}

	/**
	 * Thoi gian con lai
	 */
	@Transient
	private BigDecimal balanceTime;

	public BigDecimal getBalanceTime() {
		return balanceTime;
	}

	public void setBalanceTime(BigDecimal balanceTime) {
		this.balanceTime = balanceTime;
		this.balanceTimeStr = FormatNumber.num2Str(this.balanceTime);
	}

	/**
	 * Thoi gian con lai
	 */
	@Transient
	private String balanceTimeStr;

	public String getBalanceTimeStr() {
		return balanceTimeStr;
	}

	public void setBalanceTimeStr(String balanceTimeStr) throws ParseException {
		this.balanceTimeStr = balanceTimeStr;
		this.balanceTime = FormatNumber.str2num(this.balanceTimeStr);
	}

}
