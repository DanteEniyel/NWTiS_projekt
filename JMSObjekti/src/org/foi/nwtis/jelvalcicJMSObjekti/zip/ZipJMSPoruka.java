/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicJMSObjekti.zip;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jelvalcic
 * Klasa objekta za JMS Objest message
 * Koristi se kod slanja JMS poruke nakon što korisnik odabere zip kod za koji
 * se ne prikupljaju meteo podaci
 */
public class ZipJMSPoruka implements Serializable{
    private int id;
    private String zipKod;    
    private String korisnik;
    private Date vrijemePocetka;
    
    public ZipJMSPoruka() {
    }

    public String getZipKod() {
        return zipKod;
    }

    public void setZipKod(String zipKod) {
        this.zipKod = zipKod;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public Date getVrijemePocetka() {
        return vrijemePocetka;
    }

    public void setVrijemePocetka(Date vrijemePocetka) {
        this.vrijemePocetka = vrijemePocetka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
