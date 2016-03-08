package com.eduardoflores.utarider.service;

import com.eduardoflores.utarider.model.service.Stop;
import com.eduardoflores.utarider.model.service.Vehicle;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Eduardo Flores
 */
public interface UTAServices
{
    @GET("/SIRI/SIRI.svc/VehicleMonitor/ByRoute")
    void getVehicleData(@Query("onwardcalls") boolean onwardsCalls,
            @Query("usertoken") String usertoken,
            @Query("route")String routeNumber,
            Callback<Vehicle> callback);

    @GET("/SIRI/SIRI.svc/StopMonitor")
    void getStopData(@Query("onwardcalls") boolean onwardsCalls,
            @Query("usertoken") String usertoken,
            @Query("minutesout") int minutesout,
            @Query("stopid")String stopid,
            Callback<Stop> callback);
}
