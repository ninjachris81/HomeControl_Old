/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensormanagerservice;

import de.hc.commons.log.LoggerService;
import de.hc.commons.protocol.ascii.SimpleAsciiProtocol;
import de.hc.commons.sensor.SensorManager;
import de.hc.sensormanagerservice.api.SensorId;
import de.hc.sensormanagerservice.api.SensorManagerService;
import de.hc.serialcomm.SerialConnection;
import de.hc.serialcomm.SerialConnectionDetails;
import java.util.HashMap;
import java.util.Map;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 *
 * @author B1
 */
@Component
@Provides(specifications = {SensorManagerService.class})
@Instantiate
public class SensorManagerServiceImpl implements SensorManagerService, SimpleAsciiProtocol.DatagramProcessor<SimpleAsciiProtocol.Datagram> {
    
    @Requires
    LoggerService loggerService;

    private final SensorManager sensorManager;
    private final Map<String, SensorId> sensorIds = new HashMap<String, SensorId>();
    private final Map<Integer, SensorId> sensorMappings = new HashMap<Integer, SensorId>();
    private int currentIdIndex = 0;
    private final Map<String, SerialConnection> serialConnections = new HashMap<String, SerialConnection>();
    

    public SensorManagerServiceImpl() {
        this.sensorManager = new SensorManager();
        
        
    }

    public void setValue(SensorId sensor, Comparable value) {
        sensorManager.getSensorInput(sensor.toString()).setValue(value);
    }

    public boolean sensorExists(SensorId sensor) {
        return sensorManager.getSensorInput(sensor.toString())!=null;
    }

    public Comparable getValue(SensorId sensor) {
        return sensorManager.getSensorInput(sensor.toString()).getValue();
    }

    public SensorId sensorExists(String sensorName) {
        return sensorIds.get(sensorName);
    }

    public boolean registerSensor(SensorId sId, ConnectionType type, String connectionString) {
        switch(type) {
            case SERIAL:
                createSerialConnection(connectionString);
                break;
            default:
                return false;
        }
        
        sensorIds.put(sId.toString(), sId);
        sensorMappings.put(currentIdIndex, sId);
        currentIdIndex++;

        return true;
    }
    
    private void createSerialConnection(String connectionString) {
        if (serialConnections.containsKey(connectionString)) {
            // already existing
        } else {
            SimpleAsciiProtocol proto = new SimpleAsciiProtocol(this);
            SerialConnection conn = new SerialConnection();
            SerialConnectionDetails details = new SerialConnectionDetails(connectionString, currentIdIndex, true, true, proto);
            
            if (conn.connect(details)) {
                serialConnections.put(connectionString, conn);
            } else {
                
            }            
        }
    }

    public void onNewDatagram(SimpleAsciiProtocol.Datagram datagram) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
