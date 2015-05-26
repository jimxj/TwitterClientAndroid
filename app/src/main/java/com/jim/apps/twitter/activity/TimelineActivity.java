package com.jim.apps.twitter.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jim.apps.twitter.ComposeDialogFragment;
import com.jim.apps.twitter.OnNewTweetListener;
import com.jim.apps.twitter.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.adapter.TweetAdapter;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.models.Tweet;
import com.jim.apps.twitter.customview.EndlessScrollListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit.client.Response;

public class TimelineActivity extends ActionBarActivity
                              implements OnNewTweetListener {
  private static final String TAG = "TimelineActivity";

  private TweetAdapter tweetListAdapter;

  @InjectView(R.id.lvTimeline)
  ListView lvTimeline;

  @InjectView(R.id.swipeContainer)
  SwipeRefreshLayout swipeContainer;

  @InjectView(R.id.tbMain)
  Toolbar toolbar;

  @InjectView(R.id.fab)
  FloatingActionButton fab;

  TwitterClient twitterClient;

  Long sinceId = 1l;
  Long maxId;

  Set<Long> localNewTweetIds = new HashSet<>();
  // Save tweets created by retweet or reply locally, only add them to
  // adapter when the listview is scrolled
  List<Tweet> localNewTweets = new ArrayList<>();

  boolean needRefresh;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    ButterKnife.inject(this);
    Fresco.initialize(this);

    setupActionBar();

    twitterClient = TwitterApplication.getTwitterClient();

    tweetListAdapter = new TweetAdapter(this, new ArrayList<Tweet>());
    lvTimeline.setAdapter(tweetListAdapter);

    lvTimeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(TimelineActivity.this, TweetDetailsActivity.class);
        i.putExtra("tweet", tweetListAdapter.getItem(position));
        i.putExtra("index", position);
        startActivityForResult(i, TweetDetailsActivity.REQUEST_CODE);
      }
    });

    fetchTweets(true);

    // Setup refresh listener which triggers new data loading
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        // Your code to refresh the list here.
        // Make sure you call swipeContainer.setRefreshing(false)
        // once the network request has completed successfully.
        fetchTweets(true);
      }
    });
    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

    lvTimeline.setOnScrollListener(new EndlessScrollListener() {

      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        //Log.i(TAG, "page : " + page + ", totalItemsCount : " + totalItemsCount + ", current page : " + GoogleImageSearch.getInstance().getCurrentPage());
        fetchTweets(false);
      }

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);

        if(scrollState == SCROLL_STATE_FLING) {
          if(localNewTweets.size() > 0) {
            insertIntoAdapter(localNewTweets);
            tweetListAdapter.notifyDataSetChanged();
            localNewTweets.clear();
          } else if(needRefresh) {
            fetchTweets(true);
          }
        }
      }
    });

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fm = TimelineActivity.this.getSupportFragmentManager();
        ComposeDialogFragment dialog = ComposeDialogFragment.newInstance(null, null);
        dialog.show(fm, "Compose a new tweet");
      }
    });
  }

  private void fetchTweets(final boolean isLoadingLatest) {
    Log.d(TAG, "--------------fetchTweets, isLoadingLatest = " + isLoadingLatest + ", sinceId = " + sinceId + ", maxId = " + maxId);
    twitterClient.getHomeTimeline(isLoadingLatest ? sinceId : null,
            !isLoadingLatest ? maxId : null, null, new ApiCallback<List<Tweet>>() {
              @Override
              public void success(List<Tweet> tweets) {
                Log.d(TAG, "fetched " + tweets.size() + " new tweets : " + tweets);
                if (0 == tweets.size()) {
                  swipeContainer.setRefreshing(false);
                  return;
                }

                if (isLoadingLatest) {
                  if (1 == sinceId) {
                    tweetListAdapter.addAll(tweets);
                    if (null == maxId) {
                      maxId = tweets.get(tweets.size() - 1).getId();
                    }
                  } else {
                    insertIntoAdapter(tweets);
                  }

                  sinceId = tweets.get(0).getId();
                } else {
                  tweetListAdapter.addAll(tweets);
                  maxId = tweets.get(tweets.size() - 1).getId();
                }

                tweetListAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
              }

              @Override
              public void failure(String error) {
                Log.e(TAG, "Failed to fetch tweets : " + error);

                swipeContainer.setRefreshing(false);
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
          tweetListAdapter.getItem(index).copyFrom(updatedTweet);
          updateRow(updatedTweet.getId());
        }
      }

      if((status & TweetDetailsActivity.STATUS_ADDED) == TweetDetailsActivity.STATUS_ADDED) {
        //fetchTweets(true);
        needRefresh = true;
      }
    }
  }

  private void updateRow(Long tweetId) {
    int start = lvTimeline.getFirstVisiblePosition();
    for (int i = start, j = lvTimeline.getLastVisiblePosition(); i <= j; i++) {
      if (tweetId.equals(((Tweet) lvTimeline.getItemAtPosition(i)).getId())) {
        View view = lvTimeline.getChildAt(i - start);
        lvTimeline.getAdapter().getView(i, view, lvTimeline);
        break;
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
    localNewTweets.add(tweet);
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

  private void insertIntoAdapter(List<Tweet> tweets) {
    for (int i = tweets.size() - 1; i >= 0; i--) {
      if(!localNewTweetIds.contains(tweets.get(i).getId())) {
        tweetListAdapter.insert(tweets.get(i), 0);
      }
    }
  }
}
