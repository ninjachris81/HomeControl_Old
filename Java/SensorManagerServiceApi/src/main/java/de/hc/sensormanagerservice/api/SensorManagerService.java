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
    
    public boolean sensorExists(String sensorName);
    
    public void setValue(String sensorName, Comparable value);
    
    public Comparable getValue(String sensorName);
    
}
