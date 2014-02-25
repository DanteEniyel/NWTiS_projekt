/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jelvalcic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jelvalcic.servisi.MeteoPodaci;
import org.foi.nwtis.jelvalcic.servisi.ZipKodoviRangLista;
import org.foi.nwtis.jelvalcicJMSObjekti.zip.ZipJMSPoruka;
import org.foi.nwtis.jelvalcicapp2.web.klijenti.MeteoKlijent;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.Korisnik;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.ZipKodoviDB;
import org.foi.nwtis.jelvalcicapp2.web.slusaci.KontekstAplikacije;

/**
 *
 * @author jelvalcic
 * Klasa kojom se vraćaju i provjeravaju podaci u bazi podataka
 */
@Named(value = "vratiPodatkeDB")
@SessionScoped
public class VratiPodatkeDB implements Serializable {
    @Resource(mappedName = "NWTiS_ZipJMS")
    private Queue nWTiS_ZipJMS;
    @Resource(mappedName = "NWTiS_ZipJMSFactory")
    private ConnectionFactory nWTiS_ZipJMSFactory;
    

    @EJB
    private MeteoKlijent meteoKlijent;
    private List<ZipKodoviDB> korisnickiZipKodovi = new ArrayList<>();
    private String zipKod;
    private String status;
    private List<MeteoPodaci> meteoPodaciZaPortfolioZip = new ArrayList<>();

