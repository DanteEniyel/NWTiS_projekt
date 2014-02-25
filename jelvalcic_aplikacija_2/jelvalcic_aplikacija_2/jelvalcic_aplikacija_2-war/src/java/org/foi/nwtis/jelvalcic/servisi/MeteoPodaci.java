
package org.foi.nwtis.jelvalcic.servisi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for meteoPodaci complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="meteoPodaci">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="smjerVjetra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="temperatura" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="tlak" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="vjetar" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="vlaga" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="vraceniGrad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vraceniZipKod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zahtjevaniGrad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zahtjevaniZipKod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "meteoPodaci", propOrder = {
    "smjerVjetra",
    "temperatura",
    "tlak",
    "vjetar",
    "vlaga",
    "vraceniGrad",
    "vraceniZipKod",
    "zahtjevaniGrad",
    "zahtjevaniZipKod"
})
public class MeteoPodaci {

    protected String smjerVjetra;
    protected float temperatura;
    protected float tlak;
    protected float vjetar;
    protected float vlaga;
    protected String vraceniGrad;
    protected String vraceniZipKod;
    protected String zahtjevaniGrad;
    protected String zahtjevaniZipKod;

    /**
     * Gets the value of the smjerVjetra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmjerVjetra() {
        return smjerVjetra;
    }

    /**
     * Sets the value of the smjerVjetra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmjerVjetra(String value) {
        this.smjerVjetra = value;
    }

    /**
     * Gets the value of the temperatura property.
     * 
     */
    public float getTemperatura() {
        return temperatura;
    }

    /**
     * Sets the value of the temperatura property.
     * 
     */
    public void setTemperatura(float value) {
        this.temperatura = value;
    }

    /**
     * Gets the value of the tlak property.
     * 
     */
    public float getTlak() {
        return tlak;
    }

    /**
     * Sets the value of the tlak property.
     * 
     */
    public void setTlak(float value) {
        this.tlak = value;
    }

    /**
     * Gets the value of the vjetar property.
     * 
     */
    public float getVjetar() {
        return vjetar;
    }

    /**
     * Sets the value of the vjetar property.
     * 
     */
    public void setVjetar(float value) {
        this.vjetar = value;
    }

    /**
     * Gets the value of the vlaga property.
     * 
     */
    public float getVlaga() {
        return vlaga;
    }

    /**
     * Sets the value of the vlaga property.
     * 
     */
    public void setVlaga(float value) {
        this.vlaga = value;
    }

    /**
     * Gets the value of the vraceniGrad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVraceniGrad() {
        return vraceniGrad;
    }

    /**
     * Sets the value of the vraceniGrad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVraceniGrad(String value) {
        this.vraceniGrad = value;
    }

    /**
     * Gets the value of the vraceniZipKod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVraceniZipKod() {
        return vraceniZipKod;
    }

    /**
     * Sets the value of the vraceniZipKod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVraceniZipKod(String value) {
        this.vraceniZipKod = value;
    }

    /**
     * Gets the value of the zahtjevaniGrad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZahtjevaniGrad() {
        return zahtjevaniGrad;
    }

    /**
     * Sets the value of the zahtjevaniGrad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZahtjevaniGrad(String value) {
        this.zahtjevaniGrad = value;
    }

    /**
     * Gets the value of the zahtjevaniZipKod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZahtjevaniZipKod() {
        return zahtjevaniZipKod;
    }

    /**
     * Sets the value of the zahtjevaniZipKod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZahtjevaniZipKod(String value) {
        this.zahtjevaniZipKod = value;
    }

}
