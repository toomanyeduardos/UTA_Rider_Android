package com.eduardoflores.utarider.service;

import com.eduardoflores.utarider.listener.OnTwitterDownloaderListener;
import com.eduardoflores.utarider.model.twitter.Tweet;
import com.eduardoflores.utarider.utils.AppUtils;

import android.os.AsyncTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Eduardo Flores
 */
public class GetTweeterDataService extends AsyncTask<String, Void, ArrayList<Tweet>>
{
    private OnTwitterDownloaderListener resultsListener;
    private String TWITTER_USERNAME = "RideUTA";
    private Twitter twitter;
    private Paging page;
    private int pageCount = 50;

    public void setOnTwitterDownloaderListener(OnTwitterDownloaderListener listener)
    {
        resultsListener = listener;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Tweet> doInBackground(String... screenName)
    {
        // gets Twitter instance with default credentials
        ArrayList<Tweet> arrayOfTweets = new ArrayList<Tweet>();
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(AppUtils.TWITTER_CONSUMER_KEY)
                    .setOAuthConsumerSecret(AppUtils.TWITTER_CONSUMER_SECRET)
                    .setOAuthAccessToken(AppUtils.TWITTER_ACCESS_TOKEN)
                    .setOAuthAccessTokenSecret(AppUtils.TWITTER_ACCESS_TOKEN_SECRET);
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter1 = tf.getInstance();
            List<twitter4j.Status> statuses;
            String user = "user";

            statuses = twitter1.getUserTimeline("RideUTA");

            for (twitter4j.Status status : statuses) {
                Tweet tweet = new Tweet();
                tweet.tweet_text = status.getText();
                tweet.tweet_date = convertDateAndTime(status.getCreatedAt());
                tweet.tweet_screenName = status.getUser().getScreenName();

                URLEntity[] entities = status.getURLEntities();
                for (URLEntity entity : entities)
                {
                    tweet.tweet_url = entity.getURL();
                }
                arrayOfTweets.add(tweet);
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
        return arrayOfTweets;
    }

    private String convertDateAndTime(Date dateTime)
    {
        String dateAndTime = null;

        DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateFormat outputFormat = new SimpleDateFormat("EEE MMM dd yyyy" + "', '" + "h:mm a");
        dateAndTime = outputFormat.format(dateTime);

        return dateAndTime;
    }

    @Override
    protected void onPostExecute(ArrayList<Tweet> tweets)
    {
        super.onPostExecute(tweets);
        resultsListener.onTwitterResultsListener(tweets);
    }
}
