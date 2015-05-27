package com.jim.apps.twitter.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(final Context context, final Intent intent) {
    ConnectivityManager.getInstance().getConnectivityStatus();
  }
}
