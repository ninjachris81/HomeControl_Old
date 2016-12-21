/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hc.sensorinterface.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author B1
 */
@Path("output")
public class SensorOutputResource {

    public static ServiceReferenceHolder srh;

    @GET
    @Path("/{sensorName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getValue(@PathParam("sensorName") String sensorName) {
        if (srh.getSensorManagerService().sensorExists(sensorName)) {
            srh.getSensorManagerService().getValue(sensorName);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}