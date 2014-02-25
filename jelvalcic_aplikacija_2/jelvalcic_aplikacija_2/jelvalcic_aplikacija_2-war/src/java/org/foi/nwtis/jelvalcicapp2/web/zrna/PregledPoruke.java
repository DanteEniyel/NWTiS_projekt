/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.Poruka;
/**
 *
 * @author jelvalcic
 * Klasa za pregled odabrane poruke
 */
@ManagedBean
@RequestScoped
public class PregledPoruke {
    private Poruka poruka;
    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke() {
    }

    public Poruka getPoruka() {
        PregledSvihPoruka psp = (PregledSvihPoruka) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pregledSvihPoruka");
        poruka = psp.getPoruka();
        
     
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
       
        
    }
   
}
