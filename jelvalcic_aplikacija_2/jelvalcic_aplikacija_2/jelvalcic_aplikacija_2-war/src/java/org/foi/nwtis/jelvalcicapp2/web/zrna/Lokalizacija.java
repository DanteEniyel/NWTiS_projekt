/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author jelvalcic
 * Klasa lokalizacije web aplikacije
 */
@Named(value = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {

    private Map<String, Object> jezici;
    private String odabraniJezik = "Hrvatski";
    private Locale odabraniLocale = new Locale("hr");
    
    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
        jezici = new HashMap<String, Object>();
        jezici.put("English", Locale.ENGLISH);
        jezici.put("Deutsch", Locale.GERMAN);
        jezici.put("Hrvatski", new Locale("hr"));
        odabraniJezik = "Hrvatski";
    }

    public Map<String, Object> getJezici() {
        return jezici;
    }

    public void setJezici(Map<String, Object> jezici) {
        this.jezici = jezici;
    }

    public String getOdabraniJezik() {
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
    }

    public Locale getOdabraniLocale() {
        return odabraniLocale;
    }

    public void setOdabraniLocale(Locale odabraniLocale) {
        this.odabraniLocale = odabraniLocale;
    }
    
    /**
     * Metoda za biranje i spremanje odabranog jezika u kontekst aplikacije
     * @return String - OK u slucaju uspjesnog mijenjanja jezika
     */
    public Object odaberiJezik() {

        for (Map.Entry<String, Object> entry : jezici.entrySet()) {
            if (entry.getValue().toString().equals(odabraniJezik)) {
                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());
                odabraniLocale = (Locale) entry.getValue();

            }
        }
        return "OK";
    }
    
    /**
     * Metoda slušača promjene vrijednosti
     * Koja mijenja jezik lokalizacije u kontekstu kada korisnik klikne na 
     * neki od radio button-a
     * @param e ValueChangeEvent - jezik koji je odabran
     */
    public void changedJezik(ValueChangeEvent e) {

        String newLocaleValue = e.getNewValue().toString();

        //loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : jezici.entrySet()) {

            if (entry.getValue().toString().equals(newLocaleValue)) {
                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());
                odabraniLocale = (Locale) entry.getValue();

            }
        }
    }
}
