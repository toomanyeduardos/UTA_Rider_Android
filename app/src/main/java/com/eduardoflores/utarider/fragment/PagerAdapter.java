package com.eduardoflores.utarider.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Eduardo Flores
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FavoritesFragment tabFavorites = new FavoritesFragment();
                return tabFavorites;
            case 1:
                RoutesFragment tabRoutes = new RoutesFragment();
                return tabRoutes;
            case 2:
                Map tabMap = new Map();
                return tabMap;
            case 3:
                TwitterFragment tabTwitter = new TwitterFragment();
                return tabTwitter;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
