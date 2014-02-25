/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.web.filteri;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jelvalcic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jelvalcic.web.kontrole.Korisnik;
import org.foi.nwtis.jelvalcic.web.slusaci.KontekstAplikacije;

/**
 *
 * @author jelvalcic Klasa filtera za kontrolu pristupa običnog korisnika
 */
public class KontrolaPristupa implements Filter {

    private static final boolean debug = true;
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    private double pocetnoVrijeme;

    public KontrolaPristupa() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        pocetnoVrijeme = System.currentTimeMillis();

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

        HttpServletRequest zahtjev = (HttpServletRequest) request;
        if (request instanceof HttpServletRequest) {
            String url = zahtjev.getRequestURL().toString();
            String korIme = "";
            HttpSession session = zahtjev.getSession();
            Korisnik korisnik = new Korisnik();
            if (session.getAttribute("korisnik") == null) {
                korIme = "Anoniman";
            } else {
                korisnik = (Korisnik) session.getAttribute("korisnik");
                korIme = korisnik.getIme();
            }

            upisiUBazu(url, korIme);
        }
    }

    private void upisiUBazu(String url, String korisnik) {
        ServletContext context = KontekstAplikacije.getKontekst();
        String path = context.getRealPath("WEB-INF") + java.io.File.separator;
        String datoteka = path + context.getInitParameter("konfiguracija");
        BP_Konfiguracija bp = new BP_Konfiguracija(datoteka);


        String connUrl = bp.getServer_database() + bp.getUser_database();
        java.sql.Connection conn = null;
        java.sql.Statement stmt = null;

        String sql = "INSERT INTO dnevnik_korisnici (Korisnik, URL, DatumVrijemeAktivnosti, VrijemeObrade) VALUES ('" + korisnik + "','" + url + "', NOW(), " + (System.currentTimeMillis() - pocetnoVrijeme) / 1000 + ");";
        try {
            Class.forName(bp.getDriver_database());
        } catch (ClassNotFoundException ex) {
            System.out.println("Greska kod ucitavanja drivera " + ex.getMessage());
        }
        try {
            conn = DriverManager.getConnection(connUrl, bp.getUser_username(), bp.getUser_password());
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Greška u radu s bazom:" + ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpSession sesija = ((HttpServletRequest) request).getSession();
            Object korisnik = sesija.getAttribute("korisnik");
            //provjerava se postoji li korisnik u sesiji
            if (korisnik == null || !(korisnik instanceof Korisnik)) {
                //ako ne postoji prosljeđuje se na obrazac za prijavu
                RequestDispatcher rd = request.getRequestDispatcher("/PrijavaKorisnika");
                rd.forward(request, response);
                return;
            }
        }

        doBeforeProcessing(request, response);

        Throwable problem = null;
        try {

            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("KontrolaPristupa:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("KontrolaPristupa()");
        }
        StringBuffer sb = new StringBuffer("KontrolaPristupa(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
