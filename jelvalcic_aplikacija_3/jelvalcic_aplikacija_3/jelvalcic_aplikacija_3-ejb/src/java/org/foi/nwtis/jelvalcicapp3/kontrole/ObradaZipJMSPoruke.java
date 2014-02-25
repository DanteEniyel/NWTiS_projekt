/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.kontrole;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.jelvalcicJMSObjekti.zip.ZipJMSPoruka;
import org.foi.nwtis.jelvalcicapp3.sb.SpremacPoruka;

/**
 *
 * @author jelvalcic
 * Klasa dretva koja obrađuje primljene JMS poruke koje se šalju kod dodavanja
 * zip koda
 */
public class ObradaZipJMSPoruke extends Thread {

    SpremacPoruka spremacPoruka = lookupSpremacPorukaBean();
    private ZipJMSPoruka zjp;

    public ObradaZipJMSPoruke(ObjectMessage message) {
        try {
            this.zjp = (ZipJMSPoruka) message.getObject();
            //spremanje poruke u objekt SpremacPoruka kako bi se mogla ispisati
            spremacPoruka.dodajPorukuZip(zjp);
            posaljiKomandu(zjp.getZipKod());
            System.out.println("App3: Cekam zip: ");
        } catch (JMSException ex) {
            Logger.getLogger(ObradaZipJMSPoruke.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("App3: Pristigli zip: " + zjp.getZipKod());
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
/**
 * Metoda kojom se šalje komanda za dodavanje zip koda aplikaciji 1 u popis aktivnih zip kodova
 * @param zipKod zip kod koji se dodaje u popis aktivnih zip kodova
 */
    private void posaljiKomandu(String zipKod) {

        String komanda = "USER jura; PASSWD stublic1234; ADD ZIP " + zipKod + ";";
        String serverIp = "127.0.0.1";
        int port = 10015;
        String odgovorServera = "";

        try {

            Socket server = new Socket(serverIp, port);
            OutputStream os = server.getOutputStream();
            InputStream is = server.getInputStream();
            os.write(komanda.getBytes());
            os.flush();
            server.shutdownOutput();

            StringBuilder odgovor = new StringBuilder();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                odgovor.append((char) znak);
            }

            odgovorServera = odgovor.toString().trim();

            System.out.println("Odgovor servera: " + odgovorServera);
            os.flush();
            os.close();
            is.close();

        } catch (UnknownHostException ex) {
            Logger.getLogger(ObradaZipJMSPoruke.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaZipJMSPoruke.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
