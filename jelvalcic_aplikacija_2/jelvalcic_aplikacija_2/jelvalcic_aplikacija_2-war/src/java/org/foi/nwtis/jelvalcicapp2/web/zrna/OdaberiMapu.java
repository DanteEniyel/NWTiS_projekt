/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author jelvalcic 
 * Klasa koja upravlja odabirom mapa za pregled poruka
 */
@Named(value = "odaberiMapu")
@RequestScoped
public class OdaberiMapu {

    private String ime;

    public String getIme() {
        return ime;
    }

    /**
     * Metoda koja postavlja ime koje je odabrano
     *
     * @param ime odabrano ime mape
     */
    public void setIme(String ime) {
        PregledSvihPoruka mapa = (PregledSvihPoruka) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pregledSvihPoruka");
        mapa.setOdabranaMapa(ime);
        this.ime = ime;
    }

    public String posalji() {
        return "odaberiMapu";
    }

    /**
     * Creates a new instance of OdaberiMapu
     */
    public OdaberiMapu() {
    }
}
