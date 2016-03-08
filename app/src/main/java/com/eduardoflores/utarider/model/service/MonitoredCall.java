package com.eduardoflores.utarider.model.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Eduardo Flores
 */
@Root(strict=false)
public class MonitoredCall
{
    @Element(name = "StopPointRef")
    public String stopPointRef;

    @Element(name = "VisitNumber")
    public Integer visitNumber;

    @Element(name = "VehicleAtStop")
    public boolean vehicleAtStop;
}
