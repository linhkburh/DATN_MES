/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import common.util.FormatNumber;
import common.util.Formater;

/**
 *
 * @author mamam
 */
@Entity
@Table(name = "REDUCE_BY_AMOUNT")
public class ReduceByAmount implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@Column(name = "AMOUNT_FROM")
	private Long amountFrom;
	@Column(name = "AMOUNT_TO")
	private Long amountTo;
	@Column(name = "AMOUNT_REDUCE")
	private BigDecimal amountReduce;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "CODE")
	private String code;

	@Transient
	private String amountFromStr;
	@Transient
	private String amountToStr;
	@Transient
	private String amountReduceStr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(Long amountFrom) {
		this.amountFrom = amountFrom;
	}

	public Long getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(Long amountTo) {
		this.amountTo = amountTo;
	}

	public BigDecimal getAmountReduce() {
		return amountReduce;
	}

	public void setAmountReduce(BigDecimal amountReduce) {
		this.amountReduce = amountReduce;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAmountFromStr() {
		if (this.amountFrom != null)
			return Formater.num2str(this.amountFrom);
		else return this.amountFromStr;
	}

	public void setAmountFromStr(String amountFromStr) throws ParseException {
		this.amountFrom = FormatNumber.str2Long(amountFromStr);
		this.amountFromStr = amountFromStr;
	}

	public String getAmountToStr() {
		if (this.amountTo != null)
			return Formater.num2str(this.amountTo);
		else return this.amountToStr;
	}

	public void setAmountToStr(String amountToStr) throws ParseException {
		this.amountTo = FormatNumber.str2Long(amountToStr);
		this.amountToStr = amountToStr;
	}

	public String getAmountReduceStr() {
		if (this.amountReduce != null)
			return FormatNumber.num2Str(this.amountReduce);
		else return this.amountReduceStr;
	}

	public void setAmountReduceStr(String amountReduceStr) throws ParseException {
		this.amountReduce = FormatNumber.str2num(amountReduceStr);
		this.amountReduceStr = amountReduceStr;
	}

}
