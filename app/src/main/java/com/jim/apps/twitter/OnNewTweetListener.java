/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter;

import com.jim.apps.twitter.models.Tweet;

public interface OnNewTweetListener {
  void onNewTweet(String text);
  void onNewTweet(Tweet tweet);
  void onReplyTweet(Long inReplyId, String text);
}
