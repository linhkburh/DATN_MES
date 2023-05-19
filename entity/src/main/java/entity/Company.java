package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import entity.frwk.SysUsers;

@Entity
@Table(name = "COMPANY")
public class Company implements Serializable {

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

	@Column(name = "SORT_NAME")
	private String sortName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "BSS_REG_CERT")
	private String bssRegCert;

	public Company() {
		super();
	}

	public Company(String companyId) {
		this.id = companyId;
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

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBssRegCert() {
		return bssRegCert;
	}

	public void setBssRegCert(String bssRegCert) {
		this.bssRegCert = bssRegCert;
	}

	@Transient
	private List<AstMachine> lstAstMachine;

	@JsonIgnoreProperties("company")
	public List<AstMachine> getLstAstMachine() {
		return lstAstMachine;
	}

	public void setLstAstMachine(List<AstMachine> lstAstMachine) {
		this.lstAstMachine = lstAstMachine;
	}

	@Transient
	private List<SysUsers> lstSysUsers;

	@JsonProperty
	@JsonIgnoreProperties({ "company", "department" })
	public List<SysUsers> getLstSysUsers() {
		return lstSysUsers;
	}

	public void setLstSysUsers(List<SysUsers> lstSysUsers) {
		this.lstSysUsers = lstSysUsers;
	}
	@Transient
	private List<BssFactoryUnit> lstBssFactoryInit;

	public List<BssFactoryUnit> getLstBssFactoryInit() {
		return lstBssFactoryInit;
	}

	public void setLstBssFactoryInit(List<BssFactoryUnit> lstBssFactoryInit) {
		this.lstBssFactoryInit = lstBssFactoryInit;
	}

	@Transient
	private List<Department> lstDepartment;

	public List<Department> getLstDepartment() {
		return lstDepartment;
	}

	public void setLstDepartment(List<Department> lstDepartment) {
		this.lstDepartment = lstDepartment;
	}
}
