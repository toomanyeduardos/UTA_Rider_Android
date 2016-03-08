package com.eduardoflores.utarider.model.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Eduardo Flores
 */
@Root(strict=false)
public class VehicleLocation
{
    @Element(name = "Longitude")
    public double longitude;

    @Element(name = "Latitude")
    public double latitude;
}
