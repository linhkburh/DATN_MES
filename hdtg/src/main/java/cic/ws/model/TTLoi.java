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
 * <p>Java class for TTLoi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TTLoi"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MaChiTieu" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Ma" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MoTa" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTLoi", propOrder = {
    "maChiTieu",
    "ma",
    "moTa"
})
public class TTLoi {

    @XmlElement(name = "MaChiTieu", required = true)
    protected String maChiTieu;
    @XmlElement(name = "Ma", required = true)
    protected String ma;
    @XmlElement(name = "MoTa", required = true)
    protected String moTa;

    /**
     * Gets the value of the maChiTieu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaChiTieu() {
        return maChiTieu;
    }

    /**
     * Sets the value of the maChiTieu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaChiTieu(String value) {
        this.maChiTieu = value;
    }

    /**
     * Gets the value of the ma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMa() {
        return ma;
    }

    /**
     * Sets the value of the ma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMa(String value) {
        this.ma = value;
    }

    /**
     * Gets the value of the moTa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoTa() {
        return moTa;
    }

    /**
     * Sets the value of the moTa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoTa(String value) {
        this.moTa = value;
    }

}