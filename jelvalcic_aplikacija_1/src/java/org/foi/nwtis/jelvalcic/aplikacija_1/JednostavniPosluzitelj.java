/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.aplikacija_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.jelvalcic.web.kontrole.KorisnickaKonfiguracija;

/**
 * Klasa JednostavniPosluzitelj
 *
 * @author jelvalcic
 * Služi za primanje i obradu korisničkih i administratorskih komandi, te vraća 
 * status obrade kao odgovor servera
 */
public class JednostavniPosluzitelj extends Thread {
    private boolean radi;
    private Socket klijent;
    private KorisnickaKonfiguracija konfig;
    private String odgovorServera = "";
    private String korisnickoIme = "";
    private String korisnickaLozinka = "";
    private String zipKod = "";
    private String komanda;
    private boolean korisnickaKomanda = false;
    private boolean ispravnaKomanda = true;
    private SimpleDateFormat formatiranoVrijeme = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    //varijable za provjeru
    private String sintaksa = "";
    //mail
    private Date vrijemePocetka = new Date();
   

    /**
     * Konstruktor JednostavniPosluzitelj
     *
     * @param klijent Socket - socket na koji će se klijent spojiti
     * @param konfig KorisnickaKonfiguracija - datoteka postavki
     *
     */
    public JednostavniPosluzitelj(Socket klijent, KorisnickaKonfiguracija konfig) {
        this.klijent = klijent;
        this.konfig = konfig;
    }

/**
 * Metoda kojom se upisuju podaci u dnevnik rada klase Jednostavni poslužitelj
 * @param pKorisnickoIme String - korisničko ime korisnika koji je poslao komandu
 * @param pKomanda String - komanda koju je korisnik poslao
 * @param pOdgovorServera String - status koji se vraća kao odgovor servera
 * @param pKorisnickaKomanda boolean - zastavica koja označava da li je primljena 
 * korisnička komanda (true - korisnik, false - administrator)
 * @param pIspravnaKomanda boolean - zastavica koja označava da li je komanda 
 * ispravna (true - ispravna, false - neispravna)
 * @throws SQLException 
 */
    private void zapisiUDnevnik(String pKorisnickoIme, String pKomanda, String pOdgovorServera, boolean pKorisnickaKomanda, boolean pIspravnaKomanda) throws SQLException {
        String bKorisnickoIme = konfig.getBpKonfig().getUser_username();
        String bKorisnickaLozinka = konfig.getBpKonfig().getUser_password();
        String imeBaze = konfig.getBpKonfig().getUser_database();
        String host = konfig.getBpKonfig().getServer_database();
        String driver = konfig.getBpKonfig().getDriver_database(host + imeBaze);

        try {

            String upit = "";
            Statement instr;
            Connection veza;

            Class.forName(driver);
            veza = DriverManager.getConnection(host + imeBaze, bKorisnickoIme, bKorisnickaLozinka);
            instr = veza.createStatement();

            String vrijeme = formatiranoVrijeme.format(new Date());
            String s1 = "0";
            String s2 = "0";
            if (pKorisnickaKomanda) {
                s1 = "1";
            }
            if (pIspravnaKomanda) {
                s2 = "1";
            }

            upit = "INSERT INTO dnevnik(Datum_i_vrijeme, Korisnik, Komanda, Odgovor_servera, Korisnicka_komanda, Ispravna_komanda) "
                    + "VALUES ('" + vrijeme + "', '" + pKorisnickoIme + "', '" + pKomanda + "', '" + pOdgovorServera
                    + "', " + s1 + ", " + s2 + ")";

            instr.executeUpdate(upit);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * Metoda za projeru administratora ukoliko stigne administratorska komanda
 * @param pKorisnickoIme String - korisničko ime administratora nad kojim će se vršiti provjera
 * @param pKorisnickaLozinka String - korisnička lozinka administratora nad kojom će se vršiti provjera
 * @return boolean - vraća se true ako je administrator autenticiran, false ako nije prošla autentikacija
 * @throws SQLException 
 */
    private boolean provjeriAdministratora(String pKorisnickoIme, String pKorisnickaLozinka) throws SQLException {
        String bKorisnickoIme = konfig.getBpKonfig().getUser_username();
        String bKorisnickaLozinka = konfig.getBpKonfig().getUser_password();
        String imeBaze = konfig.getBpKonfig().getUser_database();
        String host = konfig.getBpKonfig().getServer_database();
        String driver = konfig.getBpKonfig().getDriver_database(host + imeBaze);
        boolean vrati = false;

        try {
            String upit = "SELECT * FROM korisnici WHERE Korisnicko_ime = '" + pKorisnickoIme
                    + "' AND Korisnicka_lozinka = '" + pKorisnickaLozinka + "';";
            Statement instr;
            ResultSet odgovor;
            Connection veza;

            Class.forName(driver);
            veza = DriverManager.getConnection(host + imeBaze, bKorisnickoIme, bKorisnickaLozinka);
            instr = veza.createStatement();

            odgovor = instr.executeQuery(upit);

            if (odgovor.isBeforeFirst()) { // provjera da li postoji slogova u resultsetu
                vrati = true;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vrati;
    }
/**
 * Metoda kojom se vraćaju meteo podaci ukoliko je pristigla komanda GET ZIP
 * @param pZipKod String - zip kod za kojeg se traže meteo podaci
 * @return String - vraćaju se meteo podaci za traženi zip kod (43 - za traženi 
 * zip kod se ne preuzimaju meteo podaci, 10 - za traženi zip kod se preuzimaju podaci)
 * @throws SQLException 
 */
    private String vratiMeteoPodatke(String pZipKod) throws SQLException {
        String bKorisnickoIme = konfig.getBpKonfig().getUser_username();
        String bKorisnickaLozinka = konfig.getBpKonfig().getUser_password();
        String imeBaze = konfig.getBpKonfig().getUser_database();
        String host = konfig.getBpKonfig().getServer_database();
        String driver = konfig.getBpKonfig().getDriver_database(host + imeBaze);
        String vrati = "";
        DecimalFormat df1 = new DecimalFormat("#0.00");
        DecimalFormat df2 = new DecimalFormat("###0.00");
        DecimalFormat df3 = new DecimalFormat("##0.000000");

        try {
            //upit kojim se provjerava postoji li zip kod u tablici activezipcodes
            String upit = "SELECT * FROM jelvalcic_activezipcodes WHERE ZIP = '" + pZipKod + "'";
            Statement instr;
            Statement instr2;
            ResultSet odgovor;
            ResultSet odgovor2;
            Connection veza;

            Class.forName(driver);
            veza = DriverManager.getConnection(host + imeBaze, bKorisnickoIme, bKorisnickaLozinka);
            instr = veza.createStatement();

            odgovor = instr.executeQuery(upit);

            if (!odgovor.isBeforeFirst()) {
                vrati = "OK 43";//ukoliko ne postoji vraća se odgovor servera
            } else {
                //ukoliko postoji zip kod vrši se upit nad tablicom meteo_podaci kako bi se dohvatili 
                //najsvježiji meteo podaci za traženi zip kod
                upit = "SELECT meteo_podaci.*, LATITUDE, LONGITUDE FROM meteo_podaci INNER JOIN zip_codes ON meteo_podaci.ZIP_zahtjevani = zip_codes.ZIP WHERE ZIP_zahtjevani = '" + pZipKod + "' ORDER BY RBR DESC LIMIT 1;";
                instr2 = veza.createStatement();
                odgovor2 = instr2.executeQuery(upit);
                if (!odgovor.isBeforeFirst()) {
                    vrati = "OK 43";//ukoliko ne postoje podaci za traženi zip
                } else {
                    odgovor2.next();
                    vrati = "OK 10 TEMP " + df1.format(odgovor2.getFloat("Temperatura"))
                            + " VLAGA " + df1.format(odgovor2.getFloat("Vlaga"))
                            + " TLAK " + df2.format(odgovor2.getFloat("Tlak"))
                            + " GEOSIR " + df3.format(odgovor2.getDouble("LATITUDE"))
                            + " GEODUZ " + df3.format(odgovor2.getDouble("LONGITUDE")) + " ";
                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vrati;
    }
/**
 * Metoda kojom se vraćaju podaci o statusu zip koda ukoliko stigne komanda TEST ZIP
 * @param pZipKod String - zip kod za koji se provjerava status
 * @return String - vraća se status traženog zip koda (44 - traženi zip kod se 
 * ne nalazi u tablici activezipcodes, 10 - traženi zip kod se nalazi u tablici 
 * activezipcodes)
 * @throws SQLException 
 */
    private String provjeriZip(String pZipKod) throws SQLException {
        String bKorisnickoIme = konfig.getBpKonfig().getUser_username();
        String bKorisnickaLozinka = konfig.getBpKonfig().getUser_password();
        String imeBaze = konfig.getBpKonfig().getUser_database();
        String host = konfig.getBpKonfig().getServer_database();
        String driver = konfig.getBpKonfig().getDriver_database(host + imeBaze);
        String vrati = "";

        try {
            //upit kojim se provjerava postoji li zip kod u tablici activezipcodes
            String upit = "SELECT * FROM jelvalcic_activezipcodes WHERE ZIP = '" + pZipKod + "'";
            Statement instr;
            ResultSet odgovor;
            Connection veza;

            Class.forName(driver);
            veza = DriverManager.getConnection(host + imeBaze, bKorisnickoIme, bKorisnickaLozinka);
            instr = veza.createStatement();

            odgovor = instr.executeQuery(upit);

            if (!odgovor.isBeforeFirst()) {
                vrati = "OK 44";//ukoliko ne postoji
            } else {
                vrati = "OK 10";//zip kod postoji
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vrati;
    }
/**
 * Metoda kojom se dodaje zadani zip kod u tablicu activezipcodes ukoliko stigne 
 * komanda ADD ZIP
 * @param pZipKod String - zip kod koji se treba dodati u tablicu activezipcodes
 * @param pKorisnickoIme String - korisničko ime administratora koji dodaje zip kod
 * @return String - vraća se status zip koda (42 - traženi zip kod se već nalazi 
 * u tablici activezipcodes, 10 - zip kod je uspješno dodan u tablicu activezipcodes)
 * @throws SQLException 
 */
    private String dodajZip(String pZipKod, String pKorisnickoIme) throws SQLException {
        String bKorisnickoIme = konfig.getBpKonfig().getUser_username();
        String bKorisnickaLozinka = konfig.getBpKonfig().getUser_password();
        String imeBaze = konfig.getBpKonfig().getUser_database();
        String host = konfig.getBpKonfig().getServer_database();
        String driver = konfig.getBpKonfig().getDriver_database(host + imeBaze);
        String vrati = "";

        try {
            //upit kojim se unosi traženi zip kod u tablicu activezipcodes
            String upit = "INSERT INTO jelvalcic_activezipcodes(ZIP, Korisnik) VALUES (" + pZipKod + ", '" + pKorisnickoIme + "');";
            Statement instr;
            Connection veza;

            Class.forName(driver);
            veza = DriverManager.getConnection(host + imeBaze, bKorisnickoIme, bKorisnickaLozinka);
            instr = veza.createStatement();

            //provjera postoji li zahtjevani zip kod u tablici
            if (provjeriZip(pZipKod).equalsIgnoreCase("OK 44")) {
                vrati = "OK 10"; // ukoliko ne postoji dodaje se

                instr.executeUpdate(upit);
            } else {
                vrati = "OK 42";//ukoliko postoji
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vrati;
    }
/**
 * Metoda kojom se šalje mail nakon izvršene administratorske komande sa podacima
 * o izvršenoj komandi, pristiglim korisničkim komandama između dvije administratorske
 * komande, vremenu proteklom između dvije administratorske komande
 */
    private void posaljiMail() {
        String predmetStatistike = konfig.getKonfiguracija().dajPostavku("statistikaPredmet");
        String primateljStatistike = konfig.getKonfiguracija().dajPostavku("statistikaKorisnickiEmail");
        Date vrijemeKraja = new Date();
        Long ukupnoTrajanje;

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        
        if(Posrednik.getPrethodnaAdminKomanda() == null){
            ukupnoTrajanje = 0L;
        }else{
            ukupnoTrajanje = vrijemePocetka.getTime() - Posrednik.getPrethodnaAdminKomanda().getTime();
        }
        
        String msgBody;
        msgBody = "Vrijeme izvrsavanja: " + formatiranoVrijeme.format(vrijemePocetka) + "\n"
                + "Vrijeme trajanja prethodnog stanja (ms): " + ukupnoTrajanje + "\n"
                + "Broj primljenih korisnickih komandi: " + Integer.toString(Posrednik.getBrojPrimljenihKomandi()) + "\n"
                + "Broj neispravnih komandi: " + Integer.toString(Posrednik.getBrojNeispravnihKomandi()) + "\n"
                + "Broj ispravnih korisnickih komandi: " + Integer.toString(Posrednik.getBrojIspravnihKomandi()) + "\n";
         //resetiraju se brojači 
        Posrednik.resetirajBrojace(vrijemePocetka);

        try {
            Message msg = new MimeMessage(session);
            msg.setSentDate(new Date());
            try {
                msg.setFrom(new InternetAddress(korisnickoIme, ""));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(primateljStatistike, ""));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
            }
            //po defaultu je text/plain
            msg.setSubject(predmetStatistike);
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
}

    /**
     * Metoda za pokretanje dretve servera
     */
    @Override
    public synchronized void start() {
        System.out.println("Klijent se javio!");

        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metoda koja obrađuje komande
     */
    @Override
    public void run() {
        try {

            try (InputStream is = klijent.getInputStream(); OutputStream os = klijent.getOutputStream()) {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int znak = is.read();
                    if (znak == -1) {
                        break;
                    }
                    sb.append((char) znak);

                }
                komanda = sb.toString();
                System.out.println("Komanda: " + komanda);
                String odgovor1 = komanda;
                
                if (komanda.indexOf("PASSWD") > 0) {
                    //provjerava se da li je već aktivna administratorska komanda
                    if(Posrednik.isAdminAktivan()){
                        return;
                    }
                    //postavlja se zastavica da je aktivna administratorska komanda
                    Posrednik.setAdminAktivan(true);
                }
                if (komanda.indexOf("PASSWD") > 0) {
                    //admin
                    
                    //sintaksa = "^USER ([a-z0-9_]{3,15}); PASSWD ([a-z0-9_]{3,15}); ([a-z0-9_]{3,50});";
                    String[] p = komanda.split(";");
                    //komanda.toString().trim();
                    /*Pattern pattern = Pattern.compile(sintaksa);
                     Matcher m = pattern.matcher(p);
                     boolean status = m.matches();

                     korisnickoIme = "Nepoznato";
                     if (status) {
                     korisnickoIme = m.group(1);
                     korisnickaLozinka = m.group(2);
                     }*/
                    String[] ki = p[0].split(" ");
                    String[] kl = p[1].split(" ");
                    korisnickoIme = ki[1].trim();
                    korisnickaLozinka = kl[2].trim();

                    korisnickaKomanda = false;
                    try {
                        //autentikacija administratora
                        if (provjeriAdministratora(korisnickoIme, korisnickaLozinka)) {
                            if (komanda.indexOf("PAUSE") > 0) {
                                if (Posrednik.isPauza()) {

                                    odgovorServera = "OK 40";
                                } else {
                                    Posrednik.setPauza(true);

                                    odgovorServera = "OK 10";
                                }

                            } else if (komanda.indexOf("STOP") > 0) {
                                if (Posrednik.isKraj()) {
                                    odgovorServera = "OK 42";
                                } else {
                                    Posrednik.setKraj(true);
                                    odgovorServera = "OK 10";

                                }
                            } else if (komanda.indexOf("START") > 0) {
                                if (Posrednik.isPauza()) {
                                    Posrednik.setPauza(false);

                                    odgovorServera = "OK 10";
                                } else {

                                    odgovorServera = "OK 41";
                                }
                            } else {
                                //komande za zipove
                                String temp = "TEST ZIP";
                                if (komanda.indexOf(temp) > 0) {

                                    zipKod = komanda.substring(komanda.indexOf(temp) + temp.length(), komanda.length());
                                    zipKod = zipKod.trim();
                                    zipKod = zipKod.substring(0, zipKod.length() - 1);

                                    odgovorServera = provjeriZip(zipKod);

                                } else if (komanda.indexOf("ADD ZIP") > 0) {
                                    String temp2 = "ADD ZIP";

                                    zipKod = komanda.substring(komanda.indexOf(temp2) + temp2.length(), komanda.length());
                                    zipKod = zipKod.trim();
                                    zipKod = zipKod.substring(0, zipKod.length() - 1);

                                    odgovorServera = dodajZip(zipKod, korisnickoIme);

                                } else {
                                    ispravnaKomanda = false;
                                    Posrednik.incBrojNeispravnihKomandi();
                                    odgovorServera = "ERROR - Neispravna komanda!";
                                }
                            }
                        } else {
                            ispravnaKomanda = false;
                            Posrednik.setAdminAktivan(false);
                            Posrednik.incBrojNeispravnihKomandi();
                            odgovorServera = "OK 30";
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //user
                    korisnickaKomanda = true;
                    //ukoliko su došle naredbe korisnika
                    //USER username; GET ZIP nnnnn;
                    sintaksa = "^USER ([a-z0-9_]{3,15}); GET ZIP ([0-9_]{3,5});";
                    String p = komanda.toString().trim();
                    Pattern pattern = Pattern.compile(sintaksa);
                    Matcher m = pattern.matcher(p);
                    boolean status = m.matches();
                    
                    Posrednik.incBrojPrimljenihKomandi();
                    if (status) {
                        
                        korisnickoIme = m.group(1);
                        zipKod = m.group(2);
                        try {
                            odgovorServera = vratiMeteoPodatke(zipKod);
                            Posrednik.incBrojIspravnihKomandi();
                        } catch (SQLException ex) {
                            Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        ispravnaKomanda = false;
                        Posrednik.incBrojNeispravnihKomandi();
                        odgovorServera = "ERROR - Neispravna komanda!";
                    }

                }
                try {
                    zapisiUDnevnik(korisnickoIme, komanda, odgovorServera, korisnickaKomanda, ispravnaKomanda);

                    if (!korisnickaKomanda && ispravnaKomanda) {
                        
                        posaljiMail();
                        Posrednik.setAdminAktivan(false);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JednostavniPosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
                }
                String odgovorServera2 = odgovorServera + " Komanda: " + odgovor1 + " (zapisano)";
                os.write(odgovorServera2.getBytes("UTF-8"));
                os.flush();
                os.close();
                is.close();
                klijent.close();

            }
        } catch (IOException ex) {
            Logger.getLogger(JednostavniPosluzitelj.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        radi = false;
        super.interrupt();
    }
}
