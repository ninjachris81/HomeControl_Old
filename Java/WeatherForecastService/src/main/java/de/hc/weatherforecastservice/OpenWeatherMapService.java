/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.weatherforecastservice;

import de.hc.commons.log.LoggerService;
import de.hc.commons.property.Property;
import de.hc.commons.utils.StringUtils;
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
    
    public static final String KEY_API_KEY = "apiKey";
    public static final String KEY_URL = "url";
    public static final String KEY_FORECAST_COUNT = "forecastCount";
    public static final String KEY_INTERVAL_SEC = "intervalSec";

    private List<Property<Integer>> cloudinesses;
    private List<Property<Double>> temperatures;

    private final ScheduledExecutorService executorService;

    @Requires
    LoggerService logService;

    @org.apache.felix.ipojo.annotations.Property(name = KEY_API_KEY, value="", mandatory = true)
    private String key_apiKey;

    @org.apache.felix.ipojo.annotations.Property(name = KEY_URL, value="", mandatory = true)
    private String key_url;

    @org.apache.felix.ipojo.annotations.Property(name = KEY_FORECAST_COUNT)
    private int key_forecastCount;

    @org.apache.felix.ipojo.annotations.Property(name = KEY_INTERVAL_SEC)
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
            logService.configError(KEY_FORECAST_COUNT);
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
            logService.configError(KEY_INTERVAL_SEC);
        }
    }

    @Invalidate
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public Property<Integer> getCloudiness(int day) {
        if (day>=key_forecastCount) return null;
        return cloudinesses.get(day);
    }

    @Override
    public Property<Double> getTemperature(int day) {
        if (day>=key_forecastCount) return null;
        return temperatures.get(day);
    }

    private void refreshData() {
        if (!StringUtils.isNullOrEmpty(key_apiKey) && !StringUtils.isNullOrEmpty(key_url)) {
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
            logService.configError(KEY_API_KEY, KEY_URL);
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
