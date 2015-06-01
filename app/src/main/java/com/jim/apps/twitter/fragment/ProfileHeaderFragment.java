/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jim.apps.twitter.R;
import com.jim.apps.twitter.models.User;
import com.jim.apps.twitter.util.CommonUtil;

public class ProfileHeaderFragment extends Fragment {

  //private String username;
  //private String screenName;
  //private String profileImageUrl;
  private User user;

  @InjectView(R.id.tvUserName)
  TextView tvUsername;

  @InjectView(R.id.tvScreenName)
  TextView tvScreenName;

  @InjectView(R.id.ivProfile) SimpleDraweeView ivProfileImage;

  @InjectView(R.id.ivProfileBackgroud) SimpleDraweeView ivProfileBackground;

  @InjectView(R.id.appbar) AppBarLayout appbar;

  @InjectView(R.id.toolbar) Toolbar toolbar;

  //public static ProfileHeaderFragment newInstance(String username, String screenName, String profileImageUrl) {
  //  ProfileHeaderFragment fragment = new ProfileHeaderFragment();
  //  Bundle args = new Bundle();
  //  args.putString("username", username);
  //  args.putString("screenName", screenName);
  //  args.putString("profileImageUrl", profileImageUrl);
  //  fragment.setArguments(args);
  //  return fragment;
  //}

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //username = getArguments().getString("username");
    //screenName = getArguments().getString("screenName");
    //profileImageUrl = getArguments().getString("profileImageUrl");
  }

  public void setUser(User user) {
    if(null != user) {
      tvUsername.setText(user.getName());
      tvScreenName.setText(user.getScreen_name());
      ivProfileImage.setImageURI(Uri.parse(user.getProfile_image_url()));
      if(null != user.getProfile_background_image_url()) {
        SimpleDraweeView draweeView = new SimpleDraweeView(getActivity());
        draweeView.setScaleType(ImageView.ScaleType.FIT_XY);
        draweeView.setImageURI(Uri.parse(user.getProfile_background_image_url()));
        //appbar.setBackground(draweeView.getDrawable());

        ivProfileBackground.setImageURI(Uri.parse(user.getProfile_background_image_url()));
      } else if(null != user.getProfile_background_color() && user.getProfile_background_color().length() > 1) {
        appbar.setBackgroundColor(Color.parseColor(user.getProfile_background_color()));
      }

      //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ivProfileImage.getLayoutParams();
      //params.topMargin = CommonUtil.dpToPixel(getActivity(), toolbar.getLayoutParams().height + 30);
      //ivProfileImage.setLayoutParams(params);
    }
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile_header, container, false);

    ButterKnife.inject(this, view);

    return view;
  }
}
