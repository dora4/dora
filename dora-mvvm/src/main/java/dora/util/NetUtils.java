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

package dora.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.util.Locale;

/**
 * Network-related tools.
 * 简体中文：网络相关工具。
 */
public final class NetUtils {

    public enum ApnType {
        WIFI, CMNET, CMWAP, NONE
    }

    private NetUtils() {
    }

    public static boolean checkNetworkAvailable() {
        return checkNetworkAvailable(GlobalContext.get());
    }

    public static boolean checkNetworkAvailable(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifiConnected() {
        return isWifiConnected(GlobalContext.get());
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return false;
    }

    public static boolean isMobileConnected() {
        return isMobileConnected(GlobalContext.get());
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context, int networkType) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(networkType);
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static ApnType getApnType() {
        return getApnType(GlobalContext.get());
    }

    public static ApnType getApnType(Context context) {
        ConnectivityManager connectivityManager = ServiceUtils.getConnectivityManager(context);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return ApnType.NONE;
        }
        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
                return ApnType.CMNET;
            } else {
                return ApnType.CMWAP;
            }
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return ApnType.WIFI;
        }
        return ApnType.NONE;
    }

    /**
     * Check whether the system is using an HTTP proxy.
     * 简体中文：判断是否使用了代理。
     */
    public static boolean isUsingProxy() {
        // Get system proxy host and port
        // 简体中文：获取系统代理地址和端口
        String proxyAddress = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");

        // Return true if both proxy host and port are set
        // 简体中文如果代理地址和端口都不为空，则说明使用了代理
        return (proxyAddress != null && proxyPort != null);
    }

    /**
     * Check whether the device is connected to a VPN.
     * 简体中文：判断是否连接了 VPN。
     */
    public static boolean isUsingVPN(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            Network[] networks = cm.getAllNetworks();

            for (Network network : networks) {
                NetworkCapabilities caps = cm.getNetworkCapabilities(network);

                // Check if the network has VPN transport capability
                // 简体中文：检查是否包含 VPN 类型的传输方式
                if (caps != null && caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    return true;
                }
            }
        }
        return false;
    }

}
