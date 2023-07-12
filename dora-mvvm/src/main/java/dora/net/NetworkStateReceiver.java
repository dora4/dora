package dora.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import dora.util.LogUtils;
import dora.util.NetUtils;

public class NetworkStateReceiver extends BroadcastReceiver {

    public final static String ACTION_ANDROID_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public final static String ACTION_DORA_NETWORK_CHANGE = "dora.net.CONNECTIVITY_CHANGE";

    private static boolean mNetAvailable = false;
    private static NetUtils.ApnType mApnType;
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
            if (!NetUtils.checkNetworkAvailable(context)) {
                LogUtils.i(this.getClass().getName() + ":network disconnected");
                mNetAvailable = false;
            } else {
                LogUtils.i(this.getClass().getName() + ":network connected");
                mNetAvailable = true;
                mApnType = NetUtils.getApnType(context);
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

    public static NetUtils.ApnType getApnType() {
        return mApnType;
    }

    private void notifyObserver() {
        if (mNetChangeObservers == null) {
            return;
        }
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
            mNetChangeObservers.remove(observer);
        }
    }
}
