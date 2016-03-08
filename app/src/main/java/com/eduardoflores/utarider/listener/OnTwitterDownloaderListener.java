package com.eduardoflores.utarider.listener;

import com.eduardoflores.utarider.model.twitter.Tweet;

import java.util.ArrayList;

/**
 * @author Eduardo Flores
 */
public interface OnTwitterDownloaderListener {

    public void onTwitterResultsListener(ArrayList<Tweet> arrayOfTweets);
}
