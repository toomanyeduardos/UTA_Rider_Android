package com.eduardoflores.utarider.fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.eduardoflores.utarider.AnalyticsTrackers;
import com.eduardoflores.utarider.R;
import com.eduardoflores.utarider.activity.MainActivity;
import com.eduardoflores.utarider.model.localfiles.Route;
import com.eduardoflores.utarider.model.localfiles.Stop;
import com.eduardoflores.utarider.model.service.MonitoredVehicleJourney;
import com.eduardoflores.utarider.model.service.OnwardCall;
import com.eduardoflores.utarider.model.service.Vehicle;
import com.eduardoflores.utarider.service.UTAService;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Eduardo Flores
 */
public class Map extends Fragment
{
    private GoogleMap googleMap;

    private MainActivity activity;

    private long timerDelay = 1000 * 60;  // milliseconds

    private Callback<Vehicle> vehicleDataCallback = new Callback<Vehicle>() {
        @Override
        public void success(Vehicle vehicle, Response response) {
            setUpMap();
            for (MonitoredVehicleJourney monitoredVehicleJourney : vehicle.monitoredVehicleJourney)
            {
                addMarkersForVehicles(monitoredVehicleJourney);

                if (monitoredVehicleJourney.onwardCalls != null &&
                        monitoredVehicleJourney.onwardCalls.get(0) != null)
                {
                    for (OnwardCall onwardCall : monitoredVehicleJourney.onwardCalls)
                    {
                        for(final com.eduardoflores.utarider.model.localfiles.Stop stop : activity.arrayOfStops)
                        {
                            if (onwardCall.stopPointRef.equals(stop.stopId))
                            {
                                addMarkersForStop(stop);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("MY_APP", error.getLocalizedMessage());
        }
    };

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            getDataForMap();
            timerHandler.postDelayed(this, timerDelay);
        }
    };

    private void addMarkersForStop(Stop stop)
    {
        MarkerOptions markerForStops = new MarkerOptions()
                .position(new LatLng(stop.stopLatitude, stop.stopLongitude))
                .title(stop.stopName);
        markerForStops.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_train_station));
        googleMap.addMarker(markerForStops);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoView = activity.getLayoutInflater().inflate(R.layout.map_info_viewer, null);
                TextView mapStopName = (TextView) infoView.findViewById(R.id.map_marker_stop_name);

                mapStopName.setText(marker.getTitle());

                return infoView;
            }
        });
    }

    private void addMarkersForVehicles(MonitoredVehicleJourney monitoredVehicleJourney)
    {
        switch (monitoredVehicleJourney.lineRef)
        {
            case 701:
            case 703:
            case 704:
            case 720:
                MarkerOptions markerTrax = new MarkerOptions()
                        .position(new LatLng(monitoredVehicleJourney.vehicleLocation.latitude,
                                monitoredVehicleJourney.vehicleLocation.longitude))
                        .title(monitoredVehicleJourney.publishedLineName);
                markerTrax.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_train_red));
                googleMap.addMarker(markerTrax);
                break;
            case 750:
                MarkerOptions markerFrontunner = new MarkerOptions()
                        .position(new LatLng(monitoredVehicleJourney.vehicleLocation.latitude,
                                monitoredVehicleJourney.vehicleLocation.longitude))
                        .title(monitoredVehicleJourney.publishedLineName);
                markerFrontunner.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_frontrunner_north));
                googleMap.addMarker(markerFrontunner);
                break;
            default:
                MarkerOptions markerBus = new MarkerOptions()
                        .position(new LatLng(monitoredVehicleJourney.vehicleLocation.latitude,
                                monitoredVehicleJourney.vehicleLocation.longitude))
                        .title(monitoredVehicleJourney.publishedLineName);
                markerBus.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_train_blue));
                googleMap.addMarker(markerBus);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, null, false);
        googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                .getMap();

        if (googleMap != null)
        {
            setUpMap();
        }
        return view;
    }


    /*
    This determines if the fragment is active. If I just use the onCreateView, the viewpager
    calls the map before routes have been added to the array of selected routes
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && isAdded())
        {
            getDataForMap();
            timerHandler.postDelayed(timerRunnable, 0);

            // send analytics reporting fragment is visible
            Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
            tracker.setScreenName("MapFragment");
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
        else
        {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (MainActivity) getActivity();
        if (googleMap != null && activity != null)
        {
            setUpMap();
            getDataForMap();
        }
    }

    private void getDataForMap()
    {
        // download data
        UTAService utaService = new UTAService();
        for (Route route : activity.listOfRoutesSelected)
        {
            utaService.getVehicleData(route.routeShortName, vehicleDataCallback);
        }
    }

    public void setUpMap()
    {
        googleMap.clear();

        googleMap.setMyLocationEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();

                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 12.0f), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish()
                    {
                        // stop listening for location change
                        googleMap.setOnMyLocationChangeListener(null);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }
}
