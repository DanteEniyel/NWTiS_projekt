/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.kontrole;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jelvalcic
 * Kasa navigacije nakon logina
 */
@Named(value = "navigacija")
@SessionScoped
public class Navigacija implements Serializable {

    private static final long serialVersionUID = 1520318172495977648L;

    public Navigacija() {
    }
 
    
    /**
     * Redirekcija na login
     * @return ime login stranice
     */
    public String redirectToLogin() {
        return "/javno/login.xhtml?faces-redirect=true";
    }
     
    /**
     * Postavljanje putanje za login stranicu
     * @return ime login stranice
     */
    public String toLogin() {
        return "/javno/login.xhtml";
    }
     
    /**
     * Redirekcija na početnu stranicu
     * @return ime početne stranice
     */
    public String redirectToInfo() {
        return "/javno/index.xhtml?faces-redirect=true";
    }
     
    /**
     * Postavljanje putanje za početnu stranicu
     * @return ime početne stranice
     */
    public String toInfo() {
        return "/javno/index.xhtml";
    }
     
    /**
     * Redirekcija na korisničke stranice(posebno administrator, posebno običan korisnik)
     * @return ime korisničke stranice
     */
    public String redirectToWelcome(boolean admin) {
        //return "/secured/welcome.xhtml?faces-redirect=true";
        if(admin){
            return "/admin/administrator.xhtml?faces-redirect=true";
        }else{
            return "/privatno/pregledPortfolia.xhtml?faces-redirect=true";
        }
        
    }
     
    /**
     * Postavljanje putanje za korisničke stranice
     * @return ime korisničke stranice
     */
    public String toWelcome(boolean admin) {
        if(admin){
            return "/admin/administrator.xhtml";
        }else{
            return "/privatno/pregledPortfolia.xhtml";
        }
        
    }
}
