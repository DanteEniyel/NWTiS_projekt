/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.aplikacija_1;

import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.jelvalcic.klijenti.WeatherBugKlijent;
import org.foi.nwtis.jelvalcic.web.kontrole.KorisnickaKonfiguracija;

/**
 * @author jelvalcic
 * Klasa obrada meteo podataka kojom se u intervalu zadanim postavkama preuzimaju 
 * meteo podaci prema zip kodovima iz tablice activezipcodes
 */
public class ObradaMeteoPodataka extends Thread {

    private KorisnickaKonfiguracija bpKonf = null;
    private int interval;
    private String korisnickoIme;
    private String korisnickaLozinka;
    private String host;
    private String imeBaze;
    private String driver;
    //dretva
    private boolean radi;
    ServletContext context = null;
    private WeatherBugKlijent wbk = new WeatherBugKlijent();
    SimpleDateFormat formatiranoVrijeme = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ObradaMeteoPodataka() {

        super();
    }

    public KorisnickaKonfiguracija getBpKonf() {
        return bpKonf;
    }

    public void setBpKonf(KorisnickaKonfiguracija bpKonf) {
        this.bpKonf = bpKonf;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    @Override
    public synchronized void start() {
        radi = true;
        setBpKonf((KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija"));
        interval = bpKonf.getInterval();
        korisnickoIme = bpKonf.getBpKonfig().getUser_username();
        korisnickaLozinka = bpKonf.getBpKonfig().getUser_password();
        imeBaze = bpKonf.getBpKonfig().getUser_database();
        host = bpKonf.getBpKonfig().getServer_database();
        driver = bpKonf.getBpKonfig().getDriver_database(host + imeBaze);

        super.start();

    }

    @Override
    public void run() {
        //upitkojim se uzimaju zip kodovi iz tablice activezipcodes
        String upit = "SELECT * FROM  jelvalcic_activezipcodes ";
        ResultSet odgovor;
        Statement instr;
        Connection veza;
        try {
            Class.forName(driver);
        } catch (Exception ex) {
            ex.fillInStackTrace();
            System.out.println("Driver ne postoji ili greška kod učitavanja!");
        }

        try {
            veza = DriverManager.getConnection(host + imeBaze, korisnickoIme, korisnickaLozinka);
            instr = veza.createStatement();
            
            while (radi) {
                if (!Posrednik.isPauza()) {
                    try {
                        odgovor = instr.executeQuery(upit);
                        String formatiranDatumVrijeme = formatiranoVrijeme.format(new Date());
                        while (odgovor.next()) {
                            String zip = odgovor.getString("ZIP");
                            String korisnik = odgovor.getString("Korisnik");
                            //vraćaju se podaci o gradu, traženom zip kodu i zip kodu za koji su vraćeni podaci
                            LiveWeatherData lw = wbk.dajMeteoPodatke(zip);
                            System.out.println("Grad: " + lw.getCity() + ", Zip: " + zip + ", Pravi zip: " + lw.getZipCode());
                            
                            //ukoliko je neki podatak nedostupan, odnosno metoda dajMeteoPodatke vrati N/A, 
                            //postavljamo default vrijednost da ne postoji(oblika NULL ili za temp -9999)jer je u bazi float
                            //da ne dođe do grešaka
                            if(lw.getPressure().startsWith("N")){
                                lw.setPressure("NULL");
                            }
                            if(lw.getHumidity().startsWith("N")){
                                lw.setHumidity("NULL");
                            }
                            if(lw.getTemperature().startsWith("N")){
                                lw.setTemperature("-9999");
                            }
                            if(lw.getWindSpeed().startsWith("N")){
                                lw.setWindSpeed("NULL");
                            }
                            //kraj provjere
                            //upit kojim se zapisuju dobiveni meteo podaci za zip u tablicu meteo_podaci
                            String upit2 = "INSERT INTO meteo_podaci(ZIP_zahtjevani, ZIP_vraceni, Tlak, Vlaga, Temperatura, Vjetar, Smjer_vjetra, Datum_i_vrijeme) "
                                    + "VALUES(" + zip + ", " + lw.getZipCode() + ", " + lw.getPressure() + ", " + lw.getHumidity() + ", "
                                    + lw.getTemperature() + ", " + lw.getWindSpeed() + ", '" + lw.getWindDirection() + "', '" + formatiranDatumVrijeme + "')"; 

                            Statement instr2;
                            instr2 = veza.createStatement();
                            
                            //kontrola, ukoliko je WeatherBug servis pao
                            if(lw.getZipCode().isEmpty()){
                                System.out.println("Meteo servis ne radi za " + zip);
                            }else{
                                instr2.executeUpdate(upit2);
                            
                            }
                            
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(ObradaMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    System.out.println("Idem spavati!");
                    sleep(interval * 1000);
                    System.out.println("Probudih se!");
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObradaMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            // veza.close();
        } catch (SQLException ex) {
            Logger.getLogger(ObradaMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void interrupt() {
        radi = false;
        super.interrupt();
        
    }
}
