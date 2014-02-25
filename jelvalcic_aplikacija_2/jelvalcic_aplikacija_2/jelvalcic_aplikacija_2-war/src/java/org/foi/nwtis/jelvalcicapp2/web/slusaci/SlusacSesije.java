/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.slusaci;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.Korisnik;

/**
 * Web application lifecycle listener.
 *
 * @author jelvalcic
 * Slušač aplikacije 2
 */
@WebListener()
public class SlusacSesije implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if(event.getName().compareTo("korisnik") != 0){
            return;
        }
        
        Korisnik korisnik = (Korisnik) event.getSession().getAttribute("korisnik");
        List<Korisnik> popisKorisnika = (List<Korisnik>) event.getSession().getServletContext().getAttribute("aktivniKorisnici");
        if(popisKorisnika == null){
            popisKorisnika = new ArrayList();
        }
        popisKorisnika.add(korisnik);
        event.getSession().getServletContext().setAttribute("aktivniKorisnici", popisKorisnika);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        
    }
}
