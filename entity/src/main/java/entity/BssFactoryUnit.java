package entity;
// Generated Feb 27, 2023 10:40:20 AM by Hibernate Tools 5.2.12.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * BssFactoryUnit generated by hbm2java
 */
@Entity
@Table(name = "BSS_FACTORY_UNIT", uniqueConstraints = @UniqueConstraint(columnNames = { "CODE",
		"COMPANY_ID" }))
public class BssFactoryUnit implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private Company company;
	private String code;
	private String name;

	public BssFactoryUnit() {
	}

	public BssFactoryUnit(String id, Company company, String code, String name) {
		this.id = id;
		this.company = company;
		this.code = code;
		this.name = name;
	}

	public BssFactoryUnit(String id, Company company, String code, String name, Set quotationItems) {
		this.id = id;
		this.company = company;
		this.code = code;
		this.name = name;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID", nullable = false)
	@JsonIgnore
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name = "CODE", nullable = false, length = 100)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 400)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
