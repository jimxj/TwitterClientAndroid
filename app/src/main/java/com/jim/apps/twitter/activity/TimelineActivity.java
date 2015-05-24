package com.jim.apps.twitter.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitter.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.adapter.TweetAdapter;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit.client.Response;

public class TimelineActivity extends ActionBarActivity {
  private static final String TAG = "TimelineActivity";

  private TweetAdapter tweetListAdapter;

  @InjectView(R.id.lvTimeline)
  ListView lvTimeline;

  TwitterClient twitterClient;

  Long sinceId = 1l;
  Long maxId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    ButterKnife.inject(this);
    Fresco.initialize(this);

    twitterClient = TwitterApplication.getTwitterClient();

    tweetListAdapter = new TweetAdapter(this, new ArrayList<Tweet>());
    lvTimeline.setAdapter(tweetListAdapter);

    fetchTweets();
  }

  private void fetchTweets() {
    twitterClient.getHomeTimeline(sinceId, maxId, null, new ApiCallback<List<Tweet>>() {
      @Override
      public void success(List<Tweet> tweets) {
        Log.e(TAG, "fetch tweets : " + tweets);
        if(1 == sinceId) {
          tweetListAdapter.addAll(tweets);

        }

        if(null != tweets && tweets.size() > 0) {
          tweetListAdapter.notifyDataSetChanged();
        }
      }

      @Override
      public void failure(String error) {
        Log.e(TAG, "Failed to fetch tweets : " + error);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
}
