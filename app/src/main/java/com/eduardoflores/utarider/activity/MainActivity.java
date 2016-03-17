package com.eduardoflores.utarider.activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.eduardoflores.utarider.AnalyticsTrackers;
import com.eduardoflores.utarider.R;
import com.eduardoflores.utarider.fragment.PagerAdapter;
import com.eduardoflores.utarider.listener.OnRouteSelectedListener;
import com.eduardoflores.utarider.model.localfiles.Route;
import com.eduardoflores.utarider.utils.AppUtils;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnRouteSelectedListener{

    public String SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES";

    public String SHARED_PREFERENCES_ROUTES = "ROUTES";

    public List<com.eduardoflores.utarider.model.localfiles.Stop> arrayOfStops;

    public List<Route> arrayOfRoutes;

    public List<Route> listTraxRoutes;

    public List<Route> listFrontrunnerRoutes;

    public List<Route> listBusRoutes;

    public List<List<Route>> listOfRoutesSorted;

    public List<Route> listOfRoutesSelected;

    public List<Route> listOfFavoriteRoutes;

    public Set<String> setFavoriteRoutes;

    public SharedPreferences sharedPreferences;

    private ViewPager viewPager;

    private SharedPreferences.Editor editor;

    private static final String[] INITIAL_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int LOCATION_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        arrayOfStops = AppUtils.getListOfStops(this);
        arrayOfRoutes = AppUtils.getListOfRoutes(this);

        sortRoutes();

        listOfRoutesSelected = new ArrayList<>();
        listOfFavoriteRoutes = new ArrayList<>();

        // Tabs and viewpager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.tab_favorites)));
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.tab_routes)));
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.tab_map)));
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.tab_twitter)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // viewPager.notify();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        // read favorite routes
        editor = sharedPreferences.edit();
        setFavoriteRoutes = new HashSet<String>(sharedPreferences.
                getStringSet(SHARED_PREFERENCES_ROUTES, new HashSet<String>()));
        for (String favoriteRouteId : setFavoriteRoutes) {
            onRouteFavorited(AppUtils.getRouteFromRouteId(this, favoriteRouteId), null, null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if ( !isLocationAllowed())
            {
                requestPermissions(INITIAL_PERMS, LOCATION_REQUEST);
            }
        }

        // send analytics that app opened
        AnalyticsTrackers.initialize(this);
        Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case LOCATION_REQUEST:
                if ( !isLocationAllowed())
                {
                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.permissions_denied_dialog_title)
                            .setMessage(R.string.permissions_denied_dialog_message);
                    builder.setNegativeButton(R.string.permissions_denied_dialog_close_app,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.setPositiveButton(R.string.permissions_denied_dialog_try_again,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(INITIAL_PERMS, LOCATION_REQUEST);
                                }
                            });
                    builder.show();

                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getOrder()) {
            case 100:   // About dialog
                displayAboutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isLocationAllowed()
    {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private void displayAboutDialog() {
        String version = null;

        try {
            version = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String message = "App created by Eduardo Flores in South Jordan, UT.\n" +
                "For additional information please contact me at toomanyeduardos@gmail.com.\n\n" +
                "App version: " + version;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("About UTA Rider");

        alertDialogBuilder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sortRoutes()
    {
        listTraxRoutes = new ArrayList<>();
        listFrontrunnerRoutes = new ArrayList<>();
        listBusRoutes = new ArrayList<>();

        for (Route route : arrayOfRoutes)
        {
            switch (route.routeType)
            {
                case 0:
                    listTraxRoutes.add(route);
                    break;
                case 1:
                    break;
                case 2:
                    listFrontrunnerRoutes.add(route);
                    break;
                case 3:
                    listBusRoutes.add(route);
                    break;
            }
        }

        listOfRoutesSorted = new ArrayList<>();
        listOfRoutesSorted.add(listTraxRoutes);
        listOfRoutesSorted.add(listFrontrunnerRoutes);
        listOfRoutesSorted.add(listBusRoutes);
    }

    @Override
    public void onRouteSelected(Route route) {
        if (route.isSelected)
        {
            listOfRoutesSelected.add(route);
        }
        else
        {
            listOfRoutesSelected.remove(route);
        }
    }

    @Override
    public void onRouteFavorited(Route route, View view, ArrayAdapter<Route> adapter)
    {

        ImageButton imageButton = (ImageButton)view;
        if (listOfFavoriteRoutes.contains(route))
        {
            route.isFavorite = false;
            listOfFavoriteRoutes.remove(route);
            if (imageButton != null) {
                imageButton.setImageResource(R.drawable.ic_star_empty);
            }
        }
        else
        {
            route.isFavorite = true;
            listOfFavoriteRoutes.add(route);
            setFavoriteRoutes.add(route.routeId);
            if (imageButton != null) {
                imageButton.setImageResource(R.drawable.ic_star_selected);
            }
        }
        //editor.commit();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        editor.putStringSet(SHARED_PREFERENCES_ROUTES, new HashSet<String>(setFavoriteRoutes)).commit();
    }
}
