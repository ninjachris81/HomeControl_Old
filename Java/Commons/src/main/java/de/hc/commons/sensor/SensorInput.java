package de.hc.commons.sensor;

import de.hc.commons.sensor.SensorManager.ValueChangeNotificationType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author B1
 * @param <T>
 */
public class SensorInput<T extends Comparable> {
    
    public static final int NO_NOTIFICATION = -1;
    
    private String name;
    private T value;
    private boolean isDirty = false;
    
    private ValueChangeNotificationType notificationType = ValueChangeNotificationType.IMMEDIATE;
    private int notificationInterval = NO_NOTIFICATION;
    
    private ISensorInputValueChangeListener valueChangeListener;
    
    SensorInput(String name, T defaultValue, int notificationInterval) {
        this.name = name;
        this.value = defaultValue;
        this.notificationInterval = notificationInterval;
        if (notificationInterval==NO_NOTIFICATION) {
            this.notificationType = ValueChangeNotificationType.IMMEDIATE;
        } else {
            this.notificationType = ValueChangeNotificationType.CUSTOM_INTERVAL;
        }
    }

    public ValueChangeNotificationType getNotificationType() {
        return notificationType;
    }

    public int getNotificationInterval() {
        return notificationInterval;
    }

    public String getName() {
        return name;
    }
    
    public void addValueChangeListener(ISensorInputValueChangeListener cl) {
        this.valueChangeListener = cl;
    }
    
    public void setValue(T value) {
        if (this.value.getClass().equals(value.getClass())) {
            if (!this.value.equals(value)) {
                // was updated
                this.value = value;
                isDirty = true;
                if (valueChangeListener!=null) valueChangeListener.onValueChanged(name, value);
            } else {
                // same value
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public T getValue() {
        return value;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void clearDirty() {
        this.isDirty = false;
    }
    
}
