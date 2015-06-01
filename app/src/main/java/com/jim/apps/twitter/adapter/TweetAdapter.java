/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.apps.twitter.ComposeDialogFragment;
import com.jim.apps.twitter.OnNewTweetListener;
import com.jim.apps.twitter.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.activity.UserProfileActivity;
import com.jim.apps.twitter.util.DateUtil;
import com.jim.apps.twitter.activity.TimelineActivity;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.models.Tweet;
import com.jim.apps.twitter.customview.LinkifiedTextView;
import com.jim.apps.twitter.util.ResourceUtil;

import java.util.List;


public class TweetAdapter extends ArrayAdapter<Tweet> {

  public static final String TAG = "TweetListAdapter";

  TwitterClient twitterClient = TwitterApplication.getTwitterClient();
  private OnNewTweetListener newTweetListener;

  // View lookup cache
  private static class ViewHolder {
    SimpleDraweeView authorImage;
    TextView userName;
    TextView screenName;
    TextView postTime;
    LinkifiedTextView text;
    TextView retweetNum;
    TextView favorateNum;
    SimpleDraweeView postImage;
    ImageButton ivRetweet;
    ImageButton ivFavorate;
    ImageButton ivReply;
    //TextView likes;
    //TextView commentNum;
  }

  public TweetAdapter(Context context, List<Tweet> objects) {
    super(context, R.layout.item_tweet, objects);

    newTweetListener = (OnNewTweetListener) context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final Tweet tweet = getItem(position);

    final ViewHolder viewHolder = null == convertView ? new ViewHolder() :  (ViewHolder) convertView.getTag();
    if(null == convertView) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

      viewHolder.authorImage = (SimpleDraweeView) convertView.findViewById(R.id.ivProfileImage);
      viewHolder.userName = (TextView) convertView.findViewById(R.id.tvUserName);
      viewHolder.screenName = (TextView) convertView.findViewById(R.id.tvScreenName);
      viewHolder.postTime = (TextView) convertView.findViewById(R.id.tvTime);
      viewHolder.text = (LinkifiedTextView) convertView.findViewById(R.id.tvText);
      viewHolder.retweetNum = (TextView) convertView.findViewById(R.id.tvRetweetNum);
      viewHolder.favorateNum = (TextView) convertView.findViewById(R.id.tvFavorateNum);
      viewHolder.postImage = (SimpleDraweeView) convertView.findViewById(R.id.ivPhoto);
      viewHolder.ivFavorate = (ImageButton) convertView.findViewById(R.id.ivFavorate);
      viewHolder.ivRetweet = (ImageButton) convertView.findViewById(R.id.ivRetweet);
      viewHolder.ivReply = (ImageButton) convertView.findViewById(R.id.ivReply);
      //viewHolder.postImage = (SimpleDraweeView) convertView.findViewById(R.id.ivPhoto);
      //viewHolder.likes = (TextView) convertView.findViewById(R.id.tvLikeNum);
      //viewHolder.commentNum = (TextView) convertView.findViewById(R.id.tvViewAllComments);

      convertView.setTag(viewHolder);
    }

    viewHolder.authorImage.setImageURI(Uri.parse(tweet.getUser().getProfile_image_url()));
    viewHolder.authorImage.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent i = new Intent(getContext(), UserProfileActivity.class);
        // TODO : user parceable instead
        i.putExtra("user", tweet.getUser());
        getContext().startActivity(i);
      }
    });

    //viewHolder.postImage.setImageURI(Uri.parse(photo.getImages().getStandard_resolution().getUrl()));

    viewHolder.postTime.setText(DateUtil.getRelativeTimeAgo(tweet.getCreated_at()));

    viewHolder.userName.setText(tweet.getUser().getName());
    viewHolder.screenName.setText("@" + tweet.getUser().getScreen_name());
    viewHolder.text.setText(tweet.getText());
    viewHolder.retweetNum.setText(String.valueOf(tweet.getRetweet_count()));
    viewHolder.favorateNum.setText(String.valueOf(tweet.getFavorite_count()));

    String mediaUrl = tweet.getFirstPhotoMediaUrl();
    if(null != mediaUrl) {
      viewHolder.postImage.setImageURI(Uri.parse(mediaUrl));
      viewHolder.postImage.setVisibility(View.VISIBLE);

      android.view.ViewGroup.LayoutParams layoutParams = viewHolder.postImage.getLayoutParams();
      //layoutParams.width = 80;
      layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
      viewHolder.postImage.setLayoutParams(layoutParams);
    } else {
      viewHolder.postImage.setVisibility(View.INVISIBLE);

      android.view.ViewGroup.LayoutParams layoutParams = viewHolder.postImage.getLayoutParams();
      //layoutParams.width = 80;
      layoutParams.height = 0;
      viewHolder.postImage.setLayoutParams(layoutParams);
    }

    viewHolder.ivRetweet.setImageResource(ResourceUtil.getRetweetIconResource(tweet.getRetweeted()));

    viewHolder.ivFavorate.setImageResource(ResourceUtil.getFavorateIconResource(tweet.getFavorited()));

    viewHolder.ivRetweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "-----------ivRetweet.onClick : " + tweet.getText());
        viewHolder.ivRetweet.setImageResource(ResourceUtil.getRetweetIconResource(!tweet.getRetweeted()));
        viewHolder.retweetNum.setText(String.valueOf(!tweet.getRetweeted() ? tweet.getRetweet_count() + 1 : tweet.getRetweet_count() - 1));

        twitterClient.reTweet(tweet.getId(), new ApiCallback<Tweet>() {
          @Override
          public void success(Tweet newTweet) {
            tweet.copyFrom(newTweet.getRetweeted_status());
            newTweetListener.onNewTweet(newTweet);
          }

          @Override
          public void failure(String error) {
            viewHolder.ivRetweet.setImageResource(ResourceUtil.getRetweetIconResource(tweet.getRetweeted()));
            viewHolder.retweetNum.setText(String.valueOf(tweet.getRetweet_count()));
          }
        });
      }
    });

    viewHolder.ivFavorate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "-----------ivFavorate.onClick : " + tweet.getText());
        // Change first, then revert in failure
        viewHolder.ivFavorate.setImageResource(ResourceUtil.getFavorateIconResource(!tweet.getFavorited()));
        viewHolder.favorateNum.setText(String.valueOf(!tweet.getFavorited() ? tweet.getFavorite_count() + 1 : tweet.getFavorite_count() - 1));

        twitterClient.favorite(tweet.getId(), !tweet.getFavorited(), new ApiCallback<Tweet>() {
          @Override
          public void success(Tweet newTweet) {
            tweet.copyFrom(newTweet);
          }

          @Override
          public void failure(String error) {
            viewHolder.ivFavorate.setImageResource(ResourceUtil.getFavorateIconResource(tweet.getFavorited()));
            viewHolder.favorateNum.setText(String.valueOf(tweet.getFavorite_count()));
          }
        });
      }
    });

    viewHolder.ivReply.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fm = ((TimelineActivity) getContext()).getSupportFragmentManager();
        ComposeDialogFragment dialog = ComposeDialogFragment.newInstance(tweet.getId(), tweet.getUser().getScreen_name());
        dialog.show(fm, "Reply a tweet");
      }
    });

    //viewHolder.likes.setText(Utils.formatInt(photo.getLikes().getCount()) + " likes");

    //viewHolder.commentNum.setText("View all " + Utils.formatInt(photo.getComments().getCount()) + " comments");

    return convertView;
  }
}
