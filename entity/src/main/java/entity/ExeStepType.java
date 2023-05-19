package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Generated Nov 13, 2019 6:00:05 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SysParam generated by hbm2java
 */
@Entity
@Table(name = "EXE_STEP_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "CODE"))
@XmlAccessorType(XmlAccessType.FIELD)
public class ExeStepType implements java.io.Serializable {

	private String id;
	private String name;
	private String code;
	private String description;
	@XmlTransient
	private List<ExeStep> lstExeStep = new ArrayList<ExeStep>();

	public ExeStepType() {
	}

	public ExeStepType(String id, String name, String code, String description) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
	}

	public ExeStepType(String id) {
		this.id = id;
	}

	public ExeStepType(String id, String name) {
		this.id = id;
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

	@Column(name = "NAME", nullable = false, length = 400)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE", unique = true, nullable = false, length = 40)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "DESCRIPTION", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "stepType", fetch = FetchType.LAZY)
	@XmlTransient
	@JsonIgnore
	public List<ExeStep> getLstExeStep() {
		return lstExeStep;
	}

	public void setLstExeStep(List<ExeStep> lstExeStep) {
		this.lstExeStep = lstExeStep;
	}

	@JsonIgnoreProperties("stepType")
	@XmlTransient
	@Transient
	@JsonProperty
	private List<ExeStep> lstExe;
	@JsonIgnoreProperties("stepType")
	@XmlTransient
	@Transient
	@JsonProperty
	private List<ExeStep> lstExePro;

	public void sparateExeType() {
		lstExe = new ArrayList<ExeStep>();
		lstExePro = new ArrayList<ExeStep>();
		for (ExeStep exe : this.lstExeStep) {
			if (Boolean.TRUE.equals(exe.getProgram()))
				lstExePro.add(exe);
			else
				lstExe.add(exe);
		}
		Collections.sort(lstExePro, new Comparator<ExeStep>() {
			@Override
			public int compare(ExeStep o1, ExeStep o2) {
				return o1.getStepCode().compareTo(o2.getStepCode());
			}
		});
		Collections.sort(lstExe, new Comparator<ExeStep>() {
			@Override
			public int compare(ExeStep o1, ExeStep o2) {
				return o1.getStepCode().compareTo(o2.getStepCode());
			}
		});
	}

	@Transient
	public List<ExeStep> getLstExe() {
		return lstExe;
	}

	@Transient
	public List<ExeStep> getLstExePro() {
		return lstExePro;
	}

}
