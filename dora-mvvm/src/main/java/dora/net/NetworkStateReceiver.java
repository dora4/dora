/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private static volatile BroadcastReceiver mBroadcastReceiver;

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
