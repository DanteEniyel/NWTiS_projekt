/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.kontrole;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jelvalcic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jelvalcic.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.jelvalcic.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.jelvalcic.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author jelvalcic
 * Klasa koja proširuje biblioteku bp_konfiguracije 
 * kako bi mogli izvući i ostale podatke iz datoteke konfiguracije
 */
public class KorisnickaKonfiguracija {
    private BP_Konfiguracija bpKonfig;
    
    //obrada
    private int interval;
    
    
    private Konfiguracija konfiguracija;

/**
 * Konstruktor u kojem postavljamo vrijednosti iz datoteke konfiguracije
 * @param datoteka String - datoteka konfiguracije
 */
    public KorisnickaKonfiguracija(String datoteka) {
        bpKonfig = new BP_Konfiguracija(datoteka);
        try {
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            
            this.interval = Integer.parseInt(konfiguracija.dajPostavku("interval"));
            

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(KorisnickaKonfiguracija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public BP_Konfiguracija getBpKonfig() {
        return bpKonfig;
    }
    
    public int getInterval() {
        return interval;
    }
    
    public Konfiguracija getKonfiguracija() {
        return konfiguracija;
    }  
}
