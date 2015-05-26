/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.util;

import com.jim.apps.twitter.R;

public class ResourceUtil {

  public static int getRetweetIconResource(boolean isRetweeted) {
    return isRetweeted ? R.drawable.retweet_on : R.drawable.retweet;
  }

  public static  int getFavorateIconResource(boolean isFavorated) {
    return isFavorated ? R.drawable.favorite_on : R.drawable.favorite;
  }

}
