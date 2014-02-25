/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.servisi;

/**
 *
 * @author jelvalcic
 * Klasa objekta ZipKodoviRangLista
 */
public class ZipKodoviRangLista {
    private String zipKod;
    private String imeGrada;
    private int brojPodataka;
    
    
    public ZipKodoviRangLista() {
    }

    public String getZipKod() {
        return zipKod;
    }

    public void setZipKod(String zipKod) {
        this.zipKod = zipKod;
    }

    public String getImeGrada() {
        return imeGrada;
    }

    public void setImeGrada(String imeGrada) {
        this.imeGrada = imeGrada;
    }

    public int getBrojPodataka() {
        return brojPodataka;
    }

    public void setBrojPodataka(int brojPodataka) {
        this.brojPodataka = brojPodataka;
    }
    
}
