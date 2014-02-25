/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jelvalcic.klijenti;

import net.wxbug.api.LiveWeatherData;
import net.wxbug.api.UnitType;

/**
 *
 * @author jelvalcic
 * Klasa WeatherBugKlijent kojom se na temelju korisničkog koda i zahtjevanog zip koda
 * dohvaćaju meteo podaci servisa WeatherBug
 */
public class WeatherBugKlijent {

    private String zip;
    private LiveWeatherData meteoPodatak;
    
    public LiveWeatherData dajMeteoPodatke(String zip){
        
        return getLiveWeatherByUSZipCode(zip, UnitType.METRIC, "A5565497605");
        
    }

    private static LiveWeatherData getLiveWeatherByUSZipCode(java.lang.String zipCode, net.wxbug.api.UnitType unittype, java.lang.String aCode) {
        net.wxbug.api.WeatherBugWebServices service = new net.wxbug.api.WeatherBugWebServices();
        net.wxbug.api.WeatherBugWebServicesSoap port = service.getWeatherBugWebServicesSoap12();
        return port.getLiveWeatherByUSZipCode(zipCode, unittype, aCode);
    }
    
    
    
}
