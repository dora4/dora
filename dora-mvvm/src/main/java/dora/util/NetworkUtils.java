package dora.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

public final class NetworkUtils {

    public enum ApnType {
        WIFI, CMNET, CMWAP, NONE
    }

    private NetworkUtils() {
    }

    public static boolean checkNetwork() {
        return checkNetwork(GlobalContext.get());
    }

    public static boolean checkNetwork(Context context) {
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
}
