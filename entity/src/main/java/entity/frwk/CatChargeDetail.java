package entity.frwk;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.FormatNumber;

@Entity
@Table(name = "CAT_CHARGE_DETAIL")
public class CatChargeDetail {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@JoinColumn(name = "SYS_DICT_PARAM_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private BssParam freeType;

	@JoinColumn(name = "CAT_CHARGE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JsonIgnore
	private CatCharge catCharge;

	@Column(name = "VALUE")
	private BigDecimal value;
	@Transient
	private String valueStr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BssParam getFreeType() {
		return freeType;
	}

	public void setFreeType(BssParam freeType) {
		this.freeType = freeType;
	}

	public CatCharge getCatCharge() {
		return catCharge;
	}

	public void setCatCharge(CatCharge catCharge) {
		this.catCharge = catCharge;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getValueStr() {
		return FormatNumber.num2Str(this.value);
	}

	public void setValueStr(String valueStr) throws ParseException {
		this.value = FormatNumber.str2num(this.valueStr);
	}

}
