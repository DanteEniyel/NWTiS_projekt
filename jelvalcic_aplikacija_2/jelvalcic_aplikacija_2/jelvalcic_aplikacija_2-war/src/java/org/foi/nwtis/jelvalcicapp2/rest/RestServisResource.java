/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import org.foi.nwtis.jelvalcicapp2.web.kontrole.Korisnik;
import org.foi.nwtis.jelvalcicapp2.web.slusaci.KontekstAplikacije;

/**
 * REST Web Service
 *
 * @author jelvalcic
 */
@Path("restServis")
@RequestScoped
public class RestServisResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RestServisResource
     */
    public RestServisResource() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.jelvalcicapp2.rest.RestServisResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/html")
    public String getHtml() {
        ServletContext sce = KontekstAplikacije.getKontekst();
        List<Korisnik> popisKorisnika = (List<Korisnik>) sce.getAttribute("aktivniKorisnici");
        if (popisKorisnika == null) {
            popisKorisnika = new ArrayList();
        }

        String rez = "<table border = '1'>";
        for (Korisnik k : popisKorisnika) {
            rez = rez + "<tr><td>Korisnik: </td><td>" + k.getKorisnik() + "</td></tr>";
            rez = rez + "<tr><td>Tip korisnika: </td><td>" + Integer.toString(k.getVrsta()) + "</td></tr>";
        }


        rez = rez + "</table>";

        return rez;

    }

    /**
     * PUT method for updating or creating an instance of RestServisResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}
