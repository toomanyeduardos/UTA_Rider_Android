package com.eduardoflores.utarider.fragment;

import com.eduardoflores.utarider.R;
import com.eduardoflores.utarider.activity.MainActivity;
import com.eduardoflores.utarider.adapter.RoutesAdapter;
import com.eduardoflores.utarider.listener.OnRouteSelectedListener;
import com.eduardoflores.utarider.model.localfiles.Route;
import com.eduardoflores.utarider.model.localfiles.Stop;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Eduardo Flores
 */
public class RoutesFragment extends ListFragment
{
    private MainActivity activity;

    private List<List<Route>> listSortedRoutes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        this.listSortedRoutes = activity.listOfRoutesSorted;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RoutesAdapter adapter = new RoutesAdapter(activity, false, listSortedRoutes);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
