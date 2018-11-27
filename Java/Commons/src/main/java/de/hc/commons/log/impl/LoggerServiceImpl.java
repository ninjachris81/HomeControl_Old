/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.log.impl;

import de.hc.commons.log.LoggerService;
import java.util.logging.Level;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.osgi.service.log.LogService;

/**
 *
 * @author B1
 */
@Component
@Provides(specifications = {LoggerService.class})
@Instantiate
public class LoggerServiceImpl implements LoggerService {
    
    private final boolean printOutput = true;
    
    public static LogService logService;

    public LoggerServiceImpl() {
    }
    
    private int getLogServiceLevel(Level level) {
        if(level==Level.INFO) {
            return LogService.LOG_INFO;
        } else if (level==Level.WARNING) {
            return LogService.LOG_WARNING;
        } else if (level==Level.SEVERE) {
            return LogService.LOG_ERROR;
        } else {
            return LogService.LOG_DEBUG;
        }
    }

    @Override
    public void debug(String msg) {
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(LogService.LOG_DEBUG, msg);
    }

    @Override
    public void info(String msg) {
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(LogService.LOG_INFO, msg);
    }

    @Override
    public void warning(String msg) {
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(LogService.LOG_WARNING, msg);
    }

    @Override
    public void severe(String msg) {
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(LogService.LOG_ERROR, msg);
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(getLogServiceLevel(level), msg, thrown);
    }

    @Override
    public void log(Level level, String msg) {
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(getLogServiceLevel(level), msg);
    }

    public void configError(String... keys) {
        String msg = "Missing config keys: ";
        for (String key : keys) msg+=key + " ";
        msg = msg.trim();
        if (printOutput) System.out.println(msg);
        if (logService!=null) logService.log(LogService.LOG_WARNING, msg);
    }

}
