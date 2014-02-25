/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletContext;
import org.foi.nwtis.jelvalcic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jelvalcicapp2.web.slusaci.KontekstAplikacije;

/**
 *
 * @author jelvalcic
 * Pomoćna klasa za login
 */
public class Pomocna {
    Korisnik korisnik = null;
    public Pomocna() {
    }
    
    public Korisnik provjeriKorisnika(String korisnickoIme, String lozinka) {
        ServletContext sc = KontekstAplikacije.getKontekst();
        BP_Konfiguracija bpKonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        
        String url = bpKonf.getServer_database() + bpKonf.getUser_database();
        String korIme = bpKonf.getUser_username();
        String korLoz = bpKonf.getUser_password();
        String upit = "SELECT * FROM KORISNICI WHERE KOR_IME = '" + korisnickoIme + "' AND KOR_LOZINKA = '" + lozinka + "'";

        try (
                Connection veza = DriverManager.getConnection(url, korIme, korLoz);
                Statement instr = veza.createStatement();
                ResultSet odgovor = instr.executeQuery(upit);) {
            while (odgovor.next()) {
                korisnik = new Korisnik();
                korisnik.setKorisnik(korisnickoIme);
                korisnik.setVrsta(odgovor.getInt("KOR_TIP"));
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return korisnik;
    }
    
}
