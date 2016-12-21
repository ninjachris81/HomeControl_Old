package de.hc.commons;

import de.hc.commons.log.impl.LoggerServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {
    
    public void start(BundleContext context) throws Exception {
        ServiceReference ref = context.getServiceReference(LogService.class.getName());
        if (ref != null) {
            LoggerServiceImpl.logService = (LogService) context.getService(ref);
            LoggerServiceImpl.logService.log(LogService.LOG_DEBUG, "CommonLogger set");
        }
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
    }

}
