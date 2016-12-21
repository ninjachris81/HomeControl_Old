/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensormanagerservice;

import de.hc.commons.sensor.SensorManager;
import de.hc.sensormanagerservice.api.SensorManagerService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

/**
 *
 * @author B1
 */
@Component
@Provides(specifications = {SensorManagerService.class})
@Instantiate
public class SensorManagerServiceImpl implements SensorManagerService {
    
    private final SensorManager sensorManager;

    public SensorManagerServiceImpl() {
        this.sensorManager = new SensorManager();
    }

    public void setValue(String sensorName, Comparable value) {
        sensorManager.getSensorInput(sensorName).setValue(value);
    }

    public boolean sensorExists(String sensorName) {
        return sensorManager.getSensorInput(sensorName)!=null;
    }

    public Comparable getValue(String sensorName) {
        return sensorManager.getSensorInput(sensorName).getValue();
    }
    
}
