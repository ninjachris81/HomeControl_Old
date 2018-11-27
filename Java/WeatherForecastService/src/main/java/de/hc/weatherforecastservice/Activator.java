package de.hc.weatherforecastservice;

import de.hc.commons.config.ConfigurationHelper;
import de.hc.commons.config.DefaultConfigurationHandler;
import static de.hc.weatherforecastservice.OpenWeatherMapService.PID;
import java.util.Dictionary;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    
    public void start(BundleContext context) throws Exception {
        ConfigurationHelper.createDefaultConfiguration(PID, new DefaultConfigurationHandler() {
            public void getDefaultConfiguration(Dictionary dict) {
                dict.put(OpenWeatherMapService.KEY_API_KEY, "");
                dict.put(OpenWeatherMapService.KEY_URL, "");
                dict.put(OpenWeatherMapService.KEY_FORECAST_COUNT, "3");
                dict.put(OpenWeatherMapService.KEY_INTERVAL_SEC, "600");
            }
        }, context);
    }

    public void stop(BundleContext context) throws Exception {
    }

}
