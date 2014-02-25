/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;

/**
 *
 * @author jelvalcic
 * Pomoćna klasa za validaciju korisnika iz login forme i tokom obrade poruka
 */
public class PomocnoSmetalo {
    
    /**
     * Metoda kojom se provodi provjera korisnika prema bazi podataka
     * @param context ServletContext - kontekst faceleta
     * @param korisnicko_ime String - korisničko ime koje provjeravamo
     * @param korisnicka_lozinka String - lozinka koju provjeravamo
     * @return String - OK-provjera je uspješna, NOT_OK-neuspješna provjera, ERROR-došlo je do greške
     */
    
    public static String provjeraKorisnika(ServletContext context, String korisnicko_ime, String korisnicka_lozinka) {
        //FacesContext c = FacesContext.getCurrentInstance();

        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija");
        if (bp == null) {
            return "ERROR";
        }
        if (korisnicko_ime == null || korisnicka_lozinka.length() == 0 || korisnicka_lozinka == null || korisnicka_lozinka.length() == 0) {
            return "NOT_OK";
        }

        String connUrl = bp.getBpKonfig().getServer_database() + bp.getBpKonfig().getUser_database();
        String sql = "SELECT ime, prezime, lozinka, email_adresa, vrsta FROM polaznici WHERE kor_ime = '" + korisnicko_ime + "' AND lozinka = '" + korisnicka_lozinka + "'";
        try {
            Class.forName(bp.getBpKonfig().getDriver_database());
        } catch (ClassNotFoundException ex) {
            return "ERROR";
        }

        try (
                Connection conn = DriverManager.getConnection(connUrl, bp.getBpKonfig().getUser_username(), bp.getBpKonfig().getUser_password());
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            if (!rs.next()) {
                return "NOT_OK";
            }

            return "OK";

        } catch (SQLException ex) {
            return "ERROR";
        }
    }
    
    public String getEmail_posluzitelj(ServletContext context) {
        //FacesContext c = FacesContext.getCurrentInstance();

        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija");
        return bp.getKorisnikAdresaPosluzitelja();
    }
    
    public String getKorisnicko_ime(ServletContext context) {
        //FacesContext c = FacesContext.getCurrentInstance();

        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija");
        return bp.getKorisnikLozinka();
    }
    
    public String getKorisnicka_lozinka(ServletContext context) {
        //FacesContext c = FacesContext.getCurrentInstance();

        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija");
        return bp.getKorisnikLozinka();
    }
    
}
