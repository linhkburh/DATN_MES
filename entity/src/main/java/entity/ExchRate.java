package entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysDictParam;

@Entity
@Table(name = "EXCH_RATE")
public class ExchRate {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CURRENCY", referencedColumnName = "ID")
	private SysDictParam currency;
	@Column(name = "EXCH_RATE")
	private BigDecimal exchRate;
	@Column(name = "EXCH_DATE")
	@Temporal(TemporalType.DATE)
	private Date exchDate;
	@Column(name = "EXCH_SOURCE")
	private String exchSource;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getExchRate() {
		return exchRate;
	}

	public void setExchRate(BigDecimal exchRate) {
		this.exchRate = exchRate;
	}

	public Date getExchDate() {
		return exchDate;
	}

	public void setExchDate(Date exchDate) {
		this.exchDate = exchDate;
	}

	public String getExchSource() {
		return exchSource;
	}

	public void setExchSource(String exchSource) {
		this.exchSource = exchSource;
	}

	public SysDictParam getCurrency() {
		return currency;
	}

	public void setCurrency(SysDictParam currency) {
		this.currency = currency;
	}

	public void setExchDateStr(String exchDateStr) throws Exception {
		this.exchDate = Formater.str2date(exchDateStr);
	}

	public String getExchDateStr() {
		return Formater.date2str(this.exchDate);
	}

	public void setExchRateStr(String exchRateStr) throws Exception {
		this.exchRate = FormatNumber.str2num(exchRateStr);
	}

	public String getExchRateStr() {
		return FormatNumber.num2Str(this.exchRate);
	}
}
