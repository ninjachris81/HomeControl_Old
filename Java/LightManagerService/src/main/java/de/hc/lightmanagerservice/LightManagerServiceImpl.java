/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.lightmanagerservice;

import de.hc.commons.log.LoggerService;
import de.hc.lightmanagerservice.api.LightId;
import de.hc.lightmanagerservice.api.LightManagerService;
import de.hc.lightmanagerservice.lights.LightEnum;
import de.hc.sensormanagerservice.api.SensorManagerService;
import de.hc.sensormanagerservice.sensors.Sensors;
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
@Provides(specifications = {LightManagerService.class})
@Instantiate
public class LightManagerServiceImpl implements LightManagerService {

    @Requires
    LoggerService loggerService;
    
    @Requires
    SensorManagerService sms;

    @Validate
    private void start() {
        loggerService.debug("Started solar control service");
        
        sms.registerSensor(Sensors.LIGHTS[LightEnum.LIGHT_STAIRS.ordinal()], SensorManagerService.ConnectionType.SERIAL, "COM5");
    }

    @Invalidate
    protected void stop() {
        loggerService.info("STOPPING SOLAR SERVICE");
    }

    public void setLightState(LightId light, LIGHT_STATE state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LIGHT_STATE getLightState(LightId light) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
