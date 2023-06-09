package entity.frwk;

import java.util.ArrayList;

// Generated Nov 13, 2019 6:00:05 PM by Hibernate Tools 3.4.0.CR1

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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.util.Formater;
import entity.Customer;
import entity.Department;

/**
 * SysUsers generated by hbm2java
 */
@Entity
@Table(name = "SYS_USERS", uniqueConstraints = @UniqueConstraint(columnNames = "USERNAME"))
@XmlAccessorType(XmlAccessType.FIELD)
public class SysUsers implements java.io.Serializable {

	private String id;
	private String name;
	@JsonIgnore
	private String password;
	private Boolean active;
	private Date pwdDate;
	private String pwdDatestr;
	private String passWordPlainText;

	private String pWExpriredDateStr;

	@Transient
	public String getpWExpriredDateStr() {
		return pWExpriredDateStr;
	}

	public void setpWExpriredDateStr(String pWExpriredDateStr) {
		this.pWExpriredDateStr = pWExpriredDateStr;
	}

	@Transient
	public String getPassWordPlainText() {
		return passWordPlainText;
	}

	public void setPassWordPlainText(String passWordPlainText) {
		this.passWordPlainText = passWordPlainText;
	}

	@Transient
	public String getPwdDatestr() {
		return pwdDatestr;
	}

	public void setPwdDatestr(String pwdDatestr) throws Exception {
		this.pwdDatestr = pwdDatestr;
		this.pwdDate = Formater.str2date(pwdDatestr);
	}

	private String companyName;
	private String departmentName;
	private String jobName;
	private String username;
	private Date startDate;
	private Date endDate;
	private String pathOwner;
	private String pathCreate;
	private Date auditDate;
	private String nameEn;
	private String companyNameEn;
	private String jobId;
	private String departmentNameEn;
	private String jobNameEn;
	private String email;
	private String gender;
	private String nationality;
	private String phone;
	private String cellPhone;
	private String identityPaper;
	private String identification;
	private String secDiviceCode;
	private String transltd;
	private String dayltd;
	private Date dateIssue;
	private String strdateIssue;

	private String passwordStatus;

	private boolean enableService;

	private Date blockTime;

	private long loginErrorTimes;

	@Column(name = "LOGIN_ERROR_TIMES")
	public long getLoginErrorTimes() {
		return loginErrorTimes;
	}

	public void setLoginErrorTimes(long loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}

	@Transient
	public String getStrdateIssue() {
		return strdateIssue;
	}

	public void setStrdateIssue(String strdateIssue) throws Exception {
		this.strdateIssue = strdateIssue;
		this.dateIssue = Formater.str2date(strdateIssue);
	}

	private String placeIssue;
	private String pin;
	private Boolean creatersa;
	private Boolean assignrsa;
	private Boolean createpin;
	private Boolean usersa;
	private String frtransltd;
	private String frdayltd;
	private Boolean cqBhxh;

	private entity.Company company;

	private Department department;

	@JsonIgnore
	@XmlTransient
	private List<SysRolesUsers> sysRolesUserses = new ArrayList<SysRolesUsers>();
	/**
	 * Danh sach email canh bao
	 */
	@JsonIgnoreProperties("sysUsers")
	private List<SysUserMail> lstMail = new ArrayList<SysUserMail>();

	private String roles;

	private SysUsers creator, modifier;
	@JsonProperty
	private String strDepartment;

	@JsonProperty
	private String strBssFactoryUnits;

	@Transient
	public String getStrDepartment() {
		return strDepartment;
	}

	public void setStrDepartment(String strDepartment) {
		this.strDepartment = strDepartment;
	}

	@Transient
	public String getStrBssFactoryUnits() {
		return strBssFactoryUnits;
	}

	public void setStrBssFactoryUnits(String strBssFactoryUnits) {
		this.strBssFactoryUnits = strBssFactoryUnits;
	}

	@Transient
	public SysUsers getCreator() {
		return creator;
	}

	public void setCreator(SysUsers creator) {
		this.creator = creator;
	}

	@Transient
	public SysUsers getModifier() {
		return modifier;
	}

	public void setModifier(SysUsers modifier) {
		this.modifier = modifier;
	}

