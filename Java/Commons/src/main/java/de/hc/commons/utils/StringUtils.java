/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.utils;

/**
 *
 * @author B1
 */
public class StringUtils {
    
    public static boolean isNullOrEmpty(String str) {
        if (str==null) return true;
        return str.isEmpty();
    }
    
}
