/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class CommonUtil {

  public static int dpToPixel(Context context, int dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    // convert the DP into pixel
    return (int)(dp * scale + 0.5f);
  }

  public static int pixelToDp(Context context, float px){
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    float dp = px / (metrics.densityDpi / 160f);
    return (int) dp;
  }
}
