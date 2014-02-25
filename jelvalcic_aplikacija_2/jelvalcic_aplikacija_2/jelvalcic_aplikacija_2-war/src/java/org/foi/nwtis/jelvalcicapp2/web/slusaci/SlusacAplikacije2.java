/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.slusaci;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.jelvalcic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.KorisnickaKonfiguracija;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.ObradaPoruke;

/**
 * Web application lifecycle listener.
 *
 * @author jelvalcic
 * Klasa slušača aplikacije
 */
@WebListener()
public class SlusacAplikacije2 implements ServletContextListener {
    //Dretva ObradaPoruke
    private ObradaPoruke obradaPoruka = null;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        javax.servlet.ServletContext context = sce.getServletContext();
        String path = context.getRealPath("WEB-INF") + java.io.File.separator;
        String datoteka = path + sce.getServletContext().getInitParameter("konfiguracija");
        KontekstAplikacije.setKontekst(sce.getServletContext()); 
        System.out.println(datoteka);

        KorisnickaKonfiguracija bpKonf = new KorisnickaKonfiguracija(datoteka);
        BP_Konfiguracija bpKonfig = new BP_Konfiguracija(datoteka);
        
        sce.getServletContext().setAttribute("BP_Konfiguracija", bpKonf);
        sce.getServletContext().setAttribute("BP_Konfig", bpKonfig);

        System.out.println("Konfiguracija je ucitana.");
        System.out.println(bpKonf.getBpKonfig().getDriver_database());

        obradaPoruka = new ObradaPoruke();
        obradaPoruka.setBpKonf(bpKonf);
        obradaPoruka.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (obradaPoruka != null || obradaPoruka.isAlive() || obradaPoruka.isDaemon()) {
            //prekida se rad dretve ukoliko je došlo do bilo kakvog prekida
            //osigurava se da ne ostane raditi negdje u pozadini
            obradaPoruka.interrupt();
        }
    }
}
