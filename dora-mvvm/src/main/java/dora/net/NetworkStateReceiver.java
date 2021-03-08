package dora.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import dora.log.Logger;
import dora.util.NetworkUtils;

public class NetworkStateReceiver extends BroadcastReceiver {

    public final static String ACTION_ANDROID_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public final static String ACTION_DORA_NETWORK_CHANGE = "dora.net.CONNECTIVITY_CHANGE";

    private static boolean mNetAvailable = false;
    private static NetworkUtils.ApnType mApnType;
    private static ArrayList<NetworkChangeObserver> mNetChangeObservers;
    private static BroadcastReceiver mBroadcastReceiver;

    private static BroadcastReceiver getReceiver() {
        if (mBroadcastReceiver == null) {
            synchronized (NetworkStateReceiver.class) {
                if (mBroadcastReceiver == null) {
                    mBroadcastReceiver = new NetworkStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = this;
        if (intent.getAction().equals(ACTION_ANDROID_NETWORK_CHANGE)
                || intent.getAction().equals(ACTION_DORA_NETWORK_CHANGE)) {
            if (!NetworkUtils.checkNetwork(context)) {
                Logger.i(this.getClass().getName() + ":network disconnected");
                mNetAvailable = false;
            } else {
                Logger.i(this.getClass().getName() + ":network connected");
                mNetAvailable = true;
                mApnType = NetworkUtils.getApnType(context);
            }
            notifyObserver();
        }
    }

    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ANDROID_NETWORK_CHANGE);
        filter.addAction(ACTION_DORA_NETWORK_CHANGE);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void checkNetworkState(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_DORA_NETWORK_CHANGE);
        context.sendBroadcast(intent);
    }

    public static void unregisterNetworkStateReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
        }
    }

    public static boolean isNetworkAvailable() {
        return mNetAvailable;
    }

    public static NetworkUtils.ApnType getApnType() {
        return mApnType;
    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetworkChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetworkConnect(mApnType);
                    } else {
                        observer.onNetworkDisconnect();
                    }
                }
            }
        }
    }

    public static void registerObserver(NetworkChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<>();
        }
        mNetChangeObservers.add(observer);
    }

    public static void unregisterObserver(NetworkChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }
}
