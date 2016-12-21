package de.hc.commons.sensor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author B1
 */
public class SensorManager implements ISensorInputValueChangeListener<Comparable> {

    private final Map<String, SensorInput> sensorInputs = new HashMap<String, SensorInput>();
    private final Map<String, ScheduledExecutorService> sensorExecutors = new HashMap<String, ScheduledExecutorService>();
    
    private final Map<ISensorInputValueChangeListener, String> valueChangeListeners = new HashMap<ISensorInputValueChangeListener, String>();
    private final ExecutorService callbackExecutor = Executors.newCachedThreadPool();

    public enum ValueChangeNotificationType {
        IMMEDIATE,
        CUSTOM_INTERVAL
    }
    
    public void shutdown() {
        for (String name : sensorExecutors.keySet()) {
            ScheduledExecutorService ses = sensorExecutors.get(name);
            ses.shutdown();
        }
        
        callbackExecutor.shutdown();
    }
    
    private <T extends Comparable> SensorInput createSensorInput(String name, T defaultValue, long fallbackTimeoutMs, int notificationInterval) {
        if (fallbackTimeoutMs>0) {
            return new FallbackSensorInput(name, defaultValue, fallbackTimeoutMs, notificationInterval);
        } else {
            return new SensorInput(name, defaultValue, notificationInterval);
        }
    }
    
    private SensorInput setupSensorInput(SensorInput sensorInput) {
        sensorInputs.put(sensorInput.getName(), sensorInput);
        sensorInput.addValueChangeListener(this);
        return sensorInput;
    }
    
    public <T extends Comparable> FallbackSensorInput registerFallbackSensorInput(String name, T defaultValue, long fallbackTimeoutMs) {
        final FallbackSensorInput sensorInput = (FallbackSensorInput) createSensorInput(name, defaultValue, fallbackTimeoutMs, SensorInput.NO_NOTIFICATION);
        return (FallbackSensorInput) setupSensorInput(sensorInput);
    }

    public <T extends Comparable> SensorInput registerSensorInput(String name, T defaultValue) {
        final SensorInput sensorInput = createSensorInput(name, defaultValue, FallbackSensorInput.NO_FALLBACK, SensorInput.NO_NOTIFICATION);
        return setupSensorInput(sensorInput);
    }
    
    public <T extends Comparable> SensorInput registerSensorInput(final String name, final T defaultValue, final int notificationInterval) {
        SensorInput sensorInput = createSensorInput(name, defaultValue, FallbackSensorInput.NO_FALLBACK, notificationInterval);
        sensorInput = setupSensorInput(sensorInput);

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (sensorInputs.get(name).isDirty()) {
                    sendValueChanged(name);
                    sensorInputs.get(name).clearDirty();
                }
            }
        }, 0, notificationInterval, TimeUnit.MILLISECONDS);
        sensorExecutors.put(name, ses);

        return sensorInput;
    }

    public void registerValueChangeListener(ISensorInputValueChangeListener valueChangeListener) {
        valueChangeListeners.put(valueChangeListener, null);
    }

    public void registerValueChangeListener(ISensorInputValueChangeListener valueChangeListener, String nameFilter) {
        valueChangeListeners.put(valueChangeListener, nameFilter);
    }
    
    public SensorInput getSensorInput(String name) {
        return sensorInputs.get(name);
    }

    @Override
    public void onValueChanged(String name, Comparable value) {
        switch (sensorInputs.get(name).getNotificationType()) {
            case IMMEDIATE:
                sendValueChanged(name);
                break;
            case CUSTOM_INTERVAL:
                // will get emitted automatically
                break;
        }
    }
    
    private void sendValueChanged(final String name) {
        for (final ISensorInputValueChangeListener vcl : valueChangeListeners.keySet()) {
            final String nameFilter = valueChangeListeners.get(vcl);
            
            if (nameFilter==null || name.matches(nameFilter)) {
                callbackExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        vcl.onValueChanged(name, sensorInputs.get(name).getValue());
                    }
                });
            }
        }
    }

}
