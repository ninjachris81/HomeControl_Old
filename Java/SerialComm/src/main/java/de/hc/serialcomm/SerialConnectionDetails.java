/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.serialcomm;

import de.hc.commons.connection.ConnectionDetails;
import de.hc.commons.protocol.AbstractProtocol;

/**
 *
 * @author B1
 */
public class SerialConnectionDetails implements ConnectionDetails {
    
    private final String portName;
    private final int baudRate;
    private final boolean canWrite;
    private final boolean canRead;
    private final AbstractProtocol protocol;

    public SerialConnectionDetails(String portName, int baudRate, boolean canWrite, boolean canRead, AbstractProtocol protocol) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.canWrite = canWrite;
        this.canRead = canRead;
        this.protocol = protocol;
    }

    public String getPortName() {
        return portName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public AbstractProtocol getProtocol() {
        return protocol;
    }   
    
}
