package com.jim.apps.twitter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jim.apps.twitter.OnNewTweetListener;
import com.jim.apps.twitter.R;
import com.jim.apps.twitter.fragment.ProfileHeaderFragment;
import com.jim.apps.twitter.fragment.timeline.UserTweetListFragment;
import com.jim.apps.twitter.models.Tweet;
import com.jim.apps.twitter.models.User;
import com.jim.apps.twitter.util.CommonUtil;

public class UserProfileActivity extends AppCompatActivity implements OnNewTweetListener {

  @InjectView(R.id.flUserTweets) FrameLayout flUserTweets;
  @InjectView(R.id.appbar) AppBarLayout appbar;
  @InjectView(R.id.toolbar) Toolbar toolbar;
  @InjectView(R.id.ivProfile) SimpleDraweeView ivProfileImage;
  @InjectView(R.id.tvUserName) TextView tvUsername;
  @InjectView(R.id.tvScreenName) TextView tvScreenName;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);

    ButterKnife.inject(this);

    setupActionBar();

    User user = (User) getIntent().getSerializableExtra("user");
    //if (savedInstanceState == null) {
    //  ProfileHeaderFragment headerFragment =
    //      (ProfileHeaderFragment) getSupportFragmentManager().findFragmentById(R.id.fmProfileHeader);
    //  headerFragment.setUser(user);
    //}
    tvUsername.setText(user.getName());
    tvScreenName.setText("@" + user.getScreen_name());
    ivProfileImage.setImageURI(Uri.parse(user.getProfile_image_url()));
    if(user.getProfile_use_background_image() && null != user.getProfile_background_image_url()) {
      SimpleDraweeView draweeView = new SimpleDraweeView(this);
      draweeView.setScaleType(ImageView.ScaleType.FIT_XY);
      draweeView.setImageURI(Uri.parse(user.getProfile_background_image_url()));
      appbar.setBackground(draweeView.getDrawable());

      //ivProfileBackground.setImageURI(Uri.parse(user.getProfile_background_image_url()));
    } else if(null != user.getProfile_background_color() && user.getProfile_background_color().length() > 1) {
      appbar.setBackgroundColor(Color.parseColor(user.getProfile_background_color()));
    }

    //ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ivProfileImage.getLayoutParams();
    //params.topMargin = CommonUtil.pixelToDp(this, toolbar.getLayoutParams().height);
    //ivProfileImage.setLayoutParams(params);

    // Begin the transaction
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    // Replace the contents of the container with the new fragment
    UserTweetListFragment userTweetListFragment = new UserTweetListFragment();
    userTweetListFragment.setScreenName(user.getScreen_name());
    ft.replace(R.id.flUserTweets, userTweetListFragment);
    // or ft.add(R.id.your_placeholder, new FooFragment());
    // Complete the changes added above
    ft.commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_user_profile, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void setupActionBar() {
    toolbar.setNavigationIcon(R.drawable.back_arrow);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    toolbar.setLogo(R.drawable.twitter_bird);
  }


  @Override public void onNewTweet(String text) {

  }

  @Override public void onNewTweet(Tweet tweet) {

  }

  @Override public void onReplyTweet(Long inReplyId, String text) {

  }
}
