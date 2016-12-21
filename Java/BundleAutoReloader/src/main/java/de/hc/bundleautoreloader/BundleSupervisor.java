/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.bundleautoreloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 *
 * @author B1
 */
public class BundleSupervisor {

    private static final int CHECK_INTERVAL_MS = 1000;

    private FileAlterationMonitor fam;
    private Properties timestampsProperties;
    private File timestampsFile;

    private static final String propertiesFileName = "lastTimestamps.properties";

    public BundleSupervisor() {
        timestampsProperties = new Properties();
        timestampsFile = new File(propertiesFileName);

        if (timestampsFile.exists()) {
            try {
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(timestampsFile));
                timestampsProperties.load(stream);
                stream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void startWatching(BundleContext context, Bundle ownBundle) {
        fam = new FileAlterationMonitor(CHECK_INTERVAL_MS);

        for (final Bundle bundle : context.getBundles()) {
            if (bundle.getSymbolicName().startsWith("de.hc") && !bundle.getSymbolicName().equals(ownBundle.getSymbolicName())) {
                try {
                    String bundlePath = sanitizePath(bundle.getLocation());
                    Path dir = FileSystems.getDefault().getPath(bundlePath).getParent();
                    FileAlterationObserver fao = new FileAlterationObserver(dir.toAbsolutePath().toString());
                    fao.addListener(new FileAlterationListenerAdaptor() {
                        @Override
                        public void onFileChange(File file) {
                            if (file.getName().endsWith(".jar")) {
                                try {
                                    System.out.println("Updating bundle " + file.getAbsolutePath());
                                    bundle.update();
                                } catch (BundleException ex) {
                                    Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });
                    
                    long lastModified = new File(bundlePath).lastModified();
                    if (Long.parseLong(timestampsProperties.getProperty(bundlePath, "0"))==lastModified) {
                        System.out.println("Bundle " + bundle.getSymbolicName() + " not changed");
                    } else {
                        System.out.println("Updating bundle " + bundle.getSymbolicName());
                        timestampsProperties.setProperty(bundlePath, "" + lastModified);
                        bundle.update();
                    }
                    
                    fam.addObserver(fao);
                } catch (BundleException ex) {
                    Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            timestampsProperties.store(new FileOutputStream(timestampsFile), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            fam.start();
        } catch (Exception ex) {
            Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopWatching() {
        try {
            fam.stop();
        } catch (Exception ex) {
            Logger.getLogger(BundleSupervisor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String sanitizePath(String felixPath) {
        if (felixPath.startsWith("file:/")) {
            felixPath = felixPath.substring(6);
        }
        return felixPath.replaceAll("\\\\", "/");
    }

}
