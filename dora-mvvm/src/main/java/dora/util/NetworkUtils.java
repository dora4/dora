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

    public static boolean checkNetwork(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean checkNetwork() {
        return checkNetwork(GlobalContext.get());
    }

    public static boolean isWifiConnected() {
        NetworkInfo networkInfo = getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return false;
    }

    public static boolean isMobileConnected() {
        NetworkInfo networkInfo = getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(int networkType) {
        ConnectivityManager connectivityManager = (ConnectivityManager) GlobalContext.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(networkType);
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static ApnType getApnType() {
        ConnectivityManager connectivityManager = ServiceUtils.getConnectivityManager();
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
