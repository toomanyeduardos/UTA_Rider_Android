package com.eduardoflores.utarider.adapter;

import com.eduardoflores.utarider.R;
import com.eduardoflores.utarider.activity.MainActivity;
import com.eduardoflores.utarider.model.localfiles.Route;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * @author Eduardo Flores
 */
public class RoutesAdapter extends ArrayAdapter<Route>
{
    private Context context;

    private List<List<Route>> listRoutes;

    private boolean isFavoriteList;

    private MainActivity activity;

    public RoutesAdapter(Context context, boolean isFavoriteList, List<List<Route>> routes)
    {
        super(context, R.layout.route_list_item, routes.get(0));
        this.context = context;
        this.listRoutes = routes;
        this.isFavoriteList = isFavoriteList;
        activity = (MainActivity)context;
    }

    @Override
    public int getCount()
    {
        int numberExtraCells;

        if (isFavoriteList)
        {
            numberExtraCells = 1;
            return listRoutes.get(0).size() + numberExtraCells;
        }
        else
        {
            numberExtraCells = 3;
            return listRoutes.get(0).size() +
                    listRoutes.get(1).size() +
                    listRoutes.get(2).size() +
                    numberExtraCells;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (isFavoriteList)
        {
            switch (position)
            {
                case 0:
                    View viewFavoriteTitle = inflater.inflate(R.layout.route_list_title, parent, false);
                    TextView favoriteTitle = (TextView)viewFavoriteTitle.findViewById(R.id.route_list_title);
                    favoriteTitle.setText(context.getText(R.string.route_favorite));
                    if (listRoutes.get(0).size() < 1)
                    {
                        favoriteTitle.setText(context.getText(R.string.route_favorite_empty));
                    }
                    return viewFavoriteTitle;
                default:
                    View viewFavorite = inflater.inflate(R.layout.route_list_item, parent, false);
                    final Route routeFavorite = listRoutes.get(0).get(position - 1);
                    TextView routeNameFavorite = (TextView)viewFavorite.findViewById(R.id.route_name);
                    TextView routeDetailsFavorite = (TextView)viewFavorite.findViewById(R.id.route_detail);
                    Switch routeFavoriteSwitch = (Switch)viewFavorite.findViewById(R.id.route_switch);
                    ImageButton routeFavoriteButton = (ImageButton)viewFavorite.findViewById(R.id.favorite_image);
                    routeNameFavorite.setText(routeFavorite.routeLongName);
                    routeDetailsFavorite.setText(routeFavorite.routeShortName);
                    if (routeFavorite.isFavorite)
                    {
                        routeFavoriteButton.setImageResource(R.drawable.ic_star_selected);
                    }
                    else
                    {
                        routeFavoriteButton.setImageResource(R.drawable.ic_star_empty);
                    }
                    routeFavoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            routeFavorite.isSelected = isChecked;
                            activity.onRouteSelected(routeFavorite);
                        }
                    });
                    routeFavoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onRouteFavorited(routeFavorite, v, RoutesAdapter.this);
                        }
                    });
                    return viewFavorite;
            }

        }
        else
        {
            switch (position)
            {
                case 0:
                    View viewTraxTitle = inflater.inflate(R.layout.route_list_title, parent, false);
                    TextView traxTitle = (TextView)viewTraxTitle.findViewById(R.id.route_list_title);
                    traxTitle.setText(context.getText(R.string.route_title_trax));
                    return viewTraxTitle;
                case 1:
                case 2:
                case 3:
                case 4:
                    View viewTrax = inflater.inflate(R.layout.route_list_item, parent, false);
                    final Route routeTrax = listRoutes.get(0).get(position - 1);
                    TextView routeNameTrax = (TextView)viewTrax.findViewById(R.id.route_name);
                    TextView routeDetailsTrax = (TextView)viewTrax.findViewById(R.id.route_detail);
                    Switch routeTraxSwitch = (Switch)viewTrax.findViewById(R.id.route_switch);
                    ImageButton routeTraxFavorite = (ImageButton)viewTrax.findViewById(R.id.favorite_image);
                    routeNameTrax.setText(routeTrax.routeLongName);
                    routeDetailsTrax.setText(routeTrax.routeShortName);
                    if (routeTrax.isFavorite)
                    {
                        routeTraxFavorite.setImageResource(R.drawable.ic_star_selected);
                    }
                    else
                    {
                        routeTraxFavorite.setImageResource(R.drawable.ic_star_empty);
                    }
                    routeTraxSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            routeTrax.isSelected = isChecked;
                            activity.onRouteSelected(routeTrax);
                        }
                    });
                    routeTraxFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onRouteFavorited(routeTrax, v, RoutesAdapter.this);
                        }
                    });
                    return viewTrax;
                case 5:
                    View viewFrontrunnerTitle = inflater.inflate(R.layout.route_list_title, parent, false);
                    TextView frontrunnerTitle = (TextView)viewFrontrunnerTitle.findViewById(R.id.route_list_title);
                    frontrunnerTitle.setText(context.getText(R.string.route_title_frontrunner));
                    return viewFrontrunnerTitle;
                case 6:
                    View viewFrontrunner = inflater.inflate(R.layout.route_list_item, parent, false);
                    final Route routeFrontrunner = listRoutes.get(1).get(position - 6);
                    TextView routeNameFrontrunner = (TextView)viewFrontrunner.findViewById(R.id.route_name);
                    TextView routeDetailsFrontrunner = (TextView)viewFrontrunner.findViewById(R.id.route_detail);
                    Switch routeFrontrunnerSwitch = (Switch)viewFrontrunner.findViewById(R.id.route_switch);
                    ImageButton routeFrontrunnerFavorite = (ImageButton)viewFrontrunner.findViewById(R.id.favorite_image);
                    routeNameFrontrunner.setText(routeFrontrunner.routeLongName);
                    routeDetailsFrontrunner.setText(routeFrontrunner.routeShortName);
                    if (routeFrontrunner.isFavorite)
                    {
                        routeFrontrunnerFavorite.setImageResource(R.drawable.ic_star_selected);
                    }
                    else
                    {
                        routeFrontrunnerFavorite.setImageResource(R.drawable.ic_star_empty);
                    }
                    routeFrontrunnerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            routeFrontrunner.isSelected = isChecked;
                            activity.onRouteSelected(routeFrontrunner);
                        }
                    });
                    routeFrontrunnerFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onRouteFavorited(routeFrontrunner, v, RoutesAdapter.this);
                        }
                    });
                    return viewFrontrunner;
                case 7:
                    View viewBusTitle = inflater.inflate(R.layout.route_list_title, parent, false);
                    TextView busTitle = (TextView)viewBusTitle.findViewById(R.id.route_list_title);
                    busTitle.setText(context.getText(R.string.route_title_bus));
                    return viewBusTitle;
                default:
                    View viewBus = inflater.inflate(R.layout.route_list_item, parent, false);
                    final Route routeBus = listRoutes.get(2).get(position - 8);
                    TextView routeNameBus = (TextView)viewBus.findViewById(R.id.route_name);
                    TextView routeDetailsBus = (TextView)viewBus.findViewById(R.id.route_detail);
                    Switch routeBusSwitch = (Switch)viewBus.findViewById(R.id.route_switch);
                    ImageButton routeBusFavorite = (ImageButton)viewBus.findViewById(R.id.favorite_image);
                    routeNameBus.setText(routeBus.routeLongName);
                    routeDetailsBus.setText(routeBus.routeShortName);
                    if (routeBus.isFavorite)
                    {
                        routeBusFavorite.setImageResource(R.drawable.ic_star_selected);
                    }
                    else
                    {
                        routeBusFavorite.setImageResource(R.drawable.ic_star_empty);
                    }
                    routeBusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            routeBus.isSelected = isChecked;
                            activity.onRouteSelected(routeBus);
                        }
                    });
                    routeBusFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.onRouteFavorited(routeBus, v, RoutesAdapter.this);
                        }
                    });
                    return viewBus;
            }
        }
    }

}
