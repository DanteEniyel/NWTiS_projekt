/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.slusaci;

import javax.servlet.ServletContext;

/**
 *
 * @author jelvalcic
 * Klasa objekta kontekst aplikacije kojom se prosljeđuje kontekst u klase kojima
 * je potreban kontekst aplikacije
 */
public class KontekstAplikacije {
    public static ServletContext kontekst = null;

    public static ServletContext getKontekst() {
        return kontekst;
    }

    public static void setKontekst(ServletContext kontekst) {
        KontekstAplikacije.kontekst = kontekst;
    }
}
