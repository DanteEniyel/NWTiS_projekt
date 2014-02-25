/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;

/**
 *
 * @author jelvalcic
 * Klasa kojom se autenticira korisnik
 */
public class ProvjeraKorisnika {
    private Korisnik korisnik = new Korisnik();
    
    /**
     * Metoda kojom se provodi provjera korisnika prema bazi podataka
     * @param context ServletContext - kontekst faceleta
     * @param korisnicko_ime String - korisničko ime koje provjeravamo
     * @param korisnicka_lozinka String -lozinka koju provjeravamo
     * @return String - OK-provjera je uspješna, NOT_OK-neuspješna provjera, ERROR-došlo je do greške
     */
    
    public Korisnik provjeraKorisnika(ServletContext context, String korisnicko_ime, String korisnicka_lozinka) {
        //FacesContext c = FacesContext.getCurrentInstance();
        
        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija");
        if (bp == null) {
            return null;
        }
        if (korisnicko_ime == null || korisnicka_lozinka.length() == 0 || korisnicka_lozinka == null || korisnicka_lozinka.length() == 0) {
            return null;
        }

        String connUrl = bp.getBpKonfig().getServer_database() + bp.getBpKonfig().getUser_database();
        String sql = "SELECT * FROM korisnici WHERE  Korisnicko_ime = '" + korisnicko_ime + "' AND Korisnicka_lozinka = '" + korisnicka_lozinka + "';";
        try {
            Class.forName(bp.getBpKonfig().getDriver_database());
        } catch (ClassNotFoundException ex) {
            return null;
        }

        try (
                Connection conn = DriverManager.getConnection(connUrl, bp.getBpKonfig().getUser_username(), bp.getBpKonfig().getUser_password());
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            if (!rs.next()) {
                return null;
            }
            
            korisnik.setKorisnik(rs.getString("Korisnicko_ime"));
            korisnik.setIme(rs.getString("Ime"));
            korisnik.setPrezime(rs.getString("Prezime"));
            korisnik.setVrsta(rs.getInt("Status_korisnika"));
            
            return korisnik;

        } catch (SQLException ex) {
            return null;
        }
    }
    
}
