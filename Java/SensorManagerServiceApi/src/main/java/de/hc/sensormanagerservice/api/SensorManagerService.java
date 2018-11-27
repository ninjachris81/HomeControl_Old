/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensormanagerservice.api;

/**
 *
 * @author B1
 */

public interface SensorManagerService {
    
    public enum ConnectionType {
        SERIAL
    }
    
    public boolean sensorExists(SensorId sensor);
    
    public SensorId sensorExists(String sensorName);

    public void setValue(SensorId sensor, Comparable value);
    
    public Comparable getValue(SensorId sensor);
    
    public boolean registerSensor(SensorId sId, ConnectionType type, String connectionPort);
    
}
