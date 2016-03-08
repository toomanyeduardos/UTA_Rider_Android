package com.eduardoflores.utarider.adapter;

import com.eduardoflores.utarider.R;
import com.eduardoflores.utarider.model.twitter.Tweet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author Eduardo Flores
 */
public class TwitterAdapter extends ArrayAdapter<Tweet>
{
    private Context context;

    private List<Tweet> tweetList;

    public TwitterAdapter(Context context, List<Tweet> tweetList)
    {
        super(context, R.layout.twitter_list_item, tweetList);
        this.context = context;
        this.tweetList = tweetList;
    }

    @Override
    public int getCount() {
        return tweetList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.twitter_list_item, parent, false);

        TextView tweetContent = (TextView)view.findViewById(R.id.tweet_content);
        TextView tweetTimeDate = (TextView)view.findViewById(R.id.tweet_time_date);

        tweetContent.setText(tweetList.get(position).tweet_text);
        tweetTimeDate.setText(tweetList.get(position).tweet_date);

        return view;
    }
}
