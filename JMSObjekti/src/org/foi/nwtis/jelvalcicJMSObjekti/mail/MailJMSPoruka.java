/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicJMSObjekti.mail;

import java.io.Serializable;

/**
 *
 * @author jelvalcic
 * Klasa objekta za JMS Object Message
 * Koristi se kod slanja JMS poruke nakon intervala obrade mail poruka
 */
public class MailJMSPoruka implements Serializable{
    private int ID;
    private String vrijemePocetka;
    private String vrijemeKraja;
    private int brojProcitanihPoruka;
    private int brojNwtisPoruka;

    public MailJMSPoruka() {
    }

    public String getVrijemePocetka() {
        return vrijemePocetka;
    }

    public void setVrijemePocetka(String vrijemePocetka) {
        this.vrijemePocetka = vrijemePocetka;
    }

    public String getVrijemeKraja() {
        return vrijemeKraja;
    }

    public void setVrijemeKraja(String vrijemeKraja) {
        this.vrijemeKraja = vrijemeKraja;
    }

    public int getBrojProcitanihPoruka() {
        return brojProcitanihPoruka;
    }

    public void setBrojProcitanihPoruka(int brojProcitanihPoruka) {
        this.brojProcitanihPoruka = brojProcitanihPoruka;
    }

    public int getBrojNwtisPoruka() {
        return brojNwtisPoruka;
    }

    public void setBrojNwtisPoruka(int brojNwtisPoruka) {
        this.brojNwtisPoruka = brojNwtisPoruka;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
}
