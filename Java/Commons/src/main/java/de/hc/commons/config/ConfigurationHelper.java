/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.commons.config;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 *
 * @author B1
 */
public class ConfigurationHelper {

    public static void createDefaultConfiguration(String pid, DefaultConfigurationHandler handler, BundleContext bundleContext) {

        ServiceReference configurationAdminReference = bundleContext.getServiceReference(ConfigurationAdmin.class.getName());
        ConfigurationAdmin confAdmin = (ConfigurationAdmin) bundleContext.getService(configurationAdminReference);
        
        try {
            final Configuration conf = confAdmin.getConfiguration(pid);
            if (conf.getProperties()==null) {
                Dictionary dict = new Hashtable();
                handler.getDefaultConfiguration(dict);
                conf.update(dict);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
}
