package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.FormatNumber;
import entity.frwk.SysUsers;

@Entity
@Table(name = "QUOTATION_ITEM_PROCESS")
@XmlRootElement(name = "process")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuotationItemProcess implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@JoinColumn(name = "CREATOR", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SysUsers creator;

	@Column(name = "QUALITY")
	private BigDecimal quality;
	@Column(name = "TYPE")
	private String type;

	@JoinColumn(name = "COMPANY")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@JoinColumn(name = "QUOTATION_ITEM_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private QuotationItem quotationItem;

	public QuotationItemProcess(QuotationItem quotationItem, BigDecimal quality, String type, SysUsers creator) {
		this.quotationItem = quotationItem;
		// Luu du thua de tim kiem
		this.company = quotationItem.getCompany();
		this.quality = quality;
		this.type = type;
		this.creator = creator;
		this.createDate = Calendar.getInstance().getTime();
	}

	public QuotationItemProcess() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SysUsers getCreator() {
		return creator;
	}

	public void setCreator(SysUsers creator) {
		this.creator = creator;
	}

	public BigDecimal getQuality() {
		return quality;
	}

	public void setQuality(BigDecimal quality) {
		this.quality = quality;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
	}

	public String getQualityStr() {
		return FormatNumber.num2Str(this.quality);
	}

	public void setQualityStr(String qualityStr) throws ParseException {
		this.quality = FormatNumber.str2num(qualityStr);
	}

	public Long getTotalEstTime() {
		Long itmEstTime = getItmEstTime();
		if (itmEstTime == null)
			return null;
		return itmEstTime * this.quality.longValue();
	}

	public Long getItmEstTime() {
		return "QC".equals(this.type) ? this.quotationItem.getQcItmEstTime()
				: "CL".equals(this.type) ? this.quotationItem.getPlshItmEstTime() : null;
	}
}
