package de.hc.commons.sensor;

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
public interface ISensorInputValueChangeListener<T extends Comparable> {

    void onValueChanged(String name, T value);
    
}
