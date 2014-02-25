/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.jelvalcic.web.kontrole.KorisnickaKonfiguracija;

/**
 * @author jelvalcic
 * Klasa Posrednik koja služi kao posrednik između slušača i upravljačke dretve
 * Jednostavni poslužitelj
 */
public class Posrednik extends Thread{
    private int port;    
    private ServletContext context;
    private ObradaMeteoPodataka obradaMeteoPodataka;
    private KorisnickaKonfiguracija konfig; 
    private static boolean kraj=false;
    private static boolean pauza;
    private static boolean adminAktivan = false;
    private static Date prethodnaAdminKomanda = null;
    private static int brojPrimljenihKomandi = 0;
    private static int brojNeispravnihKomandi = 0;
    private static int brojIspravnihKomandi = 0;
    
/**
 * Konstruktor klase Posrednik
 * @param port int - port koji će klasa Jednostavni poslužitelj slušati za komande
 * @param konfig KorisnickaKonfiguracija - datoteka konfiguracije s podacima potrebnim 
 * za spajanje na bazu podataka i slanje maila
 */

    public Posrednik(int port, KorisnickaKonfiguracija konfig) {
        this.konfig = konfig;
        this.port = port;      
    }

/**
 * Metoda koja pokreće Jednostavni poslužitelj
 */
    public void pokreniJednostavniPosluzitelj(){
        try {
            try (ServerSocket server = new ServerSocket(port)) {
                while(!kraj){
                    Socket klijent = server.accept();
                    JednostavniPosluzitelj dretva = new JednostavniPosluzitelj(klijent, konfig);
                    dretva.setName("JednostavniPosluzitelj" + new Date().toString());
                    dretva.start(); 
                } 
                server.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Posrednik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        pokreniJednostavniPosluzitelj();
    }

    @Override
    public void interrupt() {
          kraj=false;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
      
    }

/**
 * Provjera da li je server završio s radom
 * @return boolean - true - dretva završava s radom, false - dretva radi
 * vraća se informacija da li je došla naredba za kraj rada
 */
    public static boolean isKraj() {
        return kraj;
    }

/**
 * Postavljanje zastavice koja govori da je server završio s radom
 * @param kraj boolean - true - dretva završava s radom, false - dretva radi
 * vraća se vrijednost zastavice
 */
    public static void setKraj(boolean kraj) {
        Posrednik.kraj = kraj;
    }

/**
 * Provjera da li je server pauziran
 * @return boolean - true - dretva je u pauzi, false - dretva radi
 * vraća se informacije da li je došla naredba za pauzu rada
 */
    public static boolean isPauza() {
        return pauza;
    }

/**
 * Postavljanje zastavice koja govori da je server pauziran
 * @param pauza boolean - true - dretva je u pauzi, false - dretva radi
 * vraća se vrijednost zastavice
 */
    public static void setPauza(boolean pauza) {
        Posrednik.pauza = pauza;
    }
/**
 * Provjera da li je aktivna administratorska komanda
 * @return adminAkrivan boolean - true - administratorska komanda je aktivna
 * false - administratorska komanda nije aktivna
 */
    public static boolean isAdminAktivan() {
        return adminAktivan;
    }
/**
 * Postavljanje zastavice koja govori da li je aktivna administratorska komanda
 * @param adminAktivan boolean - true - administatorska komanda je aktivna
 * false - administratorska komanda nije aktivna
 */
    public static void setAdminAktivan(boolean adminAktivan) {
        Posrednik.adminAktivan = adminAktivan;
    }
//---------------------------STATISTIKA KOMANDI----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za brojače i statistiku komandi">
    
    
    public ObradaMeteoPodataka getObradaMeteoPodataka() {
        return obradaMeteoPodataka;
    }

    public static Date getPrethodnaAdminKomanda() {
        return prethodnaAdminKomanda;
    }

    public static void setPrethodnaAdminKomanda(Date prethodnaAdminKomanda) {
        Posrednik.prethodnaAdminKomanda = prethodnaAdminKomanda;
    }

    public static int getBrojPrimljenihKomandi() {
        return brojPrimljenihKomandi;
    }

    public static void incBrojPrimljenihKomandi() {
        Posrednik.brojPrimljenihKomandi++;
    }

    public static int getBrojNeispravnihKomandi() {
        return brojNeispravnihKomandi;
    }

    public static void incBrojNeispravnihKomandi() {
        Posrednik.brojNeispravnihKomandi++;
    }

    public static int getBrojIspravnihKomandi() {
        return brojIspravnihKomandi;
    }

    public static void incBrojIspravnihKomandi() {
        Posrednik.brojIspravnihKomandi++;
    }
    //</editor-fold>  
    
/**
 * Metoda kojom se na kraju obrade administratorske komande resetiraju brojači 
 * statistike komandi i postavlja vrijeme posljednje pristigle komande
 * @param datumTrenutneKomande Date - vrijeme posljednje pristigle komande
 */
    public static void resetirajBrojace (Date datumTrenutneKomande){
        Posrednik.brojNeispravnihKomandi = 0;
        Posrednik.brojIspravnihKomandi = 0;
        Posrednik.brojPrimljenihKomandi = 0;
        Posrednik.prethodnaAdminKomanda = datumTrenutneKomande;
    }
}
