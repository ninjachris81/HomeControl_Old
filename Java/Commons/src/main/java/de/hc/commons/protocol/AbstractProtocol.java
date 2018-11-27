/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.protocol;

import java.io.InputStream;

/**
 *
 * @author B1
 * @param <DATAGRAM>
 */
public abstract class AbstractProtocol<DATAGRAM extends AbstractProtocol.IDatagram> {
    
    protected DatagramProcessor<DATAGRAM> datagramProcessor;
    
    public abstract void nextIncomingData(byte[] data);

    public abstract InputStream getInputStream();
    
    public abstract int writeData(byte[] data);

    public interface DatagramProcessor<DATAGRAM extends IDatagram> {
        void onNewDatagram(DATAGRAM datagram);
    }
    
    public interface IDatagram {
    }

    public AbstractProtocol(DatagramProcessor<DATAGRAM> datagramProcessor) {
        this.datagramProcessor = datagramProcessor;
    }

}
