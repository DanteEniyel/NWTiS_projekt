/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.klijenti;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.xml.ws.WebServiceRef;
import org.foi.nwtis.jelvalcic.servisi.JelvalcicAplikacija1WS_Service;
import org.foi.nwtis.jelvalcic.servisi.MeteoPodaci;
import org.foi.nwtis.jelvalcic.servisi.ZipKodoviRangLista;

/**
 *
 * @author jelvalcic
 * Klasa MeteoKlijent kojim se implementiraju operacije vlastitog web servisa
 */
@Stateless
@LocalBean
public class MeteoKlijent {
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/jelvalcic_aplikacija_1/JelvalcicAplikacija1WS.wsdl")
    private JelvalcicAplikacija1WS_Service service;

/*
 * Dohvaćanje aktivnih zip kodova
 */
    public List<ZipKodoviRangLista> dajAktivneZipKodove(){
        return vratiAktivneZipKodove();
    }
    
    private java.util.List<org.foi.nwtis.jelvalcic.servisi.ZipKodoviRangLista> vratiAktivneZipKodove() {
        org.foi.nwtis.jelvalcic.servisi.JelvalcicAplikacija1WS port = service.getJelvalcicAplikacija1WSPort();
        return port.vratiAktivneZipKodove();
    }

/**
 * Dohvaćanje meteo podataka za željeni zip kod
 * @param zipKod String - zip kod za koji se traže meteo podaci
 * @return MeteoPodaci - vraćaju se posljednji dohvaćeni meteo podaci za traženi zip kod
 */
    public MeteoPodaci dajMeteoPodatkeZaZipKod(String zipKod){
        return vratiMeteoPodatkeZaZip(zipKod);
    }
    
    private MeteoPodaci vratiMeteoPodatkeZaZip(java.lang.String zipKod) {
        org.foi.nwtis.jelvalcic.servisi.JelvalcicAplikacija1WS port = service.getJelvalcicAplikacija1WSPort();
        return port.vratiMeteoPodatkeZaZip(zipKod);
    }

    private java.util.List<org.foi.nwtis.jelvalcic.servisi.MeteoPodaci> vratiPopisMeteoPodatakaVremenskiInterval(java.lang.String zipKod, java.lang.String odDatum, java.lang.String doDatum) {
        org.foi.nwtis.jelvalcic.servisi.JelvalcicAplikacija1WS port = service.getJelvalcicAplikacija1WSPort();
        return port.vratiPopisMeteoPodatakaVremenskiInterval(zipKod, odDatum, doDatum);
    }

    private java.util.List<org.foi.nwtis.jelvalcic.servisi.MeteoPodaci> vratiPopisMeteoPodatakaZaZip(java.lang.String zipKod, int brojPodataka) {
        org.foi.nwtis.jelvalcic.servisi.JelvalcicAplikacija1WS port = service.getJelvalcicAplikacija1WSPort();
        return port.vratiPopisMeteoPodatakaZaZip(zipKod, brojPodataka);
    }

    private java.util.List<org.foi.nwtis.jelvalcic.servisi.ZipKodoviRangLista> vratiRangListuZipKodova(int brojZipKodova) {
        org.foi.nwtis.jelvalcic.servisi.JelvalcicAplikacija1WS port = service.getJelvalcicAplikacija1WSPort();
        return port.vratiRangListuZipKodova(brojZipKodova);
    }

    

}
