package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MACHINE_EXE_STEP_TYPE")
public class MachineExeStepType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JoinColumn(name = "MACHINE_ID")
	@ManyToOne(optional = false)
	@JsonIgnore
	@XmlTransient
	private AstMachine astMachine;

	@JoinColumn(name = "EXE_STEP_TYPE_ID")
	@ManyToOne(optional = true)
	private ExeStepType exeStepType;

	public MachineExeStepType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlTransient
	public AstMachine getAstMachine() {
		return astMachine;
	}

	public void setAstMachine(AstMachine astMachine) {
		this.astMachine = astMachine;
	}

	public ExeStepType getExeStepType() {
		return exeStepType;
	}

	public void setExeStepType(ExeStepType exeStepType) {
		this.exeStepType = exeStepType;
	}

}
