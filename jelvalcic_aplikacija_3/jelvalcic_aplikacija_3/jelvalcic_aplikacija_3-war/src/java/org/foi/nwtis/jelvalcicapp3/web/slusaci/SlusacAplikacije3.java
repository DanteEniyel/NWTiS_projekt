/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp3.web.slusaci;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.jelvalcicapp3.kontrole.KorisnickaKonfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author jelvalcic
 * Slušač aplikacije 3
 */
@WebListener()
public class SlusacAplikacije3 implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        javax.servlet.ServletContext context = sce.getServletContext();
        String path = context.getRealPath("WEB-INF") + java.io.File.separator;
        String datoteka = path + sce.getServletContext().getInitParameter("konfiguracija");
        KontekstAplikacije.setKontekst(sce.getServletContext()); 
        System.out.println(datoteka);

        KorisnickaKonfiguracija bpKonf = new KorisnickaKonfiguracija(datoteka);
        //BP_Konfiguracija bpKonfig = new BP_Konfiguracija(datoteka);
        
        sce.getServletContext().setAttribute("BP_Konfiguracija", bpKonf);
        //sce.getServletContext().setAttribute("BP_Konfig", bpKonfig);

        System.out.println("Konfiguracija je ucitana.");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
