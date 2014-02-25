/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.jelvalcicJMSObjekti.zip.ZipJMSPoruka;
import org.foi.nwtis.jelvalcicapp3.sb.SpremacPoruka;

/**
 * 
 * @author jelvalcic
 * lasa kojom se dohvaćaju primljene Zip JMS poruke i pripremaju za ispis na JSF stranicu
 */
@Named(value = "pregledZipJMSPoruka")
@SessionScoped
public class PregledZipJMSPoruka implements Serializable {
    @EJB
    private SpremacPoruka spremacPoruka;
    
    private List<ZipJMSPoruka> zipPoruka = new ArrayList<>();
    /**
     * Creates a new instance of PregledZipJMSPoruka
     */
    public PregledZipJMSPoruka() {
    }

    public List<ZipJMSPoruka> getZipPoruka() {
        zipPoruka = spremacPoruka.getZipPoruka();
        return zipPoruka;
    }

    public void setZipPoruka(List<ZipJMSPoruka> zipPoruka) {
        this.zipPoruka = zipPoruka;
    }
    
    public void obrisiSveZipJMSPoruke(){
     zipPoruka.clear();
     spremacPoruka.setZipPoruka(zipPoruka);
    }
    
    public void obrisiZipJMSPoruku(ZipJMSPoruka poruka){
        zipPoruka.remove(poruka);
        spremacPoruka.setZipPoruka(zipPoruka);
    }
}
