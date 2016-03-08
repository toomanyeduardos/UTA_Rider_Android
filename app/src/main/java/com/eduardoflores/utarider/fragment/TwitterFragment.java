package com.eduardoflores.utarider.fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.eduardoflores.utarider.AnalyticsTrackers;
import com.eduardoflores.utarider.adapter.TwitterAdapter;
import com.eduardoflores.utarider.listener.OnTwitterDownloaderListener;
import com.eduardoflores.utarider.model.twitter.Tweet;
import com.eduardoflores.utarider.service.GetTweeterDataService;
import com.eduardoflores.utarider.utils.AppUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Eduardo Flores
 */
public class TwitterFragment extends ListFragment implements OnTwitterDownloaderListener
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GetTweeterDataService task = new GetTweeterDataService();
        task.setOnTwitterDownloaderListener(this);
        task.execute();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (menuVisible)
        {
            // send analytics reporting fragment is visible
            Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
            tracker.setScreenName("TwitterFragment");
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    @Override
    public void onTwitterResultsListener(ArrayList<Tweet> arrayOfTweets) {
        TwitterAdapter adapter = new TwitterAdapter(getActivity(), arrayOfTweets);
        setListAdapter(adapter);
    }
}
