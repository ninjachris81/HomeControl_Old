/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.solarcontrolservice;

import de.hc.commons.log.LoggerService;
import de.hc.commons.property.IPropertyValueChangeListener;
import de.hc.commons.property.Property;
import de.hc.commons.sensor.FallbackSensorInput;
import de.hc.solarcontrolservice.api.SolarControlService;
import de.hc.weatherforecastservice.api.WeatherForecastService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

/**
 *
 * @author B1
 */
@Component
@Provides(specifications = {SolarControlService.class})
@Instantiate
public class SolarControlServiceImpl implements SolarControlService {

    @Requires
    WeatherForecastService wfs;

    @Requires
    LoggerService loggerService;
    
    private FallbackSensorInput<Integer> brightnessSensor;

    @Validate
    private void start() {
        loggerService.debug("Started solar control service");

        wfs.getTemperature(WeatherForecastService.TOMORROW).addValueChangeListener(new IPropertyValueChangeListener<Double>() {
            public void valueChanged(Double value) {
                loggerService.info("Temperature changed: " + value);
            }
        }, true);
    }

    @Invalidate
    protected void stop() {
        loggerService.info("STOPPING SOLAR SERVICE");
    }

    /*
    protected void bind(WeatherForecastService wfc){
        System.out.println("BIND");
    }
    
    protected void unbind(WeatherForecastService wfc){
        System.out.println("UNBIND");
    }*/
}
