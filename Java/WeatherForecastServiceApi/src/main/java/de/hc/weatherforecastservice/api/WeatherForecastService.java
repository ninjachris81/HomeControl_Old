/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.weatherforecastservice.api;

import de.hc.commons.property.Property;

/**
 *
 * @author B1
 */
public interface WeatherForecastService {
    
    public Property<Integer> getCloudiness(int day);
    public Property<Double> getTemperature(int day);

    
}
