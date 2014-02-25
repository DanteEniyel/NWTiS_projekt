/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.servisi;

/**
 *
 * @author jelvalcic
 * Klasa objekta MeteoPodaci
 */
public class MeteoPodaci {

    private String zahtjevaniZipKod;
    private String vraceniZipKod;
    private String zahtjevaniGrad;
    private String vraceniGrad;
    private float tlak;
    private float vlaga;
    private float temperatura;
    private float vjetar;
    private String smjerVjetra;
    
    public MeteoPodaci() {
    }

    public String getZahtjevaniZipKod() {
        return zahtjevaniZipKod;
    }

    public void setZahtjevaniZipKod(String zahtjevaniZipKod) {
        this.zahtjevaniZipKod = zahtjevaniZipKod;
    }

    public String getVraceniZipKod() {
        return vraceniZipKod;
    }

    public void setVraceniZipKod(String vraceniZipKod) {
        this.vraceniZipKod = vraceniZipKod;
    }

    public String getZahtjevaniGrad() {
        return zahtjevaniGrad;
    }

    public void setZahtjevaniGrad(String zahtjevaniGrad) {
        this.zahtjevaniGrad = zahtjevaniGrad;
    }

    public String getVraceniGrad() {
        return vraceniGrad;
    }

    public void setVraceniGrad(String vraceniGrad) {
        this.vraceniGrad = vraceniGrad;
    }

    public float getTlak() {
        return tlak;
    }

    public void setTlak(float tlak) {
        this.tlak = tlak;
    }

    public float getVlaga() {
        return vlaga;
    }

    public void setVlaga(float vlaga) {
        this.vlaga = vlaga;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public float getVjetar() {
        return vjetar;
    }

    public void setVjetar(float vjetar) {
        this.vjetar = vjetar;
    }

    public String getSmjerVjetra() {
        return smjerVjetra;
    }

    public void setSmjerVjetra(String smjerVjetra) {
        this.smjerVjetra = smjerVjetra;
    }
        
}
