/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensorinterface.rest;

import de.hc.sensormanagerservice.api.SensorId;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author B1
 */
@Path("input")
public class SensorInputResource {
    
    public static ServiceReferenceHolder srh;

    @POST
    @Path("/{sensorName}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response setValue(@PathParam("sensorName") String sensorName, String sensorValue) {
        final SensorId sId = srh.getSensorManagerService().sensorExists(sensorName);
        if (sId!=null) {
            srh.getSensorManagerService().setValue(sId, sensorValue);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
