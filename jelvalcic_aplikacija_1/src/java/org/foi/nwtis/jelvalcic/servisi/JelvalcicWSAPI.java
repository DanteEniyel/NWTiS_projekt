/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.servisi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.jelvalcic.web.kontrole.KorisnickaKonfiguracija;
import org.foi.nwtis.jelvalcic.web.slusaci.KontekstAplikacije;

/**
 *
 * @author jelvalcic
 * Klasa API koja definira pozadinske metode operacija vlastitog web servisa
 */
public class JelvalcicWSAPI {

    //baza
    private String upit = "";
    private ResultSet odgovor;
    private Statement instr;
    private Connection veza;
    private ServletContext kontekst;
    private KorisnickaKonfiguracija bpKonf = null;
    private String korisnickoIme;
    private String korisnickaLozinka;
    private String host;
    private String imeBaze;
    private String driver;
    //ostalo
    private List<ZipKodoviRangLista> popisAktivnihZipKodova = new ArrayList<>();
    private MeteoPodaci meteoPodaciZaZip = new MeteoPodaci();
    private List<ZipKodoviRangLista> rangListaZipKodova = new ArrayList<>();
    private List<MeteoPodaci> popisMeteoPodatakaZaZip = new ArrayList<>();
    private List<MeteoPodaci> popisMeteoPodatakaZaZipVremenskiInterval = new ArrayList<>();
    
/**
 * Konstruktor
 */
    public JelvalcicWSAPI() {

        kontekst = KontekstAplikacije.getKontekst();
        String path = kontekst.getRealPath("WEB-INF") + java.io.File.separator;
        String datoteka = path + kontekst.getInitParameter("konfiguracija");
        bpKonf = new KorisnickaKonfiguracija(datoteka);
        bpKonf = (KorisnickaKonfiguracija) kontekst.getAttribute("BP_Konfiguracija");
        korisnickoIme = bpKonf.getBpKonfig().getUser_username();
        korisnickaLozinka = bpKonf.getBpKonfig().getUser_password();
        imeBaze = bpKonf.getBpKonfig().getUser_database();
        host = bpKonf.getBpKonfig().getServer_database();
        driver = bpKonf.getBpKonfig().getDriver_database(host + imeBaze);

        try {
            Class.forName(driver);

            veza = DriverManager.getConnection(host + imeBaze, korisnickoIme, korisnickaLozinka);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JelvalcicWSAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/**
 * Dohvaćaju se podaci iz tablice activezipcodes
 * @return List<ZipKodoviRangLista> - lista zip kodova
 */
    public List<ZipKodoviRangLista> getPopisAktivnihZipKodova() {
        upit = "SELECT jelvalcic_activezipcodes.ZIP, CITY "
                + "FROM jelvalcic_activezipcodes INNER JOIN zip_codes "
                + "ON zip_codes.ZIP = jelvalcic_activezipcodes.ZIP;";
        try {
            instr = veza.createStatement();
            odgovor = instr.executeQuery(upit);

            while (odgovor.next()) {
               ZipKodoviRangLista azk = new ZipKodoviRangLista();
               azk.setZipKod(odgovor.getString("ZIP"));
               azk.setImeGrada(odgovor.getString("CITY"));
               azk.setBrojPodataka(0);
               
               popisAktivnihZipKodova.add(azk);
            }
            instr.close();
        } catch (SQLException ex) {

            Logger.getLogger(JelvalcicWSAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return popisAktivnihZipKodova;
    }
    
    
/**
 * Dohvaćaju se meteo podaci za traženi zip kod iz tablice meteo_podaci
 * @param pZipKod String - traženi zip kod
 * @return MeteoPodaci - vraćaju se meteo podaci za traženi zip kod
 */    
    public MeteoPodaci getMeteoPodaciLista(String pZipKod) {
        
        upit = "SELECT meteo_podaci.*, CITY FROM meteo_podaci "
                + "INNER JOIN zip_codes ON meteo_podaci.ZIP_zahtjevani = zip_codes.ZIP "
                + "WHERE ZIP_zahtjevani = '" + pZipKod + "' ORDER BY RBR DESC LIMIT 1;";
        try {
            instr = veza.createStatement();
            odgovor = instr.executeQuery(upit);

            while (odgovor.next()) {
                meteoPodaciZaZip.setZahtjevaniZipKod(odgovor.getString("ZIP_zahtjevani"));
                meteoPodaciZaZip.setVraceniZipKod(odgovor.getString("ZIP_vraceni"));
                meteoPodaciZaZip.setTlak(odgovor.getFloat("Tlak"));
                meteoPodaciZaZip.setVlaga(odgovor.getFloat("Vlaga"));
                meteoPodaciZaZip.setTemperatura(odgovor.getFloat("Temperatura"));
                meteoPodaciZaZip.setVjetar(odgovor.getFloat("Vjetar"));
                meteoPodaciZaZip.setSmjerVjetra(odgovor.getString("Smjer_vjetra"));
                meteoPodaciZaZip.setZahtjevaniGrad(odgovor.getString("CITY"));
                
              
            }
            instr.close();
        } catch (SQLException ex) {

            Logger.getLogger(JelvalcicWSAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return meteoPodaciZaZip;
    }

/**
 * Dohvaća se n zip kodova za koje se prikupilo najviše meteo podataka
 * @param pBrojZipKodova int - broj zip kodova za koje se prikupilo najviše meteo podataka
 * @return List<ZipKodoviRangLista> - vraća se željeni broj zip kodova za koje
 * se prikupilo najviše meteo podataka
 */
    public List<ZipKodoviRangLista> getRangListuZipKodova(int pBrojZipKodova){
        upit = "SELECT COUNT(ZIP_zahtjevani) AS zipBroj, ZIP_zahtjevani, CITY " +
                "FROM meteo_podaci " +
                "INNER JOIN zip_codes ON zip_codes.ZIP = meteo_podaci.ZIP_zahtjevani " +
                "GROUP BY ZIP_zahtjevani, CITY ORDER BY ZIP_zahtjevani, CITY LIMIT " + Integer.toString(pBrojZipKodova) + ";";
        try {
            instr = veza.createStatement();
            odgovor = instr.executeQuery(upit);

            while (odgovor.next()) {
                ZipKodoviRangLista zkrl = new ZipKodoviRangLista();
                zkrl.setZipKod(odgovor.getString("ZIP_zahtjevani"));
                zkrl.setImeGrada(odgovor.getString("CITY"));
                zkrl.setBrojPodataka(odgovor.getInt("zipBroj"));
                
                rangListaZipKodova.add(zkrl);
            }
            instr.close();
        } catch (SQLException ex) {

            Logger.getLogger(JelvalcicWSAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rangListaZipKodova;
    }
   
/**
 * Dohvaća se n meteo podataka za željeni zip kod
 * @param pZipKod String - zip kod za koji se dohvaćaju meteo podaci
 * @param pBrojPodataka int - broj meteo podataka koje se želi dohvatiti
 * @return List<MeteoPodaci> - vraća se željeni broj meteo podataka za navedani zip kod
 */
    public List<MeteoPodaci> getPopisMeteoPodatakaZaZipKod(String pZipKod, int pBrojPodataka){
        upit = "SELECT meteo_podaci.*, CITY FROM meteo_podaci "
                + "INNER JOIN zip_codes ON meteo_podaci.ZIP_zahtjevani = zip_codes.ZIP "
                + "WHERE ZIP_zahtjevani = '" + pZipKod + "' ORDER BY RBR DESC LIMIT " + Integer.toString(pBrojPodataka) + ";";
        try {
            instr = veza.createStatement();
            odgovor = instr.executeQuery(upit);

            while (odgovor.next()) {
                MeteoPodaci pmp = new MeteoPodaci();
                pmp.setZahtjevaniZipKod(odgovor.getString("ZIP_zahtjevani"));
                pmp.setVraceniZipKod(odgovor.getString("ZIP_vraceni"));
                pmp.setTlak(odgovor.getFloat("Tlak"));
                pmp.setVlaga(odgovor.getFloat("Vlaga"));
                pmp.setTemperatura(odgovor.getFloat("Temperatura"));
                pmp.setVjetar(odgovor.getFloat("Vjetar"));
                pmp.setSmjerVjetra(odgovor.getString("Smjer_vjetra"));
                pmp.setZahtjevaniGrad(odgovor.getString("CITY"));
                
                popisMeteoPodatakaZaZip.add(pmp);
            }
            instr.close();
        } catch (SQLException ex) {

            Logger.getLogger(JelvalcicWSAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return popisMeteoPodatakaZaZip;
    }
 
/**
 * Dohvaćaju se meteo podaci u željenom intervalu za traženi zip kod
 * @param pZipKod String - zip kod za koji se dohvaćaju meteo podaci
 * @param pOdDatum String - početak intervala za koji se dohvaćaju meteo podaci
 * @param pDoDatum String - kraj intervala za koji se dohvaćaju meteo podaci
 * @return List<MeteoPodaci> - vraćaju se meteo podaci za željeni zip kod unutar 
 * navedenog intervala
 */
    public List<MeteoPodaci> getMeteoPodatkeZaZipKodVremenskiInterval(String pZipKod, String pOdDatum, String pDoDatum){
        
        
        upit = "SELECT meteo_podaci.*, CITY FROM meteo_podaci "
                + "INNER JOIN zip_codes ON meteo_podaci.ZIP_zahtjevani = zip_codes.ZIP "
                + "WHERE ZIP_zahtjevani = '" + pZipKod + "' "
                + "AND DATE(Datum_i_vrijeme) >= '" + pOdDatum +"' "
                + "AND DATE(Datum_i_vrijeme) <= '" + pDoDatum + "' "
                + "ORDER BY RBR DESC ;";
        try {
            instr = veza.createStatement();
            odgovor = instr.executeQuery(upit);

            while (odgovor.next()) {
                MeteoPodaci pmpvi = new MeteoPodaci();
                pmpvi.setZahtjevaniZipKod(odgovor.getString("ZIP_zahtjevani"));
                pmpvi.setVraceniZipKod(odgovor.getString("ZIP_vraceni"));
                pmpvi.setTlak(odgovor.getFloat("Tlak"));
                pmpvi.setVlaga(odgovor.getFloat("Vlaga"));
                pmpvi.setTemperatura(odgovor.getFloat("Temperatura"));
                pmpvi.setVjetar(odgovor.getFloat("Vjetar"));
                pmpvi.setSmjerVjetra(odgovor.getString("Smjer_vjetra"));
                pmpvi.setZahtjevaniGrad(odgovor.getString("CITY"));
                
                popisMeteoPodatakaZaZipVremenskiInterval.add(pmpvi);
            }
            instr.close();
        } catch (SQLException ex) {

            Logger.getLogger(JelvalcicWSAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return popisMeteoPodatakaZaZipVremenskiInterval;
    }
    
    
}
