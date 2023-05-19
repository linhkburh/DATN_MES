package entity.frwk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_USER_MAUANL")
public class UserDocumentation implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private String name;
	private String description;
	private byte[] xmlModel;
	private String xmlCfgFileName;
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
	
	@Column(name = "CODE", unique = true, length = 40)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 400)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "XML_MODEL")
	public byte[] getXmlModel() {
		return this.xmlModel;
	}

	public void setXmlModel(byte[] xmlModel) {
		this.xmlModel = xmlModel;
	}
	
	@Column(name = "XML_MODEL_FILE_NAME")
	public String getXmlCfgFileName() {
		return xmlCfgFileName;
	}

	public void setXmlCfgFileName(String xmlCfgFileName) {
		this.xmlCfgFileName = xmlCfgFileName;
	}
	
}
