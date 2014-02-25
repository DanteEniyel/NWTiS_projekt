/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.mb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.foi.nwtis.jelvalcicapp3.kontrole.ObradaMailJMSPoruke;

/**
 *
 * @author jelvalcic
 * Klasa slušača JMS poruka koje se šalju nakon intervala obrade mail poruka
 */
@MessageDriven(mappedName = "NWTiS_MailJMS", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MailJMSPrimatelj implements MessageListener {
    
    public MailJMSPrimatelj() {
    }
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage) message;
        ObradaMailJMSPoruke omjp = new ObradaMailJMSPoruke(om);
        omjp.start();
    }
}