    /**
     * Creates a new instance of VratiPodatkeDB
     */
    public VratiPodatkeDB() {
    }
/**
 * Metoda za dohvaćanje zip kodova u korisničkom portfoliu
 * @return lista zip kodova u portfoliu
 */
    public List<ZipKodoviDB> getKorisnickiZipKodovi() {

        ServletContext sc = KontekstAplikacije.getKontekst();
        BP_Konfiguracija bpKonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        String url = bpKonf.getServer_database() + bpKonf.getUser_database();

        Korisnik korisnik = (Korisnik) sesija.getAttribute("korisnik");

        korisnickiZipKodovi.clear();
        if (korisnik != null) {

            String korImee = korisnik.getKorisnik();
            String korIme = bpKonf.getUser_username();
            String korLoz = bpKonf.getUser_password();
            String upit = "SELECT PORTFOLIO_ZIP.ZIP, CITY FROM PORTFOLIO_ZIP INNER JOIN ZIP_CODES "
                    + "ON PORTFOLIO_ZIP.ZIP = ZIP_CODES.ZIP WHERE KORISNIK = '" + korImee + "' ";

            try (
                    Connection veza = DriverManager.getConnection(url, korIme, korLoz);
                    Statement instr = veza.createStatement();
                    ResultSet odgovor = instr.executeQuery(upit);) {
                while (odgovor.next()) {
                    ZipKodoviDB zipovi = new ZipKodoviDB();
                    zipovi.setZipKod(odgovor.getString("ZIP"));
                    zipovi.setImeGrada(odgovor.getString("CITY"));
                    korisnickiZipKodovi.add(zipovi);
                }

            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
            meteoPodaciZaPortfolioZip.clear();
            for(ZipKodoviDB zkdb : korisnickiZipKodovi){
                MeteoPodaci mp = meteoKlijent.dajMeteoPodatkeZaZipKod(zkdb.getZipKod());
                meteoPodaciZaPortfolioZip.add(mp);
            }
            return korisnickiZipKodovi;
        } else {
            return null;
        }
    }

    public void setKorisnickiZipKodovi(List<ZipKodoviDB> korisnickiZipKodovi) {
        this.korisnickiZipKodovi = korisnickiZipKodovi;
    }

    public String getZipKod() {
        return zipKod;
    }

    public void setZipKod(String zipKod) {
        this.zipKod = zipKod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MeteoPodaci> getMeteoPodaciZaPortfolioZip() {
        
        return meteoPodaciZaPortfolioZip;
    }

    public void setMeteoPodaciZaPortfolioZip(List<MeteoPodaci> meteoPodaciZaPortfolioZip) {
        this.meteoPodaciZaPortfolioZip = meteoPodaciZaPortfolioZip;
    }
    
/**
 * Metoda kojom se provjerava postoji li je zip kod koji se dodaje u popisu aktivnih 
 * zip kodova
 */
    public void provjeriZip() {

        List<ZipKodoviRangLista> aktivniZkodovi = meteoKlijent.dajAktivneZipKodove();
        boolean postoji = false;
        for (ZipKodoviRangLista zkrl : aktivniZkodovi) {
            if (zkrl.getZipKod().equals(zipKod)) {
                postoji = true;
                break;
            }
        }

        status = upisiZipUDB(zipKod);
        
        if(status.equals("OK") && !postoji){
            posaljiJMS(zipKod);
        }
    }

/**
 * Metoda kojom se upisuje uneseni zip kod u tablicu zip kodova za portfolio
 * @param zipKod zip kod koji se želi dodati
 * @return status obrade unosa
 */
    private String upisiZipUDB(String zipKod) {
        ServletContext sc = KontekstAplikacije.getKontekst();
        BP_Konfiguracija bpKonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        String url = bpKonf.getServer_database() + bpKonf.getUser_database();

        Korisnik korisnik = (Korisnik) sesija.getAttribute("korisnik");

        boolean ima = false;
        String korImee = korisnik.getKorisnik();
        String korIme = bpKonf.getUser_username();
        String korLoz = bpKonf.getUser_password();
        String upit1 = "SELECT * FROM ZIP_CODES WHERE ZIP = " + zipKod + "";
        String upit2 = "SELECT * FROM PORTFOLIO_ZIP WHERE ZIP = " + zipKod + " AND KORISNIK = '" + korImee + "'";
        String upit3 = "INSERT INTO PORTFOLIO_ZIP (KORISNIK, ZIP) VALUES ('" + korImee + "', " + zipKod + ")";

        if(korImee.isEmpty() || zipKod.isEmpty()){//kontrola ukoliko nije korisnik logiran ili zip ne postoji
                                                  //ne može dodavati ništa u ništa :D
            return "You don't know how this works, do you?";
        }
        try (
                Connection veza = DriverManager.getConnection(url, korIme, korLoz);
                Statement instr = veza.createStatement();
                ResultSet odgovor = instr.executeQuery(upit2);) {
            ima = false;
            while (odgovor.next()) {//ako imamo zip kod u tablici PORTFORLIO_ZIP, samo se javlja
                //poruka da postoji
                ima = true;
            }

            if (ima) {
                status = "Zip postoji u portfoliu";
                return status;
            }
            Statement instr3;
            try (Statement instr2 = veza.createStatement()) {
                ResultSet odgovor2 = instr2.executeQuery(upit1);
                ima = false;
                while (odgovor2.next()) {//da li postoji zip kod u tablici ZIP_CODES (baza nije ralacijska//tablice nisu povezane)
                    //provjera da li ima zip kod među svim ponuđenim
                    ima = true;
                }
                if (!ima) {//ako nema, obavijest da zip kod nije moguće dodati jer nije dopusten
                    status = "Zip " + zipKod + " ne postoji u popisu dozvoljenih zip kodova.";
                    return status;
                }
                instr3 = veza.createStatement();
                instr3.executeUpdate(upit3);
                instr.close();
            }
            instr3.close();

            status = "OK";

        } catch (Exception ex) {
            status = "Somewhere something went terribly wrong :O";
            ex.fillInStackTrace();
        }

        return status;
    }
    
/**
 * Metoda koja šalje JMS poruku aplikaciji 3 za dodavanje odabranog zip koda
 * u popis aktivnih zip kodova
 * @param zipKod zip kod koji se želi unijeti
 */
    private void posaljiJMS(String zipKod) {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Korisnik korisnik = (Korisnik) sesija.getAttribute("korisnik");
        ZipJMSPoruka zjp = new ZipJMSPoruka();
        //postavljanje parametara za slanje
        zjp.setKorisnik(korisnik.getKorisnik());
        zjp.setZipKod(zipKod);
        zjp.setVrijemePocetka(new Date());
        try {        
            //slanje JMS poruke
            sendJMSMessageToNWTiS_ZipJMS(zjp);
        } catch (JMSException ex) {
            Logger.getLogger(VratiPodatkeDB.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }

    private Message createJMSMessageFornWTiS_ZipJMS(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

    private void sendJMSMessageToNWTiS_ZipJMS(ZipJMSPoruka messageData) throws JMSException {
        javax.jms.Connection connection = null;
        Session session = null;
        try {
            connection = nWTiS_ZipJMSFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(nWTiS_ZipJMS);
            ObjectMessage om = session.createObjectMessage();
            om.setObject(messageData);
            om.setJMSType(messageData.getClass().getName());
            messageProducer.send(om);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    
}
