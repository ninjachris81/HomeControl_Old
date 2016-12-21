/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.property;

/**
 *
 * @author B1
 * @param <T>
 */
public interface IPropertyValueChangeListener<T extends Comparable> {
    
    public void valueChanged(T value);
    
}
