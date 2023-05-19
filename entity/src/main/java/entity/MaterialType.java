package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import common.util.FormatNumber;

@Entity
@Table(name = "MATERIAL_TYPE")
public class MaterialType implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "CODE")
	private String code;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "INIT_PRICE_K")
	private BigDecimal initPriceK;

	@Column(name = "INIT_PRICE_TB")
	private BigDecimal initPriceTB;

	@Column(name = "INIT_PRICE_D")
	private BigDecimal initPriceD;

	@JoinColumn(name = "PREVIOUS_VERSION", referencedColumnName = "ID")
	@ManyToOne
	private MaterialType materialType;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Transient
	private String unitPriceStr;

	public MaterialType() {
		super();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public String getUnitPriceStr() {
		return unitPriceStr;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getInitPriceK() {
		return initPriceK;
	}

	public void setInitPriceK(BigDecimal initPriceK) {
		this.initPriceK = initPriceK;
	}

	public BigDecimal getInitPriceTB() {
		return initPriceTB;
	}

	public void setInitPriceTB(BigDecimal initPriceTB) {
		this.initPriceTB = initPriceTB;
	}

	public BigDecimal getInitPriceD() {
		return initPriceD;
	}

	public void setInitPriceD(BigDecimal initPriceD) {
		this.initPriceD = initPriceD;
	}

	public String getInitPriceKStr() {
		return FormatNumber.num2Str(this.initPriceK);
	}

	public void setInitPriceKStr(String initPriceKStr) throws ParseException {
		this.initPriceK = FormatNumber.str2num(initPriceKStr);
	}

	public String getInitPriceDStr() {
		return FormatNumber.num2Str(this.initPriceD);
	}

	public void setInitPriceDStr(String initPriceDStr) throws ParseException {
		this.initPriceD = FormatNumber.str2num(initPriceDStr);
	}

	public String getInitPriceTBStr() {
		return FormatNumber.num2Str(this.initPriceTB);
	}

	public void setInitPriceTBStr(String initPriceTBStr) throws ParseException {
		this.initPriceTB = FormatNumber.str2num(initPriceTBStr);
	}
}
