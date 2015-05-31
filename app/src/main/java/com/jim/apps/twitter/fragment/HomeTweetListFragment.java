/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.fragment;

public class HomeTweetListFragment extends TweetListFragment {
  private static final String TAG = "HomeTweetListFragment";

  @Override public String getLogTag() {
    return TAG;
  }

  @Override public boolean needCache() {
    return true;
  }
}
