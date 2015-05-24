/**
 * File generated by Magnet rest2mobile 1.1 - May 22, 2015 10:30:30 PM
 * @see {@link http://developer.magnet.com}
 */

package com.codepath.apps.twitter.controller.api;

import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.async.StateChangedListener;

import com.codepath.apps.twitter.model.beans.*;

public interface SimpleTwitterApi {

  /**
   * Generated from URL POST https://api.twitter.com/1.1/favorites/create.json?id=243138128959913986
   * POST 1.1/favorites/create.json
   * @param id  style:QUERY
   * @param listener
   * @return FavoriteResult
   */
  Call<FavoriteResult> favorite(
     String id,
     StateChangedListener listener
  );


}
