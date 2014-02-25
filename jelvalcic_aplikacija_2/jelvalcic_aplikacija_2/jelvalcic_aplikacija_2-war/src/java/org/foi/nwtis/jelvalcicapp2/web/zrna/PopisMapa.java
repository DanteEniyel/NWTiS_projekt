/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.NoSuchProviderException;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;

/**
 *
 * @author jelvalcic
 * Klasa kojom se dohvaćaju mape korisnika
 * i vraća popis korisnika u klasu PregledSvihPoruka
 */
@ManagedBean
@SessionScoped
public class PopisMapa {
    private String email_posluzitelj = "localhost";
    private String korisnicko_ime = "servis@nwtis.nastava.foi.hr";
    private String korisnicka_lozinka = "123456";
    private List popisMapa = new ArrayList();
    
    /**
     * Creates a new instance of PopisMapa
     */
    public PopisMapa() {
        popuniListuMapa();
    }

    public String getEmail_posluzitelj() {
        return email_posluzitelj;
    }

    public void setEmail_posluzitelj(String email_posluzitelj) {
        this.email_posluzitelj = email_posluzitelj;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getKorisnicka_lozinka() {
        return korisnicka_lozinka;
    }

    public void setKorisnicka_lozinka(String korisnicka_lozinka) {
        this.korisnicka_lozinka = korisnicka_lozinka;
    }

    public List getPopisMapa() {
        return popisMapa;
    }

    public void setPopisMapa(List popisMapa) {
        this.popisMapa = popisMapa;
    }
    
    
/**
 * Metoda koja dohvaća mape korisnika i puni listu mapa
 */    
    private void popuniListuMapa(){
        Session session = null;
        Store store = null;
        Folder folder = null;
        Folder[] listaMapa;
        popisMapa = new ArrayList();
        
        try{
            session = Session.getDefaultInstance(System.getProperties(), null);
            store = session.getStore("imap");
            store.connect(email_posluzitelj, korisnicko_ime, korisnicka_lozinka);

            // Get a handle on the default folder
            //popisMapa.add(new SelectItem(String.valueOf("inbox"),"inbox"));
            listaMapa = store.getDefaultFolder().list();
                
            
            for(Folder f:listaMapa){
                popisMapa.add(new SelectItem(String.valueOf(f.getName()),f.getName()));
            }
            store.close();
            
            
        }catch (AuthenticationFailedException e) {
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
