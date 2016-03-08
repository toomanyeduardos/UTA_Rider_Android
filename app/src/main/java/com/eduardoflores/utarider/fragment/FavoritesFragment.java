package com.eduardoflores.utarider.fragment;

import com.eduardoflores.utarider.activity.MainActivity;
import com.eduardoflores.utarider.adapter.RoutesAdapter;
import com.eduardoflores.utarider.model.localfiles.Route;
import com.eduardoflores.utarider.model.localfiles.Stop;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eduardo Flores
 */
public class FavoritesFragment extends ListFragment
{
    private MainActivity activity;

    List<Route> listOfFavoriteRoutes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        listOfFavoriteRoutes = activity.listOfFavoriteRoutes;

//        activity.setOfFavoriteRoutes = activity.sharedPreferences.getStringSet(activity.SHARED_PREFERENCES_ROUTES, null);
//        for (String route : activity.setOfFavoriteRoutes)
//        {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<List<Route>> listOfRoutes = new ArrayList<>();
        listOfRoutes.add(listOfFavoriteRoutes);
        RoutesAdapter adapter = new RoutesAdapter(activity, true, listOfRoutes);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
