package com.eduardoflores.utarider.service;

import com.eduardoflores.utarider.BuildConfig;
import com.eduardoflores.utarider.model.service.Stop;
import com.eduardoflores.utarider.model.service.Vehicle;
import com.eduardoflores.utarider.utils.AppUtils;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;

/**
 * @author Eduardo Flores
 */
public class UTAService {

    private final UTAServices utaServices;

    public UTAService()
    {
        // setup the okhttpclient with a read timeout of 2 minutes to handle the order simulate
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(2, TimeUnit.MINUTES);

        // setting up the log level
        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;
        if (BuildConfig.DEBUG) {
            logLevel = RestAdapter.LogLevel.FULL;
        }

        // create the rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(logLevel)
                .setEndpoint(BuildConfig.SERVICE_DOMAIN)
                .setClient(new OkClient(client))
                .setConverter(new SimpleXMLConverter())
                .build();

        // create the order service
        utaServices = restAdapter.create(UTAServices.class);
    }

    public void getVehicleData(String routeNumber, Callback<Vehicle> callback)
    {
        utaServices.getVehicleData(true, AppUtils.UTA_TOKEN, routeNumber, callback);
    }

    public void getStopData(String stopId, Callback<Stop> callback)
    {
        utaServices.getStopData(true, AppUtils.UTA_TOKEN, 30, stopId, callback);
    }

}