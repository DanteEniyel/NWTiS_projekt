/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.web.kontrole;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jelvalcic.web.slusaci.KontekstAplikacije;

/**
 *
 * @author jelvalcic
 * Klasa Kontroler za upravljanje navigacijom kroz aplikaciju
 */
public class Kontroler extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String zahtjev = request.getServletPath();
        String odrediste = null;
        switch (zahtjev) {
            case "/Kontroler":
                odrediste = "/jsp/index.jsp";
                break;
            case "/PrijavaKorisnika":
                odrediste = "/jsp/login.jsp";
                break;
            case "/OdjavaKorisnika":
                odrediste = "/Kontroler";
                break;
            case "/ProvjeraKorisnika":
                String kor_ime = request.getParameter("kor_ime");
                String lozinka = request.getParameter("lozinka");
                Korisnik korisnik;
                ProvjeraKorisnika provjera = new ProvjeraKorisnika();

                if (kor_ime == null || kor_ime.trim().length() == 0 || lozinka == null || lozinka.trim().length() == 0) {
                    throw new NeuspjesnaPrijava("Neispravni podaci!");
                }
                korisnik = provjera.provjeraKorisnika(KontekstAplikacije.getKontekst(), kor_ime, lozinka);
                if (korisnik == null) {
                    //ukoliko se korisnik nije uspješno prijavio dobiva obavijest o neuspješnoj prijavi
                    throw new NeuspjesnaPrijava("Neuspjesna prijava!");
                }

                HttpSession sesija = request.getSession();
                String ip_adresa = request.getRemoteAddr();
                //ukoliko je korisnik uspješno prošao prijavu bilježe se podaci u sesiju
                korisnik.setSes_ID(sesija.getId());
                korisnik.setIp_adresa(ip_adresa);

                sesija.setAttribute("korisnik", korisnik);
                if (korisnik.getVrsta() == 0){
                    //ukoliko je korisnik administrator bilježi se u sesiju
                    sesija.setAttribute("admin", korisnik.getVrsta());
                }
                

                odrediste = "/Kontroler";

                break;

            case "/IspisAktivnihZipKodova":
                odrediste = "/privatno/ispisAktivnihZipKodova.jsp";
                break;

            case "/IspisMeteoPodataka":
                odrediste = "/privatno/ispisMeteoPodataka.jsp";
                
                break;

            case "/PregledDnevnika":
                odrediste = "/admin/pregledDnevnika.jsp";
                break;
                
            case "/PregledDnevnikaKorisnickihZahtjeva":
                odrediste = "/admin/ispisDnevnikaKorisnickihZahtjeva.jsp";
                break;
        }
        if (odrediste == null) {
            throw new ServletException("Zahtjev nije poznat");
        }

        RequestDispatcher rd = this.getServletContext().getRequestDispatcher(odrediste);
        rd.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
