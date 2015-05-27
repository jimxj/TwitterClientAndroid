/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.caching;

import com.google.gson.Gson;
import java.lang.reflect.Type;

public class CacheMashaller {

  private final Gson gson;

  public CacheMashaller() {
    gson = new Gson();
  }

  public String toString(Object object) {
    return gson.toJson(object);
  }

  public Object fromCache(String cacheStr, Type type) {
    return gson.fromJson(cacheStr, type);
  }

}
