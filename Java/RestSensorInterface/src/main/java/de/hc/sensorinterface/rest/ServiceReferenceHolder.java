/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensorinterface.rest;

import de.hc.sensormanagerservice.api.SensorManagerService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

/**
 *
 * @author B1
 */
@Component
@Instantiate
public class ServiceReferenceHolder {
    
    @Requires
    private SensorManagerService sensorManagerService;

    public SensorManagerService getSensorManagerService() {
        return sensorManagerService;
    }
    
    @Validate
    private void start() {
        SensorInputResource.srh = this;
        SensorOutputResource.srh = this;
    }
    
    @Invalidate
    protected void stop() {
        SensorInputResource.srh = null;
        SensorOutputResource.srh = null;
    }


}
