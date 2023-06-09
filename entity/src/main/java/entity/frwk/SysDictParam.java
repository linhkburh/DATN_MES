package entity.frwk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * SysDictParam generated by hbm2java
 */
@Entity
@Table(name = "SYS_DICT_PARAM", uniqueConstraints = @UniqueConstraint(columnNames = { "TYPE",
		"CODE" }))
public class SysDictParam implements java.io.Serializable {

	private String id;
	private SysDictType sysDictType;
	private String code;
	private String value;
	private String description;
	private Short disOrder;

	public SysDictParam(String id, String code, String value, String description) {
		super();
		this.id = id;
		this.code = code;
		this.value = value;
		this.description = description;
	}

	public SysDictParam() {
	}

	public SysDictParam(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TYPE", referencedColumnName = "ID")
	public SysDictType getSysDictType() {
		return this.sysDictType;
	}

	public void setSysDictType(SysDictType sysDictType) {
		this.sysDictType = sysDictType;
	}

	@Column(name = "CODE", length = 40)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "VALUE", length = 200)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DESCRIPTION", length = 2000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DIS_ORDER", precision = 3, scale = 0)
	public Short getDisOrder() {
		return this.disOrder;
	}

	public void setDisOrder(Short disOrder) {
		this.disOrder = disOrder;
	}


}
