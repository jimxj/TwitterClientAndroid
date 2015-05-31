/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.reflect.TypeToken;
import com.jim.apps.twitter.R;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.activity.TweetDetailsActivity;
import com.jim.apps.twitter.adapter.TweetAdapter;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.caching.CacheMashaller;
import com.jim.apps.twitter.caching.CachingDatabase;
import com.jim.apps.twitter.connectivity.ConnectivityListener;
import com.jim.apps.twitter.connectivity.ConnectivityManager;
import com.jim.apps.twitter.customview.EndlessScrollListener;
import com.jim.apps.twitter.models.Tweet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TweetListFragment extends Fragment {

  @InjectView(R.id.lvTimeline) ListView lvTimeline;

  @InjectView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

  protected TweetAdapter tweetListAdapter;

  protected TwitterClient twitterClient;

  protected Long sinceId = 1l;
  protected Long maxId;

  protected ConnectivityListener connectivityListener;

  @InjectView(R.id.llNetworkStatus) LinearLayout llNetworkStatus;


  protected CacheMashaller cacheMashaller;
  protected CachingDatabase cachingDatabase;

  protected boolean needRefresh;
  Set<Long> localNewTweetIds = new HashSet<>();
  // Save tweets created by retweet or reply locally, only add them to
  // adapter when the listview is scrolled
  List<Tweet> localNewTweets = new ArrayList<>();

  public abstract String getLogTag();

  protected abstract void getTimeline(Long sinceId, Long maxId, Integer count,
      final ApiCallback<List<Tweet>> callback);

  public boolean needCache() {
    return false;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

    ButterKnife.inject(this, view);
    Fresco.initialize(getActivity());

    if(needCache()) {
      cacheMashaller = new CacheMashaller();
      cachingDatabase = new CachingDatabase(getActivity());
    }

    twitterClient = TwitterApplication.getTwitterClient();

    tweetListAdapter = new TweetAdapter(getActivity(), new ArrayList<Tweet>());
    lvTimeline.setAdapter(tweetListAdapter);

    lvTimeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), TweetDetailsActivity.class);
        i.putExtra("tweet", tweetListAdapter.getItem(position));
        i.putExtra("index", position);
        startActivityForResult(i, TweetDetailsActivity.REQUEST_CODE);
      }
    });

    connectivityListener = new ConnectivityListener() {
      @Override
      public void onConnectivityStatusChanged(int lastKnowStatus, int newStatus) {
        if(newStatus == ConnectivityManager.TYPE_NOT_CONNECTED) {
          llNetworkStatus.setVisibility(View.VISIBLE);
        } else {
          llNetworkStatus.setVisibility(View.INVISIBLE);
        }
      }
    };

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

    if(isNetworkNotReachable()) {
      llNetworkStatus.setVisibility(View.VISIBLE);

      if(needCache()) {
        loadTweetsFromCache();
      }
    } else {
      swipeContainer.setRefreshing(true);
      fetchTweets(true);
    }

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

    return view;
  }

  public void updateRow(Long tweetId) {
    int start = lvTimeline.getFirstVisiblePosition();
    for (int i = start, j = lvTimeline.getLastVisiblePosition(); i <= j; i++) {
      if (tweetId.equals(((Tweet) lvTimeline.getItemAtPosition(i)).getId())) {
        View view = lvTimeline.getChildAt(i - start);
        lvTimeline.getAdapter().getView(i, view, lvTimeline);
        break;
      }
    }
  }

  public void update(int index, Tweet updatedTweet) {
    tweetListAdapter.getItem(index).copyFrom(updatedTweet);
    updateRow(updatedTweet.getId());
  }

  public void addLocalTweet(Tweet tweet) {
    localNewTweets.add(tweet);
  }

  public void setNeedRefresh(boolean value) {
    needRefresh = value;
  }

  private void fetchTweets(final boolean isLoadingLatest) {
    Log.d(getLogTag(), "--------------fetchTweets, isLoadingLatest = "
        + isLoadingLatest
        + ", sinceId = "
        + sinceId
        + ", maxId = "
        + maxId);
    if(isNetworkNotReachable()) {
      Log.d(getLogTag(), "--------------fetchTweets, Network not reachable.");
      swipeContainer.setRefreshing(false);
      return;
    }
    getTimeline(isLoadingLatest ? sinceId : null,
        !isLoadingLatest ? maxId : null, null, new ApiCallback<List<Tweet>>() {
          @Override
          public void success(List<Tweet> tweets) {
            Log.d(getLogTag(), "fetched " + tweets.size() + " new tweets : " + tweets);
            if (0 == tweets.size()) {
              swipeContainer.setRefreshing(false);
              return;
            }

            if (isLoadingLatest) {
              if (1 == sinceId) {
                tweetListAdapter.clear();
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
            Log.e(getLogTag(), "Failed to fetch tweets : " + error);

            swipeContainer.setRefreshing(false);
          }
        });
  }

  @Override public void onResume() {
    super.onResume();

    ConnectivityManager.getInstance().registerListener(connectivityListener);
  }

  @Override public void onStop() {
    super.onStop();

    if(tweetListAdapter.getCount() > 0) {
      List<Tweet> tweetsToCache = new ArrayList<>(tweetListAdapter.getCount());
      for (int i = 0; i < tweetListAdapter.getCount(); i++) {
        tweetsToCache.add(tweetListAdapter.getItem(i));
      }
      cachingDatabase.addToCache(getLogTag(), cacheMashaller.toString(tweetsToCache));
    }

    ConnectivityManager.getInstance().removeListener(connectivityListener);
  }

  protected boolean isNetworkNotReachable() {
    return ConnectivityManager.TYPE_NOT_CONNECTED == ConnectivityManager.getInstance().getConnectivityStatus();
  }

  private void loadTweetsFromCache() {
    String cachedBody = cachingDatabase.getCache(getLogTag());
    if(null != cachedBody) {
      List<Tweet> cachedTweets = (List<Tweet>) cacheMashaller.fromCache(cachedBody, new TypeToken<List<Tweet>>() {}.getType());
      tweetListAdapter.addAll(cachedTweets);
    }
  }

  /**
   * Insert the new fetched tweets on the top of the list
   * @param tweets
   */
  private void insertIntoAdapter(List<Tweet> tweets) {
    for (int i = tweets.size() - 1; i >= 0; i--) {
      if(!localNewTweetIds.contains(tweets.get(i).getId())) {
        tweetListAdapter.insert(tweets.get(i), 0);
      }
    }
  }
}
