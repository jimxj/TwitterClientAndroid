package com.jim.apps.twitter.api;

import org.apache.http.Header;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jim.apps.twitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//import retrofit.Callback;
//import retrofit.RestAdapter;
//import retrofit.http.GET;
//import retrofit.http.Path;
//import retrofit.http.Query;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
  private static final String TAG = "TwitterClient";

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "4HOHHbwBLpqDCrujWyZJ8U6YU";       // Change this
	public static final String REST_CONSUMER_SECRET = "hIGTaXhbcrMO1lN6zaYxtPT7ZbDDbISbefc4Kpz14EFFX9kDhm"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletwitter"; // Change this (here and in manifest)

  private Gson gson = new Gson();

  //private SimpleTwitterApi simpleTwitterApi;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);

//    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(REST_URL).build();
//    simpleTwitterApi = restAdapter.create(SimpleTwitterApi.class);
	}

  public void getHomeTimeline(Long sinceId, Long maxId, Integer count, final ApiCallback<List<Tweet>> callback) {
     //simpleTwitterApi.getHomeTimeline(sinceId, maxId, 20, callback);
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 20);
    if (null != sinceId) {
      params.put("since_id", sinceId);
    }
    if (null != maxId) {
      params.put("max_id", maxId  );
    }
    getClient().get(apiUrl, params, new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Type type = new TypeToken<List<Tweet>>() {}.getType();
        callback.success((List<Tweet>) gson.fromJson(new StringReader(new String(responseBody)), type));
        Log.d(TAG, "-------------getHomeTimeline response : \n" + new String(responseBody));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        callback.failure("Status code : " + statusCode + ", response body :\n" + new String(responseBody));
      }
    });
  }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

//	public interface SimpleTwitterApi {
//
//		@GET("/statuses/home_timeline.json")
//		void getHomeTimeline(@Query("since_id") Long sinceId,
//                         @Query("max_id") Long maxId,
//                         @Query("count") Integer count,
//                         Callback<List<Tweet>> callback);
//	}
}