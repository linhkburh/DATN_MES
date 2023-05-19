package entity.frwk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_SECURITY")
public class SysSecurity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name = "CODE", length = 40)
	private String code;
	@Column(name = "NAME", length = 200)
	private String name;
	@Column(name = "VALUE", length = 200)
	private String value;

	@Column(name = "ACTIVE", length = 1)
	private boolean active;

	public SysSecurity() {
		super();
	}

	public SysSecurity(String id, String code, String name, String value, boolean active) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.value = value;
		this.active = active;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
