package com.jim.apps.twitter.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jim.apps.twitter.ComposeDialogFragment;
import com.jim.apps.twitter.OnNewTweetListener;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.util.DateUtil;
import com.jim.apps.twitter.R;
import com.jim.apps.twitter.models.Tweet;
import com.jim.apps.twitter.util.ResourceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TweetDetailsActivity extends ActionBarActivity implements OnNewTweetListener {
  public static final int REQUEST_CODE = 110;

  public static final String TAG = "TweetDetails";

  public static final int STATUS_NO_CHANGE = 0;
  public static final int STATUS_UPDATED = 1;
  public static final int STATUS_ADDED = 2;

  private int status = STATUS_NO_CHANGE;

  private Tweet tweet;
  private int index;

  TwitterClient twitterClient = TwitterApplication.getTwitterClient();

  @InjectView(R.id.tbTweetDetail)
  Toolbar toolbar;

  @InjectView(R.id.ivDetailProfileImage)
  SimpleDraweeView ivProfileImage;

  @InjectView(R.id.tvUserName)
  TextView tvUserName;

  @InjectView(R.id.tvScreenName)
  TextView tvScreenName;

  @InjectView(R.id.tvText)
  TextView tvText;

  @InjectView(R.id.tvTime)
  TextView tvTime;

  @InjectView(R.id.ivPhoto)
  SimpleDraweeView ivPhoto;

  @InjectView(R.id.tvFavorateNum)
  TextView tvFavoriteNum;

  @InjectView(R.id.tvRetweetNum)
  TextView tvRetweetNum;

  @InjectView(R.id.ivRetweet)
  ImageView ivRetweet;

  @InjectView(R.id.ivFavorate)
  ImageView ivFavorate;

  @InjectView(R.id.ivShare)
  ImageView ivShare;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tweet_details);

    ButterKnife.inject(this);

    setupActionBar();

    tweet = (Tweet) getIntent().getExtras().get("tweet");
    index = getIntent().getIntExtra("index", -1);
    if(null != tweet) {
      ivProfileImage.setImageURI(Uri.parse(tweet.getUser().getProfile_image_url()));
      tvUserName.setText(tweet.getUser().getName());
      tvScreenName.setText("@" + tweet.getUser().getScreen_name());
      tvText.setText(tweet.getText());
      tvTime.setText(DateUtil.formatDate(tweet.getCreated_at()));

      String mediaUrl = tweet.getFirstPhotoMediaUrl();
      if(null != mediaUrl) {
        ivPhoto.setImageURI(Uri.parse(mediaUrl));
        ivPhoto.setVisibility(View.VISIBLE);

        android.view.ViewGroup.LayoutParams layoutParams = ivPhoto.getLayoutParams();
        //layoutParams.width = 80;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ivPhoto.setLayoutParams(layoutParams);
      } else {
        ivPhoto.setVisibility(View.INVISIBLE);

        android.view.ViewGroup.LayoutParams layoutParams = ivPhoto.getLayoutParams();
        //layoutParams.width = 80;
        layoutParams.height = 0;
        ivPhoto.setLayoutParams(layoutParams);
      }

      tvRetweetNum.setText(String.valueOf(tweet.getRetweet_count()));
      tvFavoriteNum.setText(String.valueOf(tweet.getFavorite_count()));

      ivRetweet.setImageResource(ResourceUtil.getRetweetIconResource(tweet.getRetweeted()));
      ivFavorate.setImageResource(ResourceUtil.getFavorateIconResource(tweet.getFavorited()));
    }
  }

  @OnClick(R.id.ivRetweet)
  public void onRetweetClick() {
    Log.d(TAG, "-----------ivRetweet.onClick : " + tweet.getText());
    ivRetweet.setImageResource(ResourceUtil.getRetweetIconResource(!tweet.getRetweeted()));
    tvRetweetNum.setText(String.valueOf(!tweet.getRetweeted() ? tweet.getRetweet_count() + 1 : tweet.getRetweet_count() - 1));

    twitterClient.reTweet(tweet.getId(), new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet newTweet) {
        tweet.copyFrom(newTweet.getRetweeted_status());
        status = status | STATUS_ADDED | STATUS_UPDATED;
      }

      @Override
      public void failure(String error) {
        ivRetweet.setImageResource(ResourceUtil.getRetweetIconResource(tweet.getRetweeted()));
        tvRetweetNum.setText(String.valueOf(tweet.getRetweet_count()));
      }
    });
  }

  @OnClick(R.id.ivFavorate)
  public void onFavorateClick() {
    Log.d(TAG, "-----------ivFavorate.onClick : " + tweet.getText());
    // Change first, then revert in failure
    ivFavorate.setImageResource(ResourceUtil.getFavorateIconResource(!tweet.getFavorited()));
    tvFavoriteNum.setText(String.valueOf(!tweet.getFavorited() ? tweet.getFavorite_count() + 1 : tweet.getFavorite_count() - 1));

    twitterClient.favorite(tweet.getId(), !tweet.getFavorited(), new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet newTweet) {
        tweet.copyFrom(newTweet);
        status = status | STATUS_UPDATED;
      }

      @Override
      public void failure(String error) {
        ivFavorate.setImageResource(ResourceUtil.getFavorateIconResource(tweet.getFavorited()));
        tvFavoriteNum.setText(String.valueOf(tweet.getFavorite_count()));
      }
    });
  }

  @OnClick(R.id.ivReply)
  public void onReplyClick() {
    FragmentManager fm = this.getSupportFragmentManager();
    ComposeDialogFragment dialog = ComposeDialogFragment.newInstance(tweet.getId(), tweet.getUser().getScreen_name());
    dialog.show(fm, "Reply a tweet");
  }

  @OnClick(R.id.ivShare)
  public void onShareClick() {
    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("text/html");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>\"" + tweet.getText() + "\" from "
            + tweet.getUser().getName() + "(@" + tweet.getUser().getScreen_name() + ")"
            + "</p>"));
    startActivity(Intent.createChooser(sharingIntent, "Share a tweet"));
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK ) {
      onExit();
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_tweet_details, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
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

  private void onExit() {
    Intent intent=new Intent();
    intent.putExtra("status", status);
    intent.putExtra("index", index);
    if((status & STATUS_UPDATED) == STATUS_UPDATED) {
      intent.putExtra("tweet", tweet);
    }
    setResult(REQUEST_CODE, intent);
    finish();
  }

  private void setupActionBar() {
    toolbar.setNavigationIcon(R.drawable.back_arrow);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onExit();
      }
    });

    toolbar.setLogo(R.drawable.twitter_bird);
  }

  @Override
  public void onNewTweet(String text) {

  }

  @Override
  public void onNewTweet(Tweet tweet) {

  }

  @Override
  public void onReplyTweet(Long inReplyId, String text) {
    Log.d(TAG, "-----------onReplyTweet : " + text + ", inReplyId = " + inReplyId);
    twitterClient.newTweet(text, inReplyId, new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet tweet) {
        status = status | STATUS_ADDED | STATUS_UPDATED;
      }

      @Override
      public void failure(String error) {
        Log.e(TAG, "-----------onReplyTweet failed : " + error);
      }
    });
  }
}
