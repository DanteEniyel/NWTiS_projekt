/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.servisi;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author jelvacic
 * Klasa vlastitog web servisa koja definira operacije web servisa
 * Implementira operacije za dohvaćanje meteo podataka i aktivnih zip kodova za 
 * koje se prikupljaju meteo podaci
 */
@WebService(serviceName = "JelvalcicAplikacija1WS")
public class JelvalcicAplikacija1WS {

    /**
     * Web service operation
     * Vraćaju se zip kodovi za koje se prikupljaju meteo podaci
     */
    @WebMethod(operationName = "vratiAktivneZipKodove")
    public List<ZipKodoviRangLista> vratiAktivneZipKodove() {
        JelvalcicWSAPI azk = new JelvalcicWSAPI();
                
        return azk.getPopisAktivnihZipKodova();
    }

    /**
     * Web service operation
     * Vraća se rang lista n zip kodova za koje se prikupilo najviše meteo podataka
     */
    @WebMethod(operationName = "vratiRangListuZipKodova")
    public List<ZipKodoviRangLista> vratiRangListuZipKodova(@WebParam(name = "brojZipKodova") int brojZipKodova) {
        JelvalcicWSAPI rlzk = new JelvalcicWSAPI();
        
        return rlzk.getRangListuZipKodova(brojZipKodova);
    }

    /**
     * Web service operation
     * Vraća se n posljednih meteo podataka za željeni zip kod
     */
    @WebMethod(operationName = "vratiPopisMeteoPodatakaZaZip")
    public List<MeteoPodaci> vratiPopisMeteoPodatakaZaZip(@WebParam(name = "zipKod") String zipKod, @WebParam(name = "brojPodataka") int brojPodataka) {
        JelvalcicWSAPI pmp = new JelvalcicWSAPI();
        
        return pmp.getPopisMeteoPodatakaZaZipKod(zipKod, brojPodataka);
    }

    /**
     * Web service operation
     * Vraćaju se meteo podaci za željeni zip kod unutar definiranog vremenskog intervala
     */
    @WebMethod(operationName = "vratiPopisMeteoPodatakaVremenskiInterval")
    public List<MeteoPodaci> vratiPopisMeteoPodatakaVremenskiInterval(@WebParam(name = "zipKod") String zipKod, @WebParam(name = "odDatum") String odDatum, @WebParam(name = "doDatum") String doDatum) {
        JelvalcicWSAPI pmpvi = new JelvalcicWSAPI();
        
        return pmpvi.getMeteoPodatkeZaZipKodVremenskiInterval(zipKod, doDatum, doDatum);
    }

    /**
     * Web service operation
     * Vraćaju se meteo podaci za šeljeni zip kod
     */
    @WebMethod(operationName = "vratiMeteoPodatkeZaZip")
    public MeteoPodaci vratiMeteoPodatkeZaZip(@WebParam(name = "zipKod") String zipKod) {
        JelvalcicWSAPI mpl = new JelvalcicWSAPI();
        
        return mpl.getMeteoPodaciLista(zipKod);
    }

    
}
