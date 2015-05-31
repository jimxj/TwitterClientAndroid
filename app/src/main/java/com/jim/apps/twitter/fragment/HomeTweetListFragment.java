/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.fragment;

import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.models.Tweet;
import java.util.List;

public class HomeTweetListFragment extends TweetListFragment {
  private static final String TAG = "HomeTweetListFragment";

  @Override public String getLogTag() {
    return TAG;
  }

  @Override protected void getTimeline(Long sinceId, Long maxId, Integer count,
      ApiCallback<List<Tweet>> callback) {
    twitterClient.getHomeTimeline(sinceId, maxId, count, callback);
  }

  @Override public boolean needCache() {
    return true;
  }
}
