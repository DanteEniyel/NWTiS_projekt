/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.KorisnickaKonfiguracija;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.Poruka;
import org.foi.nwtis.jelvalcicapp2.web.slusaci.KontekstAplikacije;

/**
 *
 * @author jelvalcic
 * Klasa za pregled svih poruka nekog korisnika temeljem odabrane mape
 */
@ManagedBean
@SessionScoped
public class PregledSvihPoruka implements Serializable {
   
    private String email_posluzitelj;
    private String korisnicko_ime ;
    private String korisnicka_lozinka = "";
    private List<Poruka> poruke = new ArrayList<Poruka>();
    private String porukaID;
    private Poruka poruka;
    private List popisMapa = new ArrayList();
    public String odabranaMapa = "inbox";
    public String tekstPoruke;
    public int brojStranice = 0;
    public int brojPorukaPoStranici;
    public ServletContext kontekst = KontekstAplikacije.getKontekst();;
    
    /**
     * Creates a new instance of PregledSvihPoruka
     */
    public PregledSvihPoruka() {
        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) kontekst.getAttribute("BP_Konfiguracija");
        email_posluzitelj = bp.getKorisnikAdresaPosluzitelja();
        korisnicko_ime = bp.getKorisnikEmailAdresa();
        korisnicka_lozinka = bp.getKorisnikLozinka();
        brojPorukaPoStranici = bp.getBrojPorukaPoStranici();
        
    }

    public List<Poruka> getPoruke() {
        KorisnickaKonfiguracija bp = (KorisnickaKonfiguracija) kontekst.getAttribute("BP_Konfiguracija");
        email_posluzitelj = bp.getKorisnikAdresaPosluzitelja();
        korisnicko_ime = bp.getKorisnikEmailAdresa();
        korisnicka_lozinka = bp.getKorisnikLozinka();
        brojPorukaPoStranici = bp.getBrojPorukaPoStranici();
        preuzmiPoruke();
        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    public String getPorukaID() {
        return porukaID;
    }

    public String pregledPoruke() {

        return "OK";
    }

    public void setPorukaID(String porukaID) {
        poruka = null;
        for (Poruka p : poruke) {
            if (p.getId().equals(porukaID)) {
                poruka = p;
                break;
            }
        }
        this.porukaID = porukaID;
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    public List getPopisMapa() {
        return popisMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
        this.brojStranice = 0;
    }

    public int getBrojStranice() {
        return brojStranice;
    }

    public void setBrojStranice(int brojStranice) {
        this.brojStranice = brojStranice;
    }

    public void povecajBrojStranice(){
        this.brojStranice++;//TODO ako bude vremena napraviti i gornju granicu
    }
    
    public void smanjiBrojStranice(){
        this.brojStranice--;
        if(this.brojStranice < 0){
            this.brojStranice = 0; // tako da broj stranice ne ode u nulu
        }
    }
    
    //Responsible for printing Data to Console
    private void printData(String data) {
        System.out.println(data);
    }

    private void preuzmiPoruke() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        Object messagecontentObject = null;
        String sender = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;

        try {
            session = Session.getDefaultInstance(System.getProperties(), null);

            store = session.getStore("imap");

            store.connect(email_posluzitelj, korisnicko_ime, korisnicka_lozinka);

            // Get a handle on the default folder
            folder = store.getDefaultFolder();


            // Retrieve the "Inbox"
            folder = folder.getFolder(odabranaMapa);

            //Reading the Email Index in Read / Write Mode
            folder.open(Folder.READ_ONLY);
            // Retrieve the messages
            poruke = new ArrayList<Poruka>();
            
            messages = folder.getMessages();

            // Loop over all of the messages
            int od =(messages.length-1) - (brojStranice*brojPorukaPoStranici);
            
            //int doo = od + brojPorukaPoStranici;
            int doo = od - (brojPorukaPoStranici-1);
            if(doo < 0 ){
                doo = 0;
            }
            
            for (int messageNumber = od; messageNumber >= doo; messageNumber--) {
                // Retrieve the next message to be read
                message = messages[messageNumber];

                // Retrieve the message content
                messagecontentObject = message.getContent();

                // Determine email type
                if (messagecontentObject instanceof Multipart) {
                    sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                    // If the "personal" information has no entry, check the address for the sender information

                    if (sender == null) {
                        sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                    }
                    sender = sender.substring(0, sender.indexOf(":"));
                    // Get the subject information
                    subject = message.getSubject();

                    // Retrieve the Multipart object from the message
                    multipart = (Multipart) message.getContent();

                    // Loop over the parts of the email
                    for (int i = 0; i < multipart.getCount(); i++) {
                        // Retrieve the next part
                        part = multipart.getBodyPart(i);

                        // Get the content type
                        contentType = part.getContentType().toLowerCase();

                        // Display the content type
                        String fileName = "";
                        if (contentType.startsWith("text/plain")) {
                            
                            tekstPoruke = "";
                            tekstPoruke = part.getContent().toString();
                        } else {
                            // Retrieve the file name
                            fileName = part.getFileName();
                            contentType = contentType.substring(0, contentType.indexOf(";"));

                        }


                    }
                    String[] zaglavlje = message.getHeader("Message-ID");
                    String messId = "";
                    if (zaglavlje != null && zaglavlje.length > 0) {
                        messId = zaglavlje[0];
                    }

                    Poruka po = new Poruka(messId, message.getSentDate(), sender, subject, message.getContentType(),
                            message.getSize(), message.getFlags(), true, true, tekstPoruke);

                    poruke.add(po);

                } else {
                    sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                    // If the "personal" information has no entry, check the address for the sender information

                    if (sender == null) {
                        sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                    }

                    sender = sender.substring(0, sender.indexOf(":"));
                    // Get the subject information
                    subject = message.getSubject();

                    String[] zaglavlje = message.getHeader("Message-ID");
                    String messId = "";
                    if (zaglavlje != null && zaglavlje.length > 0) {
                        messId = zaglavlje[0];
                    }
                    Poruka p = new Poruka(messId, message.getSentDate(), sender, subject, message.getContentType(),
                            message.getSize(), message.getFlags(), true, true, message.getContent().toString());

                    poruke.add(p);
                }
            }

            // Close the folder
            
            folder.close(true);

            // Close the message store
            store.close();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (FolderClosedException e) {
            e.printStackTrace();
        } catch (FolderNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (ReadOnlyFolderException e) {
            e.printStackTrace();
        } catch (StoreClosedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
