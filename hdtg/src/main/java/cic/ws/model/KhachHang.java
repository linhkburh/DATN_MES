//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.22 at 02:32:45 PM ICT 
//


package cic.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KhachHang complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KhachHang"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MaKH" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TenKH" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DongBatDau" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DSLoi" type="{http://www.endpoint.ws.cmc.com.vn/cicreport}DSLoi"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KhachHang", propOrder = {
    "maKH",
    "tenKH",
    "dongBatDau",
    "dsLoi"
})
public class KhachHang {

    @XmlElement(name = "MaKH", required = true)
    protected String maKH;
    @XmlElement(name = "TenKH", required = true)
    protected String tenKH;
    @XmlElement(name = "DongBatDau", required = true)
    protected String dongBatDau;
    @XmlElement(name = "DSLoi", required = true)
    protected DSLoi dsLoi;

    /**
     * Gets the value of the maKH property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaKH() {
        return maKH;
    }

    /**
     * Sets the value of the maKH property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaKH(String value) {
        this.maKH = value;
    }

    /**
     * Gets the value of the tenKH property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTenKH() {
        return tenKH;
    }

    /**
     * Sets the value of the tenKH property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTenKH(String value) {
        this.tenKH = value;
    }

    /**
     * Gets the value of the dongBatDau property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDongBatDau() {
        return dongBatDau;
    }

    /**
     * Sets the value of the dongBatDau property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDongBatDau(String value) {
        this.dongBatDau = value;
    }

    /**
     * Gets the value of the dsLoi property.
     * 
     * @return
     *     possible object is
     *     {@link DSLoi }
     *     
     */
    public DSLoi getDSLoi() {
        return dsLoi;
    }

    /**
     * Sets the value of the dsLoi property.
     * 
     * @param value
     *     allowed object is
     *     {@link DSLoi }
     *     
     */
    public void setDSLoi(DSLoi value) {
        this.dsLoi = value;
    }

}
