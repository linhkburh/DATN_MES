package entity.frwk;

// Generated Feb 20, 2023 11:10:47 AM by Hibernate Tools 5.2.12.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * SysFile generated by hbm2java
 */
@Entity
@Table(name = "SYS_FILE")
public class SysFile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private SysDictParam sysDictParam;
	private SysUsers sysUsers;
	private String name;
	private Date uploadDate;
	private String descripton;
	private String owner;
	private String recordId;
	private Double fileSize;

	private SysUsers modifiedBy;
	private Date modifiedDate;

	public SysFile() {
	}

	public SysFile(String id, SysUsers sysUsers, String name, Date uploadDate) {
		this.id = id;
		this.sysUsers = sysUsers;
		this.name = name;
		this.uploadDate = uploadDate;
	}

	public SysFile(String id, SysDictParam sysDictParam, SysUsers sysUsers,
			String name, Date uploadDate, String descripton, String owner) {
		this.id = id;
		this.sysDictParam = sysDictParam;
		this.sysUsers = sysUsers;
		this.name = name;
		this.uploadDate = uploadDate;
		this.descripton = descripton;
		this.owner = owner;
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
	@JoinColumn(name = "TYPE")
	public SysDictParam getSysDictParam() {
		return this.sysDictParam;
	}

	public void setSysDictParam(SysDictParam sysDictParam) {
		this.sysDictParam = sysDictParam;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOADER", nullable = false)
	public SysUsers getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(SysUsers sysUsers) {
		this.sysUsers = sysUsers;
	}

	@Column(name = "NAME", nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPLOAD_DATE", nullable = false)
	public Date getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	@Column(name = "DESCRIPTON", length = 400)
	public String getDescripton() {
		return this.descripton;
	}

	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}

	@Column(name = "OWNER", length = 40)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	
	@Column(name = "RECORD_ID", length = 40)
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY")
	public SysUsers getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(SysUsers modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name = "FILE_SIZE")
	public Double getFileSize() {
		return fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

}
