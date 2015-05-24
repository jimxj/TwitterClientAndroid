/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
  // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
  public static String getRelativeTimeAgo(String rawJsonDate) {
    String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
    sf.setLenient(true);

    String relativeDate = "";
    try {
      long dateMillis = sf.parse(rawJsonDate).getTime();
      relativeDate = toRelativeTime(dateMillis);
//      relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//              System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return relativeDate;
  }

  public static String toRelativeTime(Long unixTimestamp) {
    String result = DateUtils.getRelativeTimeSpanString(unixTimestamp).toString();
    result = result.replace(" ago", "");
    result = result.replaceAll("minutes", "m");
    result = result.replaceAll("hours", "h");
    result = result.replaceAll("days", "d");
    result = result.replaceAll("weeks", "w");
    result = result.replaceAll("years", "y");
    result = result.replaceAll("minute", "m");
    result = result.replaceAll("hour", "h");
    result = result.replaceAll("day", "d");
    result = result.replaceAll("week", "w");
    result = result.replaceAll("year", "y");

    return result;
  }
}
