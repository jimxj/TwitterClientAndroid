package com.jim.apps.twitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jim.apps.twitter.ComposeDialogFragment;
import com.jim.apps.twitter.OnNewTweetListener;
import com.jim.apps.twitter.R;
import com.jim.apps.twitter.TwitterApplication;
import com.jim.apps.twitter.api.ApiCallback;
import com.jim.apps.twitter.api.TwitterClient;
import com.jim.apps.twitter.fragment.timeline.HomeTweetListFragment;
import com.jim.apps.twitter.fragment.timeline.MentionsTweetListFragment;
import com.jim.apps.twitter.fragment.timeline.AbstractTweetListFragment;
import com.jim.apps.twitter.models.Tweet;
import java.util.ArrayList;
import java.util.List;
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit.client.Response;

public class TimelineActivity extends AppCompatActivity
                              implements OnNewTweetListener {
  private static final String TAG = "TimelineActivity";

  @InjectView(R.id.toolbar)
  Toolbar toolbar;

  @InjectView(R.id.fab) FloatingActionButton fab;

  @InjectView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;

  Adapter fragmentPagerAdapter;
  ViewPager viewPager;

  protected TwitterClient twitterClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_timeline);

    ButterKnife.inject(this);

    twitterClient = TwitterApplication.getTwitterClient();

    setSupportActionBar(toolbar);

    final ActionBar ab = getSupportActionBar();
    ab.setHomeAsUpIndicator(R.drawable.ic_menu);
    ab.setDisplayHomeAsUpEnabled(true);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fm = TimelineActivity.this.getSupportFragmentManager();
        ComposeDialogFragment dialog = ComposeDialogFragment.newInstance(null, null);
        dialog.show(fm, "Compose a new tweet");
      }
    });

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      setupDrawerContent(navigationView);
    }

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    if (viewPager != null) {
      setupViewPager(viewPager);
    }

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
  }

  private void setupViewPager(ViewPager viewPager) {
    fragmentPagerAdapter = new Adapter(getSupportFragmentManager());
    fragmentPagerAdapter.addFragment(new HomeTweetListFragment(), "Home");
    fragmentPagerAdapter.addFragment(new MentionsTweetListFragment(), "@Mentions");
    viewPager.setAdapter(fragmentPagerAdapter);
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            return true;
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;

      case R.id.action_settings:
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == TweetDetailsActivity.REQUEST_CODE) {
      int status = data.getIntExtra("status", 0);
      if((status & TweetDetailsActivity.STATUS_UPDATED) == TweetDetailsActivity.STATUS_UPDATED) {
        int index = data.getIntExtra("index", -1);
        Tweet updatedTweet = (Tweet) data.getSerializableExtra("tweet");
        if(index >= 0 && null != updatedTweet) {
          getCurrentFragement().update(index, updatedTweet);
        }
      }

      if((status & TweetDetailsActivity.STATUS_ADDED) == TweetDetailsActivity.STATUS_ADDED) {
        //fetchTweets(true);
        getCurrentFragement().setNeedRefresh(true);
      }
    }
  }

  private AbstractTweetListFragment getCurrentFragement() {
    return ((AbstractTweetListFragment) fragmentPagerAdapter.getItem(viewPager.getCurrentItem()));
  }

  private void setupActionBar() {
    //setSupportActionBar(toolbar);
    //toolbar.setLogo(R.drawable.ic_launcher);
    toolbar.setNavigationIcon(R.drawable.twitter_bird);
//    ActionBar actionBar = getSupportActionBar();
    //getSupportActionBar().setHomeButtonEnabled(true);
    //getSupportActionBar().setDisplayShowHomeEnabled(true);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    //getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    actionBar.setLogo(R.drawable.ic_launcher);
//    actionBar.setDisplayUseLogoEnabled(true);
////    actionBar.setTitle(null);

//    toolbar.setNavigationIcon(R.drawable.ic_launcher);
    //toolbar.setTitle("Title");
    //toolbar.setSubtitle("Sub");

    //toolbar.setLogo(R.drawable.ic_launcher);
  }

  @Override
  public void onNewTweet(String text) {
    Log.d(TAG, "-----------onNewTweet : " + text);
    twitterClient.newTweet(text, null, new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet tweet) {
        onNewTweet(tweet);
      }

      @Override
      public void failure(String error) {

      }
    });
  }

  @Override
  public void onNewTweet(Tweet tweet) {
    getCurrentFragement().addLocalTweet(tweet);
  }

  @Override
  public void onReplyTweet(Long inReplyId, String text) {
    Log.d(TAG, "-----------onReplyTweet : " + text + ", inReplyId = " + inReplyId);
    twitterClient.newTweet(text, inReplyId, new ApiCallback<Tweet>() {
      @Override
      public void success(Tweet tweet) {
        onNewTweet(tweet);
      }

      @Override
      public void failure(String error) {

      }
    });
  }

  static class Adapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public Adapter(FragmentManager fm) {
      super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
      mFragments.add(fragment);
      mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
      return mFragments.get(position);
    }

    @Override
    public int getCount() {
      return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mFragmentTitles.get(position);
    }
  }

}
