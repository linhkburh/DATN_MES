package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.util.FormatNumber;

@Entity
@Table(name = "DLV_PACKAGE_DETAIL")
public class DLVPackageDetail {
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@JoinColumn(name = "DLV_PACKAGE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	@JsonIgnore
	@XmlTransient
	private DLVPackage dlvPackage;
	@Column(name = "AMOUNT")
	private Long amount;
	@JoinColumn(name = "QUOTATION_ITEM_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({ "department", "company", "technicalId", "planner", "acId", "quotationItemMaterialList",
			"lstProcessExe", "lstQuotationRepaire", "quotationId", "quotationItemAllExeList" })
	private QuotationItem quotationItem;

	public DLVPackageDetail(QuotationItem quotationItem, Long amount) {
		this(quotationItem);
		this.amount = amount;
	}

	public DLVPackageDetail(QuotationItem quotationItem) {
		this();
		this.quotationItem = quotationItem;
	}

	public DLVPackageDetail() {

	}

	public String getAmountStr() {
		return FormatNumber.num2Str(this.amount);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlTransient
	@JsonIgnore
	public DLVPackage getDlvPackage() {
		return dlvPackage;
	}

	public void setDlvPackage(DLVPackage dlvPackage) {
		this.dlvPackage = dlvPackage;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public QuotationItem getQuotationItem() {
		return quotationItem;
	}

	public void setQuotationItem(QuotationItem quotationItem) {
		this.quotationItem = quotationItem;
	}

	public String getQuotationCode() {
		return this.getQuotationItem().getQuotationId().getCode();
	}

}
