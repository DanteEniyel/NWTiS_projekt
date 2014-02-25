/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.kontrole;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jelvalcic
 * Klasa koja upravlja prijavim korisnika
 */
@Named(value = "kontrolaPrijave")
@SessionScoped
public class KontrolaPrijave implements Serializable {

    private static final long serialVersionUID = 7765876811740798583L;
   
    private Korisnik korisnik;
    private Pomocna helper = new Pomocna();
    private String username;
    private String password;
    private boolean loggedIn;
    private boolean admin;

     /*   
     @ManagedProperty(value="#{navigacija}")
     private Navigacija navigationBean;
     */
    private Navigacija navigationBean = new Navigacija();

    public KontrolaPrijave() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Navigacija getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(Navigacija navigationBean) {
        this.navigationBean = navigationBean;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    
    /**
     * Login operation.
     *
     * @return vraća se redirekcija
     */
    public String doLogin() {

       korisnik = helper.provjeriKorisnika(username, password);
       admin = false; 
       HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            // Uspješan login
            if (korisnik != null) {
                loggedIn = true;
                admin = korisnik.getVrsta() == 0;
                sesija.setAttribute("korisnik", korisnik);
                return navigationBean.redirectToWelcome(admin);
            }
        

        // Ispis errora za login
        FacesMessage msg = new FacesMessage("Login error!", "ERROR MSG");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);

        // Ukoliko je došlo do greške vraća se na login
        return navigationBean.toLogin();
    }

    /**
     * Logout 
     *
     * @return vraća se redirekcija
     */
    public String doLogout() {

        // Postavljanje zastavice za login korisnika na false
        loggedIn = false;

        // Postavljanje poruke za odjavu
        FacesMessage msg = new FacesMessage("Logout success!", "INFO MSG");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return navigationBean.toLogin();
    }
}
