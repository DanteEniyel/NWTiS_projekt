/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import org.foi.nwtis.jelvalcicapp3.rest.klijent.RestKlijent;

/**
 *
 * @author jelvalcic
 * Klasa za ispis aktivnih korisnika aplikacije 2 putem REST servisa
 */
@Named(value = "restPodaciKorisnik")
@SessionScoped
public class RestPodaciKorisnik implements Serializable {
    private String restPodaci;
    /**
     * Creates a new instance of RestPodaciKorisnik
     */
    public RestPodaciKorisnik() {
    }

    public String getRestPodaci() {
        RestKlijent rk = new RestKlijent();
        restPodaci = rk.getHtml();
        return restPodaci;
    }

    public void setRestPodaci(String restPodaci) {
        this.restPodaci = restPodaci;
    }
    
    
}
