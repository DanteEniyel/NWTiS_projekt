/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.kontrole;

import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import org.foi.nwtis.jelvalcicJMSObjekti.mail.MailJMSPoruka;

/**
 *
 * @author jelvalcic
 * Klasa dretve za obradu mail poruka
 */
public class ObradaPoruke extends Thread {

    private KorisnickaKonfiguracija bpKonf = null;
    public static String email_posluzitelj;
    private String email_port;
    private int interval;
    public static String korisnicko_ime;
    public static String korisnicka_lozinka;
    private String trazeni_predmet;
    //varijable za mape
    private String ispravnePorukeNaziv;
    private String ostalePorukeNaziv;
    //dretva
    private boolean radi;
    ServletContext context = null;

    public ObradaPoruke() {
    }

    public KorisnickaKonfiguracija getBpKonf() {
        return bpKonf;
    }

    public void setBpKonf(KorisnickaKonfiguracija bpKonf) {
        this.bpKonf = bpKonf;
    }

    @Override
    public synchronized void start() {
        radi = true;

        context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        setBpKonf((KorisnickaKonfiguracija) context.getAttribute("BP_Konfiguracija"));


        email_posluzitelj = bpKonf.getObradaAdresaPosluzitelja();
        email_port = bpKonf.getObradaImapPort();
        interval = bpKonf.getObradaInterval();
        korisnicko_ime = bpKonf.getObradaEmailAdresa();
        korisnicka_lozinka = bpKonf.getObradaLozinka();
        trazeni_predmet = bpKonf.getObradaPredmet();

        //imena mapa
        ispravnePorukeNaziv = bpKonf.getObradaMapaIspravno();
        ostalePorukeNaziv = bpKonf.getObradaMapaOstalo();

        super.start();
    }

    @Override
    public void run() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;

        Folder ispravnePorukeMapa = null;
        Folder ostalePorukeMapa = null;

        boolean spojen;

//        .==.        .==.          
//       //`^\\      //^`\\         
//      // ^ ^\(\__/)/^ ^^\\        
//     //^ ^^ ^/6  6\ ^^ ^ \\       
//    //^ ^^ ^/( .. )\^ ^ ^ \\      
//   // ^^ ^/\| v""v |/\^ ^ ^\\     
//  // ^^/\/ /  `~~`  \ \/\^ ^\\    
//  -----------------------------
/// HERE BE DRAGONS
// SO TAKE THIS
//         /
// *//////{<>==================-
//         \


