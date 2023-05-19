
package integration.genhub;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for genHubFile complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genHubFile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorDes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileContent" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genHubFile", propOrder = { "errorCode", "errorDes", "fileContent", "fileName", "fileType" })
public class GenHubFile implements IFile {

	protected String errorCode;
	protected String errorDes;
	protected byte[] fileContent;
	protected String fileName;
	protected Integer fileType;
	@XmlTransient
	private InputStream is;
	@XmlTransient
	private String xmlInput;

	/**
	 * Gets the value of the errorCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets the value of the errorCode property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setErrorCode(String value) {
		this.errorCode = value;
	}

	/**
	 * Gets the value of the errorDes property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getErrorDes() {
		return errorDes;
	}

	/**
	 * Sets the value of the errorDes property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setErrorDes(String value) {
		this.errorDes = value;
	}

	/**
	 * Gets the value of the fileContent property.
	 * 
	 * @return possible object is byte[]
	 */
	public byte[] getFileContent() {
		return fileContent;
	}

	/**
	 * Sets the value of the fileContent property.
	 * 
	 * @param value allowed object is byte[]
	 */
	public void setFileContent(byte[] value) {
		this.fileContent = value;
	}

	/**
	 * Gets the value of the fileName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the value of the fileName property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFileName(String value) {
		this.fileName = value;
	}

	/**
	 * Gets the value of the fileType property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getFileType() {
		return fileType;
	}

	/**
	 * Sets the value of the fileType property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setFileType(Integer value) {
		this.fileType = value;
	}

	@Override
	@XmlTransient
	public InputStream getIs() {
		if (this.fileContent == null || this.fileContent.length == 0)
			return null;
		if (this.is == null)
			this.is = new ByteArrayInputStream(this.fileContent);
		return is;
	}

	@Override
	@XmlTransient
	public Integer getContentLength() {
		return this.fileContent.length;
	}

	@Override
	public void close() throws Exception {
		if (is != null)
			is.close();
	}

	public String getXmlInput() {
		return xmlInput;
	}

	public void setXmlInput(String xmlInput) {
		this.xmlInput = xmlInput;
	}

}
