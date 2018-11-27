/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.lightmanagerservice.api;

import de.hc.sensormanagerservice.api.SensorId;

/**
 *
 * @author B1
 * @param <LIGHT_ENUM>
 */
public class LightId<LIGHT_ENUM extends Enum> extends SensorId<LIGHT_ENUM> {
    
    public LightId(LIGHT_ENUM id) {
        super(id);
    }

    @Override
    public String getPrefix() {
        return "LIGHT";
    }
    
}
