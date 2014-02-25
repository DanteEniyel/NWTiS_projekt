/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcicapp2.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.jelvalcic.servisi.MeteoPodaci;
import org.foi.nwtis.jelvalcic.servisi.ZipKodoviRangLista;
import org.foi.nwtis.jelvalcicapp2.web.klijenti.MeteoKlijent;

/**
 *
 * @author jelvalcic
 * Klasa kojom se vraćaju podaci dohvaćeni putem web servisa i aplikacije 1
 */
@Named(value = "vratiPodatke")
@SessionScoped
public class VratiPodatke implements Serializable {

    @EJB
    private MeteoKlijent meteoKlijent;
    //aktivni zip kodovi
    private List<ZipKodoviRangLista> aktivniZipKodovi;
    //meteo podaci za aktivne zip kodove
    private List<MeteoPodaci> meteoPodaciZaAktivneZipKodove;

    /**
     * Creates a new instance of VratiPodatke
     */
    public VratiPodatke() {
    }

//---------------------------AKTIVNI ZIP KODOVI----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za Aktivni zip kodovi">
    public List<ZipKodoviRangLista> getAktivniZipKodovi() {
        aktivniZipKodovi = meteoKlijent.dajAktivneZipKodove();
        return aktivniZipKodovi;
    }

    public void setAktivniZipKodovi(List<ZipKodoviRangLista> aktivniZipKodovi) {
        this.aktivniZipKodovi = aktivniZipKodovi;
    }
    //</editor-fold>

//---------------------METEO PODACI ZA AKTIVNE ZIP KODOVE------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za Meteo podaci za aktivne zip kodove">
    public List<MeteoPodaci> getMeteoPodaciZaAktivneZipKodove() {
        List<ZipKodoviRangLista> popisAktivniZipKodovi; 
        popisAktivniZipKodovi = meteoKlijent.dajAktivneZipKodove();
        
        if(popisAktivniZipKodovi == null || popisAktivniZipKodovi.isEmpty()){
            return null;
        }
        
        meteoPodaciZaAktivneZipKodove = null;
        meteoPodaciZaAktivneZipKodove = new ArrayList<>();
        
        for(ZipKodoviRangLista zk : popisAktivniZipKodovi){
            meteoPodaciZaAktivneZipKodove.add(meteoKlijent.dajMeteoPodatkeZaZipKod(zk.getZipKod()));
        }
        
        return meteoPodaciZaAktivneZipKodove;
    }

    public void setMeteoPodaciZaAktivneZipKodove(List<MeteoPodaci> meteoPodaciZaAktivneZipKodove) {
        this.meteoPodaciZaAktivneZipKodove = meteoPodaciZaAktivneZipKodove;
    }
    //</editor-fold>
//--------------------------- ----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za ">
    //</editor-fold>
//--------------------------- ----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za ">
    //</editor-fold>
//--------------------------- ----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za ">
    //</editor-fold>
//--------------------------- ----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za ">
    //</editor-fold>
//--------------------------- ----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za ">
    //</editor-fold>
//--------------------------- ----------------------------------
    // <editor-fold defaultstate="collapsed" desc="Get i set metode za ">
    //</editor-fold>
    
//---------------------------AKTIVNI ZIP KODOVI----------------------------------    
    
    
//---------------------------METEO PODACI ZA AKTIVNE ZIP KODOVE----------------------------------

}
