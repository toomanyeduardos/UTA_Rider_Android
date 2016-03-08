package com.eduardoflores.utarider.model.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Eduardo Flores
 */
@Root(strict = false)
public class Extensions {

    @Element(name = "LastGPSFix", required = false)
    public String lastGPSFix;

    @Element(name = "Scheduled", required = false)
    public boolean scheduled;

    @Element(name = "Bearing", required = false)
    public double bearing;

    @Element(name = "Speed", required = false)
    public double speed;

    @Element(name = "DestinationName", required = false)
    public String destinationName;

}
