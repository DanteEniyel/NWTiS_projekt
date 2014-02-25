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
import org.foi.nwtis.jelvalcicJMSObjekti.mail.MailJMSPoruka;
import org.foi.nwtis.jelvalcicapp3.sb.SpremacPoruka;

/**
 *
 * @author jelvalcic
 * Klasa kojom se dohvaćaju primljene Mail JMS poruke i pripremaju za ispis na JSF stranicu
 */
@Named(value = "pregledMailJMSPoruka")
@SessionScoped
public class PregledMailJMSPoruka implements Serializable {
    @EJB
    private SpremacPoruka spremacPoruka;
    
    private List<MailJMSPoruka> mailPoruka = new ArrayList<>();
    /**
     * Creates a new instance of PregledMailJMSPoruka
     */
    public PregledMailJMSPoruka() {
    }

    public List<MailJMSPoruka> getMailPoruka() {
        mailPoruka = spremacPoruka.getMailPoruka();
        return mailPoruka;
    }

    public void setMailPoruka(List<MailJMSPoruka> mailPoruka) {
        this.mailPoruka = mailPoruka;
    }
    
    public void obrisiSveMailJMSPoruke(){
        mailPoruka.clear();
        spremacPoruka.setMailPoruka(mailPoruka);
    }
    
    public void obrisiMailJMSPoruku(MailJMSPoruka poruka){
        mailPoruka.remove(poruka);
        spremacPoruka.setMailPoruka(mailPoruka);
    }
}
