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
import org.foi.nwtis.jelvalcicapp3.kontrole.ObradaZipJMSPoruke;

/**
 *
 * @author jelvalcic
 * Klasa slušača JMS poruka koje se primaju kada se dodaje zip kod
 */
@MessageDriven(mappedName = "NWTiS_ZipJMS", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ZipJMSPrimatelj implements MessageListener {
    
    public ZipJMSPrimatelj() {
    }
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage) message;
        ObradaZipJMSPoruke zmjp = new ObradaZipJMSPoruke(om);
        zmjp.start();
    }
}
