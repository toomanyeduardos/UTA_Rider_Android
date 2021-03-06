package com.eduardoflores.utarider.model.service;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Eduardo Flores
 */
@Root(name = "Siri", strict=false)
public class Stop
{
    @Path("StopMonitoringDelivery/MonitoredStopVisit")
    @ElementList(entry = "MonitoredVehicleJourney", inline = true)
    public List<MonitoredVehicleJourney> monitoredVehicleJourney;

    public Stop(){}
}
