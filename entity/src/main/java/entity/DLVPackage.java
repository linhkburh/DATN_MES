package entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.util.FormatNumber;
import common.util.Formater;
import entity.frwk.SysUsers;

@Entity
@Table(name = "DLV_PACKAGE")
public class DLVPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "CODE", nullable = false, length = 40)
	private String code;
	@Column(name = "QUALITY")
	private Long quality;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "DELIVER_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deliverDate;
	@JsonIgnoreProperties("lstMail")
	@JoinColumn(name = "CREATOR", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SysUsers creator;
	@Column(name = "PCK_CODE", length = 40)
	// Ma ban ve
	private String pckCode;
	@JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Customer customer;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dlvPackage", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("dlvPackage")
	// @JsonIgnore
	private List<DLVPackageDetail> lstPackDetail = new ArrayList<DLVPackageDetail>();
	@Column(name = "STATUS")
	private Byte status;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getQuality() {
		return quality;
	}

	public void setQuality(Long quality) {
		this.quality = quality;
	}

	public String getQualityStr() {
		return FormatNumber.num2Str(this.quality);
	}

	public void setQualityStr(String qualityStr) throws ParseException {
		this.quality = FormatNumber.str2Long(qualityStr);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public SysUsers getCreator() {
		return creator;
	}

	public void setCreator(SysUsers creator) {
		this.creator = creator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public String getQuotationItemCode() {
		return this.lstPackDetail.get(0).getQuotationItem().getCode();
	}

	@Transient
	public String getQuotationItemName() {
		return this.lstPackDetail.get(0).getQuotationItem().getName();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JsonIgnoreProperties("dlvPackage")
	public List<DLVPackageDetail> getLstPackDetail() {
		return lstPackDetail;
	}

	public void setLstPackDetail(List<DLVPackageDetail> lstPackDetail) {
		this.lstPackDetail = lstPackDetail;
	}

	public DLVPackage() {
		this.createDate = Calendar.getInstance().getTime();
		this.quality = 0l;
	}

	public DLVPackage(SysUsers creator, QuotationItem quotationItem, int pckSeq) {
		this();
		this.creator = creator;
		this.customer = quotationItem.getQuotationId().getCustomer();
		this.pckCode = quotationItem.getQuotationId().getCode();
		if (pckSeq < 10)
			this.code = "00" + pckSeq;
		else if (pckSeq < 100)
			this.code = "0" + pckSeq;
		else
			this.code = "" + pckSeq;
	}

	public DLVPackage(SysUsers creator, DLVPackageDetail detail) {
		this(creator, detail.getQuotationItem(), 0);
		detail.setDlvPackage(this);
		this.quality = detail.getAmount().longValue();
		this.lstPackDetail = new ArrayList<DLVPackageDetail>(Arrays.asList(detail));
		this.ids = detail.getQuotationItem().getId();
		this.numOfPackage = 1l;
		this.amounts = String.valueOf(detail.getAmount());

	}

	public DLVPackage(SysUsers creator, DLVPackageDetail detail, Date createDate) {

	}

	public DLVPackage(SysUsers creator, DLVPackageDetail detail, Date createDate, int pckSeq) {
		this(creator, detail.getQuotationItem(), pckSeq);
		detail.setDlvPackage(this);
		this.quality = detail.getAmount().longValue();
		this.lstPackDetail = new ArrayList<DLVPackageDetail>(Arrays.asList(detail));
		this.ids = detail.getQuotationItem().getId();
		this.numOfPackage = 1l;
		this.amounts = String.valueOf(detail.getAmount());
		this.createDate = createDate;

	}

	@Transient
	private String amounts;

	public String getAmounts() {
		return amounts;
	}

	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}

	public DLVPackage(SysUsers creator) {
		this();
		this.creator = creator;
	}

	@XmlElement(name = "qrContent")
	@JsonIgnore
	public String getQrContent() {
		DLVPackageDetail detalO = this.lstPackDetail.get(0);
		QuotationItem qi = detalO.getQuotationItem();
		String qrCode = getDsMaQl();
		qrCode += "|" + qi.getCode();
		qrCode += "|" + this.getQualityStr();
		return qrCode;
	}

	@JsonIgnore
	@XmlElement(name = "qrText")
	public String getQrText() {
		DLVPackageDetail detalO = this.lstPackDetail.get(0);
		QuotationItem qi = detalO.getQuotationItem();
		String qrCode = qi.getQuotationId().getCompany().getSortName();
		qrCode += System.getProperty("line.separator") + getDsMaQl();
		qrCode += System.getProperty("line.separator") + this.lstPackDetail.get(0).getQuotationItem().getCode();
		qrCode += System.getProperty("line.separator") + this.getQualityStr() + "PCS";
		qrCode += System.getProperty("line.separator") + Formater.dateTime2str(this.deliverDate);
		return qrCode;
	}

	@XmlElement(name = "dsMaQl")
	public String getDsMaQl() {
		String dsMaQl = null;
		for (DLVPackageDetail d : lstPackDetail) {
			if (dsMaQl == null)
				dsMaQl = Formater.isNull(d.getQuotationItem().getRootManageCode())
						? d.getQuotationItem().getManageCode()
						: d.getQuotationItem().getRootManageCode();
			else
				dsMaQl += ", " + (Formater.isNull(d.getQuotationItem().getRootManageCode())
						? d.getQuotationItem().getManageCode()
						: d.getQuotationItem().getRootManageCode());
		}
		return dsMaQl;
	}

	@XmlTransient
	@JsonIgnore
	public String getDsMaQlDung() {
		String dsMaQl = null;
		for (DLVPackageDetail d : lstPackDetail) {
			if (dsMaQl == null)
				dsMaQl = d.getQuotationItem().getManageCode();
			else
				dsMaQl += ", " + d.getQuotationItem().getManageCode();
		}
		return dsMaQl;
	}

	public void add(DLVPackageDetail detail) {
		this.quality += detail.getAmount();
		this.lstPackDetail.add(detail);
		detail.setDlvPackage(this);
		if (ids == null)
			ids = detail.getQuotationItem().getId();
		else
			ids += "," + detail.getQuotationItem().getId();
		if (amounts == null)
			this.amounts = String.valueOf(detail.getAmount());
		else
			amounts += "," + String.valueOf(detail.getAmount());

	}

	@Transient
	private String ids;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Transient
	private Long numOfPackage;

	public Long getNumOfPackage() {
		return numOfPackage;
	}

	public void setNumOfPackageStr(String numOfPackageStr) throws ParseException {
		this.numOfPackage = FormatNumber.str2Long(numOfPackageStr);
	}

	@Transient
	private Long numPerPackage;

	public Long getNumPerPackage() {
		return numPerPackage;
	}

	public void setNumPerPackageStr(String numPerPackageStr) throws ParseException {
		this.numPerPackage = FormatNumber.str2Long(numPerPackageStr);
	}

	public String getPckCode() {
		return pckCode;
	}

	public void setPckCode(String pckCode) {
		this.pckCode = pckCode;
	}

}
