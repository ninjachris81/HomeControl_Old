/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.pumpcontrolservice.api;

/**
 *
 * @author B1
 * @param <PUMP_ENUM>
 */
public interface PumpControlService<PUMP_ENUM extends Enum> {
    
    public enum PUMP_STATE {
        PUMP_ON,
        PUMP_OFF
    }
    
    public void setPumpState(PumpId<PUMP_ENUM> pump, PUMP_STATE state);
    
}
