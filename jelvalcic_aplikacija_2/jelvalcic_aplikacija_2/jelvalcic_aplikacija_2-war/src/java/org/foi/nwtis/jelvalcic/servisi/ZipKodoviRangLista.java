
package org.foi.nwtis.jelvalcic.servisi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for zipKodoviRangLista complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="zipKodoviRangLista">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="brojPodataka" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="imeGrada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zipKod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "zipKodoviRangLista", propOrder = {
    "brojPodataka",
    "imeGrada",
    "zipKod"
})
public class ZipKodoviRangLista {

    protected int brojPodataka;
    protected String imeGrada;
    protected String zipKod;

    /**
     * Gets the value of the brojPodataka property.
     * 
     */
    public int getBrojPodataka() {
        return brojPodataka;
    }

    /**
     * Sets the value of the brojPodataka property.
     * 
     */
    public void setBrojPodataka(int value) {
        this.brojPodataka = value;
    }

    /**
     * Gets the value of the imeGrada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImeGrada() {
        return imeGrada;
    }

    /**
     * Sets the value of the imeGrada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImeGrada(String value) {
        this.imeGrada = value;
    }

    /**
     * Gets the value of the zipKod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipKod() {
        return zipKod;
    }

    /**
     * Sets the value of the zipKod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipKod(String value) {
        this.zipKod = value;
    }

}
