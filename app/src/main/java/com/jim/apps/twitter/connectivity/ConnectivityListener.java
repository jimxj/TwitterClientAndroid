package com.jim.apps.twitter.connectivity;

public interface ConnectivityListener {
  void onConnectivityStatusChanged(int lastKnowStatus, int newStatus);
}
