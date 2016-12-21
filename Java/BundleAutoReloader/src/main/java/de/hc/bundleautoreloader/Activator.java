package de.hc.bundleautoreloader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private final BundleSupervisor bs = new BundleSupervisor();

    public void start(BundleContext context) throws Exception {
        bs.startWatching(context, context.getBundle());
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
        bs.stopWatching();
    }
}
