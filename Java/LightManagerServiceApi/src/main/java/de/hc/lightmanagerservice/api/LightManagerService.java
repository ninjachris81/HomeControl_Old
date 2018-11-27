/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.lightmanagerservice.api;

/**
 *
 * @author B1
 */
public interface LightManagerService {
    
    public enum LIGHT_STATE {
        STATE_ON,
        STATE_OFF
    }
            
    
    public void setLightState(LightId light, LIGHT_STATE state);
    
    public LIGHT_STATE getLightState(LightId light);

}
