/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.FormatNumber;
import entity.frwk.SysDictParam;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "MATERIAL")
public class Material implements Serializable, Cloneable {

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
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "DENSITY")
	private BigDecimal density;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marterialId")
//    private List<QuotationItemMaterial> quotationItemMaterialList;
	@OneToMany(mappedBy = "previousVersion")
	@JsonIgnore
	private List<Material> materialList;
	@JoinColumn(name = "PREVIOUS_VERSION", referencedColumnName = "ID")
	@ManyToOne
	private Material previousVersion;
	@Column(name = "INIT_PRICE")
	private BigDecimal initPrice;

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	@JoinColumn(name = "UNIT")
	@ManyToOne(fetch = FetchType.EAGER)
	private SysDictParam unit;

	@JoinColumn(name = "MATERIAL_TYPE")
	@ManyToOne(fetch = FetchType.EAGER)
	private MaterialType materialType;

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	@Override
	public Material clone() {
		Material clone = this.clone();
		clone.setId(null);
		clone.setPreviousVersion(this);
		return clone;
	}

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CURRENCY", referencedColumnName = "ID")
	private SysDictParam currency;

	public SysDictParam getCurrency() {
		return currency;
	}

	public void setCurrency(SysDictParam currency) {
		this.currency = currency;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Material() {
	}

	public Material(String id) {
		this.id = id;
	}

	public Material(String id, String code) {
		this.id = id;
		this.code = code;
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

	public BigDecimal getDensity() {
		return density;
	}

	public void setDensity(BigDecimal density) {
		this.density = density;
	}

	public String getDensityStr() {
		return FormatNumber.num2Str(this.density);
	}

	public void setDensityStr(String densityStr) throws ParseException {
		this.density = FormatNumber.str2num(densityStr);
	}
//    public List<QuotationItemMaterial> getQuotationItemMaterialList() {
//        return quotationItemMaterialList;
//    }
//
//    public void setQuotationItemMaterialList(List<QuotationItemMaterial> quotationItemMaterialList) {
//        this.quotationItemMaterialList = quotationItemMaterialList;
//    }

	public List<Material> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<Material> materialList) {
		this.materialList = materialList;
	}

	public Material getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(Material previousVersion) {
		this.previousVersion = previousVersion;
	}

	public SysDictParam getUnit() {
		return unit;
	}

	public void setUnit(SysDictParam unit) {
		this.unit = unit;
	}

	public String getInitPriceStr() {
		return FormatNumber.num2Str(this.initPrice);
	}

	public void setInitPriceStr(String initPriceStr) throws ParseException {
		this.initPrice = FormatNumber.str2num(initPriceStr);
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

}
