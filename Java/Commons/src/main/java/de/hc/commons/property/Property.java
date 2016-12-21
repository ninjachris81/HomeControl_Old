/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.property;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author B1
 * @param <T>
 */
public class Property<T extends Comparable> {
    
    private Set<IPropertyValueChangeListener<T>> listeners;
    
    private String name;
    private T value;

    public Property(T defaultValue) {
        this.value = defaultValue;
    }

    public Property(T defaultValue, String name) {
        this(defaultValue);
        this.name = name;
    }
    
    public void addValueChangeListener(IPropertyValueChangeListener<T> l) {
        addValueChangeListener(l, false);
    }
    
    public void addValueChangeListener(IPropertyValueChangeListener<T> l, boolean fireOnce) {
        if (listeners==null) listeners = new HashSet<IPropertyValueChangeListener<T>>();
        listeners.add(l);
        if (fireOnce) {
            broadcastValueChanged();
        }
    }

    public T getValue() {
        return this.value;
    }
    
    public void setValue(T value) {
        if (this.value.equals(value)) return;
        
        this.value = value;
        broadcastValueChanged();
    }
    
    private void broadcastValueChanged() {
        if (listeners!=null) {
            for (IPropertyValueChangeListener cl : listeners) {
                cl.valueChanged(value);
            }
        }
    }
    
}
