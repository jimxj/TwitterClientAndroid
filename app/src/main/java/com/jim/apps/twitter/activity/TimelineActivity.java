package com.jim.apps.twitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jim.apps.twitter.ComposeDialogFragment;
import com.jim.apps.twitter.OnNewTweetListener;
import com.jim.apps.twitter.R;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.fragment.HomeTweetListFragment;
import com.jim.apps.twitter.fragment.TweetListFragment;
import com.jim.apps.twitter.models.Tweet;
import com.melnykov.fab.FloatingActionButton;
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit.client.Response;

public class TimelineActivity extends ActionBarActivity
                              implements OnNewTweetListener {
  private static final String TAG = "TimelineActivity";

  @InjectView(R.id.tbMain)
  Toolbar toolbar;

  @InjectView(R.id.fab)
  FloatingActionButton fab;

  HomeTweetListFragment tweetListFragment;

  protected TwitterClient twitterClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_timeline);

    ButterKnife.inject(this);

    tweetListFragment =
        (HomeTweetListFragment) getSupportFragmentManager().findFragmentById(R.id.fmTweetList);

    twitterClient = TwitterApplication.getTwitterClient();

    setupActionBar();

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fm = TimelineActivity.this.getSupportFragmentManager();
        ComposeDialogFragment dialog = ComposeDialogFragment.newInstance(null, null);
        dialog.show(fm, "Compose a new tweet");
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == TweetDetailsActivity.REQUEST_CODE) {
      int status = data.getIntExtra("status", 0);
      if((status & TweetDetailsActivity.STATUS_UPDATED) == TweetDetailsActivity.STATUS_UPDATED) {
        int index = data.getIntExtra("index", -1);
        Tweet updatedTweet = (Tweet) data.getSerializableExtra("tweet");
        if(index >= 0 && null != updatedTweet) {
          tweetListFragment.update(index, updatedTweet);
        }
      }

      if((status & TweetDetailsActivity.STATUS_ADDED) == TweetDetailsActivity.STATUS_ADDED) {
        //fetchTweets(true);
        tweetListFragment.setNeedRefresh(true);
      }
    }
  }

  private void setupActionBar() {
    //setSupportActionBar(toolbar);
    //toolbar.setLogo(R.drawable.ic_launcher);
    toolbar.setNavigationIcon(R.drawable.twitter_bird);
//    ActionBar actionBar = getSupportActionBar();
    //getSupportActionBar().setHomeButtonEnabled(true);
    //getSupportActionBar().setDisplayShowHomeEnabled(true);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    //getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    actionBar.setLogo(R.drawable.ic_launcher);
//    actionBar.setDisplayUseLogoEnabled(true);
////    actionBar.setTitle(null);

//    toolbar.setNavigationIcon(R.drawable.ic_launcher);
    //toolbar.setTitle("Title");
    //toolbar.setSubtitle("Sub");

    //toolbar.setLogo(R.drawable.ic_launcher);
  }

  @Override
  public void onNewTweet(String text) {
    Log.d(TAG, "-----------onNewTweet : " + text);
    twitterClient.newTweet(text, null, new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet tweet) {
        onNewTweet(tweet);
      }

      @Override
      public void failure(String error) {

      }
    });
  }

  @Override
  public void onNewTweet(Tweet tweet) {
    tweetListFragment.addLocalTweet(tweet);
    //localNewTweets.add(tweet);
    //tweetListAdapter.insert(tweet, 0);
    //tweetListAdapter.notifyDataSetChanged();
  }

  @Override
  public void onReplyTweet(Long inReplyId, String text) {
    Log.d(TAG, "-----------onReplyTweet : " + text + ", inReplyId = " + inReplyId);
    twitterClient.newTweet(text, inReplyId, new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet tweet) {
        onNewTweet(tweet);
      }

      @Override
      public void failure(String error) {

      }
    });
  }

}
