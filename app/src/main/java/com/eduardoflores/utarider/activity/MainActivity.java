package com.eduardoflores.utarider.activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import com.eduardoflores.utarider.AnalyticsTrackers;
import com.eduardoflores.utarider.R;
import com.eduardoflores.utarider.fragment.PagerAdapter;
import com.eduardoflores.utarider.listener.OnRouteSelectedListener;
import com.eduardoflores.utarider.model.localfiles.Route;
import com.eduardoflores.utarider.model.service.MonitoredVehicleJourney;
import com.eduardoflores.utarider.model.service.OnwardCall;
import com.eduardoflores.utarider.model.service.Stop;
import com.eduardoflores.utarider.model.service.Vehicle;
import com.eduardoflores.utarider.service.UTAService;
import com.eduardoflores.utarider.utils.AppUtils;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    public Set<String> setOfFavoriteRoutes;

    private ViewPager viewPager;

    public SharedPreferences sharedPreferences;

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
        setOfFavoriteRoutes = new HashSet<String>();

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

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String routeJson = gson.toJson(route);

        ImageButton imageButton = (ImageButton)view;
        if (listOfFavoriteRoutes.contains(route))
        {
            route.isFavorite = false;
            listOfFavoriteRoutes.remove(route);
            imageButton.setImageResource(R.drawable.ic_star_empty);
        }
        else
        {
            route.isFavorite = true;
            listOfFavoriteRoutes.add(route);
            setOfFavoriteRoutes.add(routeJson);
            imageButton.setImageResource(R.drawable.ic_star_selected);
            editor.putStringSet(SHARED_PREFERENCES_ROUTES, setOfFavoriteRoutes);
        }
        editor.commit();
        adapter.notifyDataSetChanged();
    }
}
