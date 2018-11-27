/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons;

/**
 *
 * @author B1
 */
public abstract class Identifier<ENUM_TYPE extends Enum> {
    
    protected final ENUM_TYPE id;

    public Identifier(ENUM_TYPE id) {
        this.id = id;
    }
    
    @Override
    public abstract String toString();
    
}
