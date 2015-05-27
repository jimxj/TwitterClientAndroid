/**
 * File generated by Magnet rest2mobile 1.1 - May 24, 2015 11:23:42 PM
 * @see {@link http://developer.magnet.com}
 */

package com.jim.apps.twitter.controller.api;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.controller.ControllerFactory;
import com.magnet.android.mms.controller.AbstractControllerSchemaFactory;
import com.magnet.android.mms.controller.RequestSchema;
import com.magnet.android.mms.controller.RequestSchema.JMethod;
import com.magnet.android.mms.controller.RequestSchema.JMeta;
import com.magnet.android.mms.controller.RequestSchema.JParam;

import java.util.Arrays;
import java.util.Collection;

import com.jim.apps.twitter.model.beans.*;
import java.util.List;

public class TwitterClient2Factory extends ControllerFactory<TwitterClient2> {
  public TwitterClient2Factory(MagnetMobileClient magnetClient) {
    super(TwitterClient2.class, TwitterClient2SchemaFactory.getInstance().getSchema(), magnetClient);
  }

  // Schema factory for controller TwitterClient2
  public static class TwitterClient2SchemaFactory extends AbstractControllerSchemaFactory {
    private static TwitterClient2SchemaFactory __instance;

    public static synchronized TwitterClient2SchemaFactory getInstance() {
      if(null == __instance) {
        __instance = new TwitterClient2SchemaFactory();
      }

      return __instance;
    }

    private TwitterClient2SchemaFactory() {}

    protected synchronized void initSchemaMaps() {
      if(null != schema) {
        return;
      }

      schema = new RequestSchema();
      schema.setRootPath("");

      //controller schema for controller method getMedia
      JMethod method1 = addMethod("getMedia",
        "api/1.1",
        "GET",
        List.class,
        MediaResult.class,
        null,
        Arrays.asList("application/json"));
      method1.setBaseUrl("http://twitter.com");
    }

  }

}
