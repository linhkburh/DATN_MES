package entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class DashBroadNG implements Serializable {

	private static final long serialVersionUID = 1L;

	private String time;
	private String level;
	private BigDecimal amount;
	private BigDecimal ngAmount;
	private BigDecimal totalAmount;
	private BigDecimal brokenAmount;

	public DashBroadNG() {
		super();
	}

	public DashBroadNG(String time, String level, BigDecimal amount,
			BigDecimal ngAmount, BigDecimal totalAmount) {
		this.time = time;
		this.level = level;
		this.amount = amount;
		this.ngAmount = ngAmount;
		this.totalAmount = totalAmount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getNgAmount() {
		return ngAmount;
	}

	public void setNgAmount(BigDecimal ngAmount) {
		this.ngAmount = ngAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getBrokenAmount() {
		return brokenAmount;
	}

	public void setBrokenAmount(BigDecimal brokenAmount) {
		this.brokenAmount = brokenAmount;
	}

}
