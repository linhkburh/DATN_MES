package cic.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "http://api", propOrder = { "templateCode", "data", "format", "incFontSize" })
@XmlRootElement(name = "GenFile")
public class GenFile {

	@XmlElement(name = "TemplateCode", required = true)
	protected String templateCode;
	@XmlElement(name = "Data", required = true)
	protected String data;
	@XmlElement(name = "Format", required = true)
	protected String format;
	@XmlElement(name = "IncFontSize", required = true)
	protected String incFontSize;

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getIncFontSize() {
		return incFontSize;
	}

	public void setIncFontSize(String incFontSize) {
		this.incFontSize = incFontSize;
	}

}
