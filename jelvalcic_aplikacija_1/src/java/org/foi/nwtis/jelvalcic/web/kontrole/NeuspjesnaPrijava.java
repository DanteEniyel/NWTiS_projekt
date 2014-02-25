/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.web.kontrole;


import javax.servlet.ServletException;

/**
 *
 * @author jelvalcic
 * Klasa iznimke za neuspješnu prijavu korisnika
 */
public class NeuspjesnaPrijava extends ServletException{

    public NeuspjesnaPrijava(String message) {
        super("Opis greske: " + message);
    }
    
}
