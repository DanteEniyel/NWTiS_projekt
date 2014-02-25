/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.kontrole;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jelvalcic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jelvalcic.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.jelvalcic.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.jelvalcic.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author jelvalcic 
 * Klasa koja proširuje biblioteku bp_konfiguracije kako bi
 * mogli izvući i ostale podatke iz datoteke konfiguracije
 */
public class KorisnickaKonfiguracija {

    private BP_Konfiguracija bpKonfig;
    //obrada
    //obrada
    private String obradaAdresaPosluzitelja;
    private String obradaImapPort;
    private int obradaInterval;
    private String obradaEmailAdresa;
    private String obradaLozinka;
    private String obradaPredmet;
    private String obradaMapaIspravno;
    private String obradaMapaOstalo;
    private String korisnikAdresaPosluzitelja;
    private String korisnikEmailAdresa;
    private String korisnikLozinka;
    private int brojPorukaPoStranici;
    private Konfiguracija konfiguracija;

    /**
     * Konstruktor u kojem postavljamo vrijednosti iz datoteke konfiguracije
     *
     * @param datoteka datoteka konfiguracije
     */
    public KorisnickaKonfiguracija(String datoteka) {
        bpKonfig = new BP_Konfiguracija(datoteka);
        try {
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);

            this.obradaAdresaPosluzitelja = konfiguracija.dajPostavku("obradaAdresaPosluzitelja");
            this.obradaImapPort = konfiguracija.dajPostavku("obradaImapPort");
            this.obradaInterval = Integer.parseInt(konfiguracija.dajPostavku("obradaInterval"));
            this.obradaEmailAdresa = konfiguracija.dajPostavku("obradaEmailAdresa");
            this.obradaLozinka = konfiguracija.dajPostavku("obradaLozinka");
            this.obradaPredmet = konfiguracija.dajPostavku("obradaPredmet");
            this.obradaMapaIspravno = konfiguracija.dajPostavku("obradaMapaIspravno");
            this.obradaMapaOstalo = konfiguracija.dajPostavku("obradaMapaOstalo");
            this.korisnikAdresaPosluzitelja = konfiguracija.dajPostavku("korisnikAdresaPosluzitelja");
            this.korisnikEmailAdresa = konfiguracija.dajPostavku("korisnikEmail");
            this.korisnikLozinka = konfiguracija.dajPostavku("korisnikLozinka");
            this.brojPorukaPoStranici = Integer.parseInt(konfiguracija.dajPostavku("korisnikBrojPrikazanihPoruka"));
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(KorisnickaKonfiguracija.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public BP_Konfiguracija getBpKonfig() {
        return bpKonfig;
    }

    public void setBpKonfig(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;
    }

    public String getObradaAdresaPosluzitelja() {
        return obradaAdresaPosluzitelja;
    }

    public void setObradaAdresaPosluzitelja(String obradaAdresaPosluzitelja) {
        this.obradaAdresaPosluzitelja = obradaAdresaPosluzitelja;
    }

    public String getObradaImapPort() {
        return obradaImapPort;
    }

    public void setObradaImapPort(String obradaImapPort) {
        this.obradaImapPort = obradaImapPort;
    }

    public int getObradaInterval() {
        return obradaInterval;
    }

    public void setObradaInterval(int obradaInterval) {
        this.obradaInterval = obradaInterval;
    }

    public String getObradaEmailAdresa() {
        return obradaEmailAdresa;
    }

    public void setObradaEmailAdresa(String obradaEmailAdresa) {
        this.obradaEmailAdresa = obradaEmailAdresa;
    }

    public String getObradaLozinka() {
        return obradaLozinka;
    }

    public void setObradaLozinka(String obradaLozinka) {
        this.obradaLozinka = obradaLozinka;
    }

    public String getObradaPredmet() {
        return obradaPredmet;
    }

    public void setObradaPredmet(String obradaPredmet) {
        this.obradaPredmet = obradaPredmet;
    }

    public String getObradaMapaIspravno() {
        return obradaMapaIspravno;
    }

    public void setObradaMapaIspravno(String obradaMapaIspravno) {
        this.obradaMapaIspravno = obradaMapaIspravno;
    }

    public String getObradaMapaOstalo() {
        return obradaMapaOstalo;
    }

    public void setObradaMapaOstalo(String obradaMapaOstalo) {
        this.obradaMapaOstalo = obradaMapaOstalo;
    }

    public Konfiguracija getKonfiguracija() {
        return konfiguracija;
    }

    public void setKonfiguracija(Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    public String getKorisnikAdresaPosluzitelja() {
        return korisnikAdresaPosluzitelja;
    }

    public void setKorisnikAdresaPosluzitelja(String korisnikAdresaPosluzitelja) {
        this.korisnikAdresaPosluzitelja = korisnikAdresaPosluzitelja;
    }

    public String getKorisnikEmailAdresa() {
        return korisnikEmailAdresa;
    }

    public void setKorisnikEmailAdresa(String korisnikEmailAdresa) {
        this.korisnikEmailAdresa = korisnikEmailAdresa;
    }

    public String getKorisnikLozinka() {
        return korisnikLozinka;
    }

    public void setKorisnikLozinka(String korisnikLozinka) {
        this.korisnikLozinka = korisnikLozinka;
    }

    public int getBrojPorukaPoStranici() {
        return brojPorukaPoStranici;
    }

    public void setBrojPorukaPoStranici(int brojPorukaPoStranici) {
        this.brojPorukaPoStranici = brojPorukaPoStranici;
    }
    
    
    
}
