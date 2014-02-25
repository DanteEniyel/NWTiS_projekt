/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.kontrole;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.jelvalcicJMSObjekti.mail.MailJMSPoruka;
import org.foi.nwtis.jelvalcicapp3.sb.SpremacPoruka;

/**
 *
 * @author jelvalcic
 * Klasa dretva koja obrađuje primljene JMS poruke koje se šalju nakon intervala
 * obrade mail poruka
 */
public class ObradaMailJMSPoruke extends Thread {
    SpremacPoruka spremacPoruka = lookupSpremacPorukaBean();
    

    
    private MailJMSPoruka mjp;
    
    public ObradaMailJMSPoruke(ObjectMessage message) {
        try {
            this.mjp = (MailJMSPoruka) message.getObject();
            //spremanje poruke u objekt SpremacPoruka kako bi se mogla ispisati
            spremacPoruka.dodajPoruku(mjp);
            System.out.println("App3: Vrijeme pocetka: ");
        } catch (JMSException ex) {
            Logger.getLogger(ObradaMailJMSPoruke.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("App3: Vrijeme pocetka: " + mjp.getVrijemePocetka());
    }
    
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    private SpremacPoruka lookupSpremacPorukaBean() {
        try {
            Context c = new InitialContext();
            return (SpremacPoruka) c.lookup("java:global/jelvalcic_aplikacija_3/jelvalcic_aplikacija_3-ejb/SpremacPoruka!org.foi.nwtis.jelvalcicapp3.sb.SpremacPoruka");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
  
    
}
