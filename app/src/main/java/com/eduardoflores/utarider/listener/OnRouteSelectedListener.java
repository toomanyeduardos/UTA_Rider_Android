package com.eduardoflores.utarider.listener;

import com.eduardoflores.utarider.model.localfiles.Route;

import android.view.View;
import android.widget.ArrayAdapter;

/**
 * @author Eduardo Flores
 */
public interface OnRouteSelectedListener
{
    void onRouteSelected(Route route);

    void onRouteFavorited(Route route, View view, ArrayAdapter<Route> adapter);

}
