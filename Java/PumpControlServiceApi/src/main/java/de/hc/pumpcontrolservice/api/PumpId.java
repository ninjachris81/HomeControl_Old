/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.pumpcontrolservice.api;

import de.hc.sensormanagerservice.api.SensorId;

/**
 *
 * @author B1
 * @param <PUMP_ENUM>
 */
public class PumpId<PUMP_ENUM extends Enum> extends SensorId<PUMP_ENUM> {

    public PumpId(PUMP_ENUM id) {
        super(id);
    }

    @Override
    public String getPrefix() {
        return "PUMP";
    }
    
}
