/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.connection;

/**
 *
 * @author B1
 */
public interface Connection {
    
    public String getConnectionType();
    
    public boolean isConnected();
    
    public boolean connect(final ConnectionDetails details);
    
    public boolean disconnect();
    
}
