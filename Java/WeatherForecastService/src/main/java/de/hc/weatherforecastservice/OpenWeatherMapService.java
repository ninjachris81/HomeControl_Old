/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.weatherforecastservice;

import de.hc.commons.config.ConfigurationHelper;
import de.hc.commons.config.DefaultConfigurationHandler;
import de.hc.commons.log.LoggerService;
import de.hc.commons.property.Property;
import de.hc.weatherforecastservice.api.WeatherForecastService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.annotations.Validate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

/**
 *
 * @author B1
 */
@Component(managedservice = OpenWeatherMapService.PID)
@Provides
@Instantiate
public class OpenWeatherMapService implements WeatherForecastService {

    public static final String PID = "de.hc.weatherforecastservice.OpenWeatherMapService";

    //private static final int FORECAST_COUNT = 3;
    //private static final int INTERVAL_SEC = 60 * 10;
    
    private List<Property<Integer>> cloudinesses;
    private List<Property<Double>> temperatures;

    private ScheduledExecutorService executorService;

    @Requires
    LoggerService logService;

    @org.apache.felix.ipojo.annotations.Property(name = "apiKey", value="", mandatory = true)
    private String key_apiKey;

    @org.apache.felix.ipojo.annotations.Property(name = "url", value="", mandatory = true)
    private String key_url;

    @org.apache.felix.ipojo.annotations.Property(name = "forecastCount")
    private int key_forecastCount;

    @org.apache.felix.ipojo.annotations.Property(name = "intervalSec")
    private int key_intervalSec;

    public OpenWeatherMapService(BundleContext bundleContext) {
        if (key_forecastCount>0) {
            cloudinesses = new ArrayList<Property<Integer>>();
            for (int i = 0; i < key_forecastCount; i++) {
                cloudinesses.add(new Property((int) -1));
            }

            temperatures = new ArrayList<Property<Double>>();
            for (int i = 0; i < key_forecastCount; i++) {
                temperatures.add(new Property((double) -1));
            }
        } else {
            logService.warning("Invalid forecast count !");
        }

        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Validate
    public void start() {
        logService.info("Started weather map service");
        
        if (key_intervalSec>0) {
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    refreshData();
                }
            }, 0, key_intervalSec, TimeUnit.SECONDS);
        } else {
            logService.warning("Invalid interval !");
        }
    }

    @Invalidate
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public Property<Integer> getCloudiness(int day) {
        return cloudinesses.get(day);
    }

    @Override
    public Property<Double> getTemperature(int day) {
        return temperatures.get(day);
    }

    private void refreshData() {
        if (key_apiKey!=null && !key_apiKey.isEmpty()) {
            try {
                logService.info("Refreshing weather data");

                InputStream is = new URL(key_url.replace("$API_KEY", key_apiKey)).openStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                JSONArray entries = json.getJSONArray("list");
                for (int i = 0; i < entries.length(); i++) {
                    if (i >= key_forecastCount) {
                        break;
                    }

                    JSONObject obj = entries.getJSONObject(i);

                    int cloudsAll = obj.getJSONObject("clouds").getInt("all");
                    cloudinesses.get(i).setValue(cloudsAll);

                    double temp = obj.getJSONObject("main").getDouble("temp");
                    temperatures.get(i).setValue(temp);
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(OpenWeatherMapService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OpenWeatherMapService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            logService.warning("API_KEY not configured");
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Updated
    public void updated() throws ConfigurationException {
        logService.debug("Updated " + PID + " configuration");
    }

}
