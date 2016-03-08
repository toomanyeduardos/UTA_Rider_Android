package com.eduardoflores.utarider.model.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Eduardo Flores
 */
@Root(strict=false)
public class MonitoredVehicleJourney
{
    @Element(name = "LineRef")
    public Integer lineRef;

    @Element(name = "DirectionRef", required = false)
    public String directionRef;

    @Element(name = "PublishedLineName")
    public String publishedLineName;

    @Element(name = "OriginRef")
    public String originRef;

    @Element(name = "DestinationRef")
    public String destinationRef;

    @Element(name = "VehicleLocation")
    public VehicleLocation vehicleLocation;

    @Element(name = "ProgressRate")
    public Integer progressRate;

    @Element(name = "CourseOfJourneyRef")
    public Integer courseOfJourneyRef;

    @Element(name = "VehicleRef")
    public Integer vehicleRef;

    @Element(name = "MonitoredCall", required = false)
    public MonitoredCall monitoredCall;

    @ElementList(name = "OnwardCalls", required = false)
    public List<OnwardCall> onwardCalls;

    @Element(name = "Extensions")
    public Extensions extensions;
}
