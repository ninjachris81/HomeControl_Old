/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.pumpcontrolservice;

import de.hc.pumpcontrolservice.api.PumpControlService;
import de.hc.pumpcontrolservice.api.PumpId;
import de.hc.sensormanagerservice.api.SensorManagerService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 *
 * @author B1
 */
@Component
@Provides(specifications = PumpControlService.class)
@Instantiate
public class PumpControlServiceImpl<PumpEnum> implements PumpControlService {
    
    @Requires
    SensorManagerService sms;

    public void setPumpState(PumpId pump, PUMP_STATE state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
