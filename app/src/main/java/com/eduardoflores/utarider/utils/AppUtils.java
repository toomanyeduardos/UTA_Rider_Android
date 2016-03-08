package com.eduardoflores.utarider.utils;

import com.eduardoflores.utarider.model.localfiles.Route;
import com.eduardoflores.utarider.model.localfiles.Stop;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Eduardo Flores
 */
public class AppUtils
{
    public static String UTA_TOKEN = "UMM4NBIBJUJ";

    public static String TWITTER_CONSUMER_KEY = "JjppSoLTt7CDDWGegs8SWqSo6";
    public static String TWITTER_CONSUMER_SECRET = "KogEjVNm2sztW3V3DQuMJYWRSp3Sh6PtBAvfsv6aBGOYn50c2s";
    public static String TWITTER_ACCESS_TOKEN = "78984518-uzF78AaAdehdyDnR5f2MbyvcjVM5CO7GbFeT276xA";
    public static String TWITTER_ACCESS_TOKEN_SECRET = "ECEDOK8db7NmnnfprVI2WnmNYbpLDXZ5qdVljyeHrOwi3";

    private static AssetManager assetManager;

    public static ArrayList<Route> getListOfRoutes(Context context)
    {
        assetManager = context.getAssets();
        ArrayList<Route> arrayOfRoutes = new ArrayList<Route>();
        InputStream stream = null;

        try
        {
            stream = assetManager.open("routes.txt");

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));

            try {
                String line;
                int counter = 0;
                while ((line = reader.readLine()) != null) {
                    if (counter > 0)
                    {
                        Route route = new Route();

                        String[] routeAsArray = line.split(",");
                        route.routeId = routeAsArray[0];
                        route.routeShortName = routeAsArray[2];
                        route.routeLongName = routeAsArray[3];
                        route.routeType = Integer.valueOf(routeAsArray[5]);

                        arrayOfRoutes.add(route);
                    }
                    counter++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return arrayOfRoutes;
    }// end getListOfRoutes

    public static ArrayList<Stop> getListOfStops(Context context)
    {
        assetManager = context.getAssets();
        ArrayList<Stop> arrayOfStops = new ArrayList<Stop>();
        InputStream stream = null;

        try
        {
            stream = assetManager.open("stops.txt");

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));

            try {
                String line;
                int counter = 0;
                while ((line = reader.readLine()) != null)
                {
                    if (counter > 0)
                    {
                        Stop stop = new Stop();

                        String[] stopAsArray = line.split(",");
                        stop.stopId = stopAsArray[0];
                        stop.stopCode = stopAsArray[1];
                        stop.stopName = stopAsArray[2];
                        stop.stopDescription = stopAsArray[3];
                        stop.stopLatitude = Double.valueOf(stopAsArray[4]);
                        stop.stopLongitude = Double.valueOf(stopAsArray[5]);

                        arrayOfStops.add(stop);
                    }
                    counter++;
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return arrayOfStops;
    }//end getListOfStops
}
