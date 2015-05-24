/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jim.apps.twitter.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jim.apps.twitter.Utils;
import com.jim.apps.twitter.models.Tweet;

import java.util.List;


public class TweetAdapter extends ArrayAdapter<Tweet> {

    public static final String TAG = "TweetListAdapter";
    // View lookup cache
    private static class ViewHolder {
      SimpleDraweeView authorImage;
      TextView userName;
      TextView screenName;
      TextView postTime;
      TextView text;
      TextView retweetNum;
      TextView favorateNum;
      //SimpleDraweeView postImage;
      //TextView likes;
      //TextView commentNum;
    }

    public TweetAdapter(Context context, List<Tweet> objects) {
      super(context, R.layout.item_tweet, objects);
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
        viewHolder.text = (TextView) convertView.findViewById(R.id.tvText);
        viewHolder.retweetNum = (TextView) convertView.findViewById(R.id.tvRetweetNum);
        viewHolder.favorateNum = (TextView) convertView.findViewById(R.id.tvFavorateNum);
        //viewHolder.postImage = (SimpleDraweeView) convertView.findViewById(R.id.ivPhoto);
        //viewHolder.likes = (TextView) convertView.findViewById(R.id.tvLikeNum);
        //viewHolder.commentNum = (TextView) convertView.findViewById(R.id.tvViewAllComments);

        convertView.setTag(viewHolder);
      }

      viewHolder.authorImage.setImageURI(Uri.parse(tweet.getUser().getProfile_image_url()));

      //viewHolder.postImage.setImageURI(Uri.parse(photo.getImages().getStandard_resolution().getUrl()));

      viewHolder.postTime.setText(Utils.getRelativeTimeAgo(tweet.getCreated_at()));

      viewHolder.userName.setText(tweet.getUser().getName());
      viewHolder.screenName.setText("@" + tweet.getUser().getScreen_name());
      viewHolder.text.setText(tweet.getText());
      viewHolder.retweetNum.setText(String.valueOf(tweet.getRetweet_count()));
      viewHolder.favorateNum.setText(String.valueOf(tweet.getFavorite_count()));
      //viewHolder.likes.setText(Utils.formatInt(photo.getLikes().getCount()) + " likes");

      //viewHolder.commentNum.setText("View all " + Utils.formatInt(photo.getComments().getCount()) + " comments");

      return convertView;
    }
}
