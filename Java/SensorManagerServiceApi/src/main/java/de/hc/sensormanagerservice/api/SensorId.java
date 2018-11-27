/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensormanagerservice.api;

import de.hc.commons.Identifier;

/**
 *
 * @author B1
 * @param <SENSOR_ENUM>
 */
public abstract class SensorId<SENSOR_ENUM extends Enum> extends Identifier<SENSOR_ENUM> {

    public SensorId(SENSOR_ENUM id) {
        super(id);
    }
    
    public abstract String getPrefix();
    
    @Override
    public String toString() {
        return getPrefix() + "_" + id.name();
    }

    
}
