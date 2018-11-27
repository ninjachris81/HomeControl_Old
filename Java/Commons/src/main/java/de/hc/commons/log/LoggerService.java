/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.log;

import java.util.logging.Level;

/**
 *
 * @author B1
 */
public interface LoggerService {
    
    public void debug(String msg);

    public void info(String msg);
    
    public void warning(String msg);

    public void severe(String msg);

    public void log(Level level, String msg, Throwable thrown);

    public void log(Level level, String msg);
    
    public void configError(String... keys);

}
