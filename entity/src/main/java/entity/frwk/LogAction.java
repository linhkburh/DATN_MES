package entity.frwk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_LOG_ACTION", uniqueConstraints = @UniqueConstraint(columnNames = "ID"))
public class LogAction implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
    private String className;
    private String fncName;
    
    @Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "CLASSNAME", nullable = false, length = 150)
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name = "FNCNAME", nullable = false, length = 300)
	public String getFncName() {
		return fncName;
	}
	public void setFncName(String fncName) {
		this.fncName = fncName;
	}
    
}
