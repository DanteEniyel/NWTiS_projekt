/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.sb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import org.foi.nwtis.jelvalcicJMSObjekti.mail.MailJMSPoruka;
import org.foi.nwtis.jelvalcicJMSObjekti.zip.ZipJMSPoruka;

/**
 *
 * @author jelvalcic
 * Klasa objekta SpremacPoruka koja služi za spremanje JMS poruka kako bi se 
 * kasnije mogle ispisati
 */
@Singleton
@LocalBean
public class SpremacPoruka implements Serializable{

    private List<MailJMSPoruka> mailPoruka = new ArrayList<>();
    private List<ZipJMSPoruka> zipPoruka = new ArrayList<>();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public SpremacPoruka() {
    } 

    public List<MailJMSPoruka> getMailPoruka() {
        return mailPoruka;
    }

    public void setMailPoruka(List<MailJMSPoruka> mailPoruka) {
        this.mailPoruka = mailPoruka;
    }
    
    public List<ZipJMSPoruka> getZipPoruka() {
        return zipPoruka;
    }

    public void setZipPoruka(List<ZipJMSPoruka> zipPoruka) {
        this.zipPoruka = zipPoruka;
    }
    
    
    public void dodajPoruku(MailJMSPoruka poruka){
        mailPoruka.add(poruka);
    }
    
    
    public void dodajPorukuZip(ZipJMSPoruka poruka){
        zipPoruka.add(poruka);
    }
    
}