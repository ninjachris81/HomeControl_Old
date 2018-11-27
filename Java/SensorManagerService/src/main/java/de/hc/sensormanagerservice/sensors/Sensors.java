/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensormanagerservice.sensors;

import de.hc.lightmanagerservice.api.LightId;
import de.hc.lightmanagerservice.lights.LightEnum;
import de.hc.pumpcontrolservice.api.PumpId;
import de.hc.pumpcontrolservice.pumps.PumpEnum;
import de.hc.sensormanagerservice.api.SensorId;

/**
 *
 * @author B1
 */
public class Sensors {
    
    public static final SensorId[] LIGHTS = new SensorId[]{new LightId(LightEnum.LIGHT_STAIRS)};
    
    public static final SensorId[] PUMPS = new PumpId[] {new PumpId(PumpEnum.PUMP_HC), new PumpId(PumpEnum.PUMP_SOLAR)};
    
}
