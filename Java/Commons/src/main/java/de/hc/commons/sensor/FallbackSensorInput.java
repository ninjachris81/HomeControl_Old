/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.sensor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author B1
 */
public class FallbackSensorInput<T extends Comparable> extends SensorInput<T> {
    
    public static final long NO_FALLBACK = -1;
    
    private final T fallbackValue;
    private long fallbackTimeoutMs = NO_FALLBACK;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture;
    
    private boolean fallbackEnabled = true;
    
    FallbackSensorInput(String name, T defaultValue, long fallbackTimeoutMs, int notificationInterval) {
        super(name, defaultValue, notificationInterval);
        this.fallbackValue = defaultValue;
        this.fallbackTimeoutMs = fallbackTimeoutMs;
        this.fallbackEnabled = fallbackTimeoutMs!=NO_FALLBACK;
    }

    public boolean isFallbackEnabled() {
        return fallbackEnabled;
    }

    public void setFallbackEnabled(boolean fallbackEnabled) {
        this.fallbackEnabled = fallbackEnabled;
    }
    
    @Override
    public void setValue(T value) {
        super.setValue(value);
        
        if (fallbackEnabled) {
            if (scheduledFuture!=null) scheduledFuture.cancel(true);
            scheduledFuture = executorService.schedule(new Runnable() {
                public void run() {
                    System.out.println("Setting fallback value");
                    setValue(fallbackValue);
                }
            }, fallbackTimeoutMs, TimeUnit.MILLISECONDS);
        }
        
        
    }
    
    
    
}
