/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.constants.Constants;
import common.util.FormatNumber;
import common.util.Formater;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "QUOTATION_ITEM_MATERIAL")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuotationItemMaterial implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "SIZE_LONG")
	private BigDecimal sizeLong;
	@Column(name = "SIZE_WIDTH")
	private BigDecimal sizeWidth;
	@Column(name = "SIZE_HEIGHT")
	private BigDecimal sizeHeight;
	@Column(name = "SIZE_WEIGH")
	private BigDecimal sizeWeigh;
	@Column(name = "QUALITY")
	private BigDecimal quality;
	@JoinColumn(name = "MARTERIAL_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Material marterialId;
	@JoinColumn(name = "QUOTATION_ITEM_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	@JsonIgnore
	@XmlTransient
	private QuotationItem quotationItemId;
	@JoinColumn(name = "MARTERIAL_BACKUP_ID", referencedColumnName = "ID")
	@ManyToOne(optional = true)
	private Material marteriaBackupId;
	@Column(name = "PRICE")
	private BigDecimal price;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public QuotationItemMaterial() {
	}

	public QuotationItemMaterial(String id) {
		this.id = id;
	}

	@Transient
	private BigDecimal exchangRate;
	@Transient
	private Boolean isChangeSizeWeight;
	private BigDecimal waste;

	public BigDecimal getWaste() {
		return waste;
	}

	public void setWaste(BigDecimal waste) {
		this.waste = waste;
	}

	@Transient
	public String getWasteStr() {
		return FormatNumber.num2Str(this.waste);
	}

	public void setWasteStr(String wasteStr) throws ParseException {
		this.waste = FormatNumber.str2num(wasteStr);

	}
	
	@Column(name="ANGLE_WORKPIECE")
	private BigDecimal angleWorkpiece;
	@Column(name="COLD_ROLLED")
	private String coldRolled;
	@Column(name="TECHNICAL_NAME")
	private String technicalName;
	
	@Column(name="EMBRYO_NOTE")
	private String embryoNote;
	
	@Column(name="SOURCE_PRO")
	private String sourcePro;

	public QuotationItemMaterial(Material marterialId, Material marteriaBackupId, BigDecimal sizeLong,
			BigDecimal sizeWidth, BigDecimal sizeHeight, BigDecimal sizeWeigh, BigDecimal quality,
			BigDecimal exchangRate, Boolean isChangeSizeWeight, String currenPriceStr, BigDecimal waste) {
		this.marterialId = marterialId;
		this.marteriaBackupId = marteriaBackupId;
		this.sizeLong = sizeLong;
		this.sizeWidth = sizeWidth;
		this.sizeHeight = sizeHeight;
		this.quality = quality;
		this.sizeWeigh = sizeWeigh;
		this.exchangRate = exchangRate;
		this.isChangeSizeWeight = isChangeSizeWeight;
		this.waste = waste;
		calulatePrice();
	}

	public QuotationItemMaterial(QuotationItem item, Material material) {
		this.quotationItemId = item;
		this.marterialId = material;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSizeLong() {
		return sizeLong;
	}

	public void setSizeLong(BigDecimal sizeLong) {
		this.sizeLong = sizeLong;
	}

	public String getSizeLongStr() {
		return FormatNumber.num2Str(this.sizeLong);
	}

	public void setSizeLongStr(String sizeLongStr) throws ParseException {
		this.sizeLong = FormatNumber.str2num(sizeLongStr);
	}

	public BigDecimal getSizeWidth() {
		return sizeWidth;
	}

	public void setSizeWidth(BigDecimal sizeWidth) {
		this.sizeWidth = sizeWidth;
	}

	public String getSizeWidthStr() {
		return FormatNumber.num2Str(this.sizeWidth);
	}

	public void setSizeWidthStr(String sizeWidthStr) throws ParseException {
		this.sizeWidth = FormatNumber.str2num(sizeWidthStr);
	}

	public BigDecimal getSizeHeight() {
		return sizeHeight;
	}

	public void setSizeHeight(BigDecimal sizeHeight) {
		this.sizeHeight = sizeHeight;
	}

	public String getSizeHeightStr() {
		return FormatNumber.num2Str(this.sizeHeight);
	}

	public void setSizeHeightStr(String sizeHeightStr) throws ParseException {
		this.sizeHeight = FormatNumber.str2num(sizeHeightStr);
	}

	public BigDecimal getQuality() {
		return quality;
	}

	public void setQuality(BigDecimal quality) {
		this.quality = quality;
	}

	public String getQualityStr() {
		return FormatNumber.num2Str(this.quality);
	}

	public void setQualityStr(String qualityStr) throws ParseException {
		this.quality = FormatNumber.str2num(qualityStr);
	}

	public Material getMarterialId() {
		return marterialId;
	}

	public void setMarterialId(Material marterialId) {
		this.marterialId = marterialId;
	}

	public QuotationItem getQuotationItemId() {
		return quotationItemId;
	}

	public void setQuotationItemId(QuotationItem quotationItemId) {
		this.quotationItemId = quotationItemId;
	}

	public BigDecimal getSizeWeigh() {
		return sizeWeigh;
	}

	public void setSizeWeigh(BigDecimal sizeWeigh) {
		this.sizeWeigh = sizeWeigh;
	}

	public String getSizeWeighStr() {
		return FormatNumber.num2Str(this.sizeWeigh);
	}

	public void setSizeWeighStr(String sizeWeighStr) throws ParseException {
		this.sizeWeigh = FormatNumber.str2num(sizeWeighStr);
	}

	public Material getMarteriaBackupId() {
		return marteriaBackupId;
	}

	public void setMarteriaBackupId(Material marteriaBackupId) {
		this.marteriaBackupId = marteriaBackupId;
	}

	@Transient
	public String getVolumnStr() {
		return FormatNumber.num2Str(getVolumn());
	}

	@Transient
	public BigDecimal getVolumn() {
		if (this.getSizeWidth() == null || this.getSizeHeight() == null)
			return null;
		// The tich vuong
		if (this.getSizeLong() != null)
			return this.getSizeLong().multiply(this.getSizeWidth()).multiply(this.getSizeHeight());
		// The tich tru
		BigDecimal r = this.getSizeWidth().divide(BigDecimal.valueOf(2));
		Double r2 = Math.pow(r.doubleValue(), Double.valueOf(2));
		return BigDecimal.valueOf(Math.PI).multiply(BigDecimal.valueOf(r2)).multiply(this.getSizeHeight());

	}

	public String getPriceStr() {
		if (!caculatePrice)
			getPrice();
		return FormatNumber.num2Str(this.price);
	}

	public BigDecimal calulatePrice() {
		Material material = this.marteriaBackupId == null ? this.marterialId : this.marteriaBackupId;
		caculatePrice = Boolean.TRUE;
		BigDecimal dimention = null;
		if (isChangeSizeWeight != null && isChangeSizeWeight) {
			if (Constants.MEASR_MSS.equals(material.getUnit().getValue().trim()))
				dimention = this.getSizeWeigh();
			else
				return this.price;
		} else {
			if (material.getUnit() == null || material.getUnit() == null
					|| Formater.isNull(material.getUnit().getValue()))
				return null;
			if (Constants.MEASR_LENTH.equals(material.getUnit().getValue().trim())) {
				if (this.getSizeLong() == null)
					return null;
				dimention = this.getSizeLong();
			} else if (Constants.MEASR_AREA.equals(material.getUnit().getValue().trim())) {
				if (this.getSizeLong() == null || this.getSizeWidth() == null)
					return null;
				dimention = this.getSizeLong().multiply(this.getSizeWidth());
			} else if (Constants.MEASR_VOLUM.equals(material.getUnit().getValue().trim())) {
				dimention = getVolumn();
				if (this.getSizeWeigh() == null || new BigDecimal(0).compareTo(this.getSizeWeigh()) == 0) {
					if (this.getVolumn() != null && material.getDensity() != null)
						this.setSizeWeigh(this.getVolumn().multiply(material.getDensity()));
				}
			} else if (Constants.MEASR_MSS.equals(material.getUnit().getValue().trim())) {
				if (this.getSizeWeigh() == null || new BigDecimal(0).compareTo(this.getSizeWeigh()) == 0) {
					if (this.getVolumn() != null && material.getDensity() != null)
						this.setSizeWeigh(this.getVolumn().multiply(material.getDensity()));
				}
				dimention = this.getSizeWeigh();
			}
		}
		if (material.getInitPrice() == null)
			return null;
		if (dimention == null)
			return null;

		// Muc do hao ton
		if (this.waste != null && this.waste.compareTo(new BigDecimal(0)) > 0) {
			BigDecimal wasteAmount = dimention.multiply(this.waste).divide(new BigDecimal(100), 3,
					RoundingMode.HALF_UP);
			dimention = dimention.add(wasteAmount);
		}

		this.price = dimention.multiply(material.getInitPrice());

		// Nhan so luong
		if (this.getQuality() != null && new BigDecimal(0).compareTo(this.quality) != 0)
			this.price = this.price.multiply(this.quality);

		return this.price;
	}

	@Transient
	private Boolean caculatePrice = Boolean.FALSE;

	@Transient
	public String getDensityStr() {
		if (this.marteriaBackupId != null)
			return this.marteriaBackupId.getDensityStr();
		if (this.marterialId != null)
			return this.marterialId.getDensityStr();
		return null;
	}

	@Transient
	public String getInitPriceStr() {
		if (this.marteriaBackupId != null)
			return this.marteriaBackupId.getInitPriceStr();
		if (this.marterialId != null)
			return this.marterialId.getInitPriceStr();
		return null;
	}

	public BigDecimal getAngleWorkpiece() {
		return angleWorkpiece;
	}

	public void setAngleWorkpiece(BigDecimal angleWorkpiece) {
		this.angleWorkpiece = angleWorkpiece;
	}

	public String getColdRolled() {
		return coldRolled;
	}

	public void setColdRolled(String coldRolled) {
		this.coldRolled = coldRolled;
	}

	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	public String getEmbryoNote() {
		return embryoNote;
	}

	public void setEmbryoNote(String embryoNote) {
		this.embryoNote = embryoNote;
	}

	public String getSourcePro() {
		return sourcePro;
	}

	public void setSourcePro(String sourcePro) {
		this.sourcePro = sourcePro;
	}
}
