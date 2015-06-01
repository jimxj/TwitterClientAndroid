/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.fragment.timeline;

import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.models.Tweet;
import java.util.List;

public class MentionsTweetListFragment extends AbstractTweetListFragment {
  private static final String TAG = "MentionsTweetListFragment";

  @Override public String getLogTag() {
    return TAG;
  }

  @Override public boolean needCache() {
    return false;
  }

  @Override protected void getTimeline(Long sinceId, Long maxId, Integer count,
      ApiCallback<List<Tweet>> callback) {
    twitterClient.getMentionsTimeline(sinceId, maxId, count, callback);
  }
}