	@Transient
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public SysUsers() {
	}

	public SysUsers(String id, String name, String username) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
	}

	public SysUsers(String id, String name, boolean active, Date pwdDate, String username) {
		this.id = id;
		this.name = name;
		this.active = active;
		this.pwdDate = pwdDate;
		this.username = username;
	}

	public SysUsers(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID", unique = true, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 3000)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	@Column(name = "PASSWORD", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "ACTIVE", precision = 1, scale = 0)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PWD_DATE", length = 7)
	public Date getPwdDate() {
		return this.pwdDate;
	}

	public void setPwdDate(Date pwdDate) {
		this.pwdDate = pwdDate;
		this.pwdDatestr = Formater.date2str(pwdDate);
	}

	@Column(name = "COMPANY_NAME", length = 3000)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "DEPARTMENT_NAME", length = 3000)
	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@JoinColumn(name = "COMPANY_ID")
	@ManyToOne
	public entity.Company getCompany() {
		return company;
	}

	public void setCompany(entity.Company company) {
		this.company = company;
	}

	@JoinColumn(name = "DEPARTMENT_ID")
	@ManyToOne
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "JOB_NAME", length = 3000)
	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "USERNAME", unique = true, length = 3000)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "PATH_OWNER", length = 3000)
	public String getPathOwner() {
		return this.pathOwner;
	}

	public void setPathOwner(String pathOwner) {
		this.pathOwner = pathOwner;
	}

	@Column(name = "PATH_CREATE", length = 3000)
	public String getPathCreate() {
		return this.pathCreate;
	}

	public void setPathCreate(String pathCreate) {
		this.pathCreate = pathCreate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUDIT_DATE", length = 7)
	public Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	@Column(name = "NAME_EN", length = 3000)
	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "COMPANY_NAME_EN", length = 3000)
	public String getCompanyNameEn() {
		return this.companyNameEn;
	}

	public void setCompanyNameEn(String companyNameEn) {
		this.companyNameEn = companyNameEn;
	}

	@Column(name = "JOB_ID", length = 40)
	public String getJobId() {
		return this.jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Column(name = "DEPARTMENT_NAME_EN", length = 3000)
	public String getDepartmentNameEn() {
		return this.departmentNameEn;
	}

	public void setDepartmentNameEn(String departmentNameEn) {
		this.departmentNameEn = departmentNameEn;
	}

	@Column(name = "JOB_NAME_EN", length = 3000)
	public String getJobNameEn() {
		return this.jobNameEn;
	}

	public void setJobNameEn(String jobNameEn) {
		this.jobNameEn = jobNameEn;
	}

	@Column(name = "EMAIL", length = 200)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "GENDER", length = 40)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "NATIONALITY", length = 100)
	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Column(name = "PHONE", length = 40)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "CELL_PHONE", length = 40)
	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	@Column(name = "IDENTITY_PAPER", length = 40)
	public String getIdentityPaper() {
		return this.identityPaper;
	}

	public void setIdentityPaper(String identityPaper) {
		this.identityPaper = identityPaper;
	}

	@Column(name = "IDENTIFICATION", length = 40)
	public String getIdentification() {
		return this.identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@Column(name = "SEC_DIVICE_CODE", length = 40)
	public String getSecDiviceCode() {
		return this.secDiviceCode;
	}

	public void setSecDiviceCode(String secDiviceCode) {
		this.secDiviceCode = secDiviceCode;
	}

	@Column(name = "TRANSLTD", length = 40)
	public String getTransltd() {
		return this.transltd;
	}

	public void setTransltd(String transltd) {
		this.transltd = transltd;
	}

	@Column(name = "DAYLTD", length = 40)
	public String getDayltd() {
		return this.dayltd;
	}

	public void setDayltd(String dayltd) {
		this.dayltd = dayltd;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_ISSUE", length = 7)
	public Date getDateIssue() {
		return this.dateIssue;
	}

	public void setDateIssue(Date dateIssue) {
		this.dateIssue = dateIssue;
		this.strdateIssue = Formater.date2str(dateIssue);
	}

	@Column(name = "PLACE_ISSUE", length = 3000)
	public String getPlaceIssue() {
		return this.placeIssue;
	}

	public void setPlaceIssue(String placeIssue) {
		this.placeIssue = placeIssue;
	}

	@Column(name = "PIN", length = 120)
	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	@Column(name = "CREATERSA", precision = 1, scale = 0)
	public Boolean getCreatersa() {
		return this.creatersa;
	}

	public void setCreatersa(Boolean creatersa) {
		this.creatersa = creatersa;
	}

	@Column(name = "ASSIGNRSA", precision = 1, scale = 0)
	public Boolean getAssignrsa() {
		return this.assignrsa;
	}

	public void setAssignrsa(Boolean assignrsa) {
		this.assignrsa = assignrsa;
	}

	@Column(name = "CREATEPIN", precision = 1, scale = 0)
	public Boolean getCreatepin() {
		return this.createpin;
	}

	public void setCreatepin(Boolean createpin) {
		this.createpin = createpin;
	}

	@Column(name = "USERSA", precision = 1, scale = 0)
	public Boolean getUsersa() {
		return this.usersa;
	}

	public void setUsersa(Boolean usersa) {
		this.usersa = usersa;
	}

	@Column(name = "FRTRANSLTD", length = 40)
	public String getFrtransltd() {
		return this.frtransltd;
	}

	public void setFrtransltd(String frtransltd) {
		this.frtransltd = frtransltd;
	}

	@Column(name = "FRDAYLTD", length = 40)
	public String getFrdayltd() {
		return this.frdayltd;
	}

	public void setFrdayltd(String frdayltd) {
		this.frdayltd = frdayltd;
	}

	@Column(name = "CQ_BHXH", precision = 1, scale = 0)
	public Boolean getCqBhxh() {
		return this.cqBhxh;
	}

	public void setCqBhxh(Boolean cqBhxh) {
		this.cqBhxh = cqBhxh;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysUsers", cascade = CascadeType.ALL)
	public List<SysRolesUsers> getSysRolesUserses() {
		return this.sysRolesUserses;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysUsers", cascade = CascadeType.ALL)
	public List<SysUserMail> getLstMail() {
		return lstMail;
	}

	public void setLstMail(List<SysUserMail> lstMail) {
		this.lstMail = lstMail;
	}

	public void setSysRolesUserses(List<SysRolesUsers> sysRolesUserses) {
		this.sysRolesUserses = sysRolesUserses;
	}

	private Boolean pssWordValidTime;

	@Transient
	public Boolean getPssWordValidTime() {
		return pssWordValidTime;
	}

	public void setPssWordValidTime(Boolean pssWordValidTime) {
		this.pssWordValidTime = pssWordValidTime;
	}

	private int expireDay;

	@Transient
	public int getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(int expireDay) {
		this.expireDay = expireDay;
	}

	@Column(name = "PASSWORD_STATUS", length = 1)
	public String getPasswordStatus() {
		return passwordStatus;
	}

	public void setPasswordStatus(String passwordStatus) {
		this.passwordStatus = passwordStatus;
	}

	@Column(name = "ENABLE_SERVICE", length = 1)
	public boolean getEnableService() {
		return enableService;
	}

	public void setEnableService(boolean enableService) {
		this.enableService = enableService;
	}

	@Column(name = "BLOCK_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(Date blockTime) {
		this.blockTime = blockTime;
	}

	private Customer partner;
	private Boolean isPartner;

	@JoinColumn(name = "PARTNER_ID")
	@ManyToOne
	public Customer getPartner() {
		return partner;
	}

	public void setPartner(Customer partner) {
		this.partner = partner;
	}

	@Column(name = "IS_PARTNER")
	public Boolean getIsPartner() {
		return isPartner;
	}

	public void setIsPartner(Boolean isPartner) {
		this.isPartner = isPartner;
	}

	private String shift;

	@Column(name = "SHIFT", length = 40)
	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	private String factoryUnit;

	@Column(name = "FACTORY_UNIT", length = 40)
	public String getFactoryUnit() {
		return factoryUnit;
	}

	public void setFactoryUnit(String factoryUnit) {
		this.factoryUnit = factoryUnit;
	}

}