        while (radi) {
            int brojIspravnihPoruka = 0;
            int brojPrimljenihPoruka = 0;
            Date vrijemePocetka = new Date();
            try {
                Properties props = System.getProperties();
                props.put("mail.protocol.port", Integer.valueOf(bpKonf.getObradaImapPort()));
                props.put("mail.smtp.debug", "true");
                session = Session.getDefaultInstance(props, null);
                store = session.getStore("imap");

                spojen = true;
                try {
                    store.connect(email_posluzitelj, korisnicko_ime, korisnicka_lozinka);
                } catch (javax.mail.MessagingException ex) {
                    spojen = false;
                }
                if (spojen) {

                    // Get a handle on the default folder
                    folder = store.getDefaultFolder();

                    // Retrieve the "Inbox"
                    folder = folder.getFolder("inbox");

                    //Reading the Email Index in Read / Write Mode
                    folder.open(Folder.READ_WRITE);

//-----------------KREIRANJE OSTALIH MAPA AKO NE POSTOJE------------------------------POČETAK
                    ispravnePorukeMapa = store.getFolder(ispravnePorukeNaziv);
                    // kreira mapu za ispravne poruke ako ne postoji
                    if (!ispravnePorukeMapa.exists()) {
                        ispravnePorukeMapa.create(Folder.HOLDS_MESSAGES);
                    }
                    ispravnePorukeMapa.open(Folder.READ_WRITE);

                    ostalePorukeMapa = store.getFolder(ostalePorukeNaziv);
                    // kreira mapu za ostale poruke ako ne postoji
                    if (!ostalePorukeMapa.exists()) {
                        ostalePorukeMapa.create(Folder.HOLDS_MESSAGES);
                    }
                    ostalePorukeMapa.open(Folder.READ_WRITE);

//-----------------KREIRANJE OSTALIH MAPA AKO NE POSTOJE------------------------------KRAJ

                    // Retrieve the messages
                    messages = folder.getMessages();
                    // Loop over all of the messages
                    for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                        brojPrimljenihPoruka = messages.length;

                        // Retrieve the next message to be read
                        message = messages[messageNumber];


                        // Get the subject information
                        subject = message.getSubject();
                        // Get the content type
                        contentType = message.getContentType().toLowerCase();//jer java mail piše veliko text/plain 


                        if (subject.contentEquals(trazeni_predmet) && contentType.startsWith("text/plain")) {
                            brojIspravnihPoruka++;
                            ispravnePorukeMapa.appendMessages(new Message[]{message});
                            message.setFlag(Flags.Flag.DELETED, true);

                        } else {
                            ostalePorukeMapa.appendMessages(new Message[]{message});
                            message.setFlag(Flags.Flag.DELETED, true);

                        }

                    }
                    // Close the folder
                    ispravnePorukeMapa.expunge();
                    ostalePorukeMapa.expunge();
                    folder.expunge();

                    ispravnePorukeMapa.close(true);
                    ostalePorukeMapa.close(true);
                    folder.close(true);

                    // Close the message store
                    store.close();
                }
                try {
                    //šalje se JMS poruka aplikaciji 3
                    posaljiJMS(vrijemePocetka, brojIspravnihPoruka, brojPrimljenihPoruka);
                    System.out.println("App2: Saljem poruku, odoh spavat...");
                    sleep(interval * 1000);
                    System.out.println("App2: Back to work...");
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObradaPoruke.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ObradaPoruke.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void interrupt() {
        radi = false;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

        
/**
 * Metoda za slanje JMS poruke aplikaciji 3
 * @param vrijemePocetka Date - vrijeme početka intervala obrade
 * @param brojIspravnihPoruka int - broj obrađenih poruka intervala koje dogovaraju
 * predmetu poruke navedenom u korisničkoj konfiguraciji
 * @param brojPrimljenihPoruka int - broj ukupnih primljenih/obrađenih poruka jednog intervala
 */
    
    private void posaljiJMS(Date vrijemePocetka, int brojIspravnihPoruka, int brojPrimljenihPoruka) {
        MailJMSPoruka mjp = new MailJMSPoruka();
        SimpleDateFormat formatiranoVrijeme = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        //vrijeme završetka intervala obrade
        Date vrijemeKraja = new Date();
        //postavljanje parametara za slanje
        mjp.setBrojNwtisPoruka(brojIspravnihPoruka);
        mjp.setBrojProcitanihPoruka(brojPrimljenihPoruka);
        mjp.setVrijemeKraja(formatiranoVrijeme.format(vrijemeKraja));
        mjp.setVrijemePocetka(formatiranoVrijeme.format(vrijemePocetka));
        try {
            //slanje JMS poruke
            sendJMSMessageToNWTiS_MailJMS(mjp);
        } catch (NamingException ex) {
            Logger.getLogger(ObradaPoruke.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(ObradaPoruke.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private javax.jms.Message createJMSMessageFornWTiS_MailJMS(javax.jms.Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

    private void sendJMSMessageToNWTiS_MailJMS(MailJMSPoruka messageData) throws NamingException, JMSException {
        Context c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup("java:comp/env/NWTiS_MailJMSFactory");
        Connection conn = null;
        javax.jms.Session s = null;
        try {
            conn = cf.createConnection();
            s = conn.createSession(false, s.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) c.lookup("java:comp/env/NWTiS_MailJMS");
            MessageProducer mp = s.createProducer(destination);
            ObjectMessage om = s.createObjectMessage();
            om.setObject(messageData);
            om.setJMSType(messageData.getClass().getName());
            mp.send(om);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
//alas you came out alive :)    
}