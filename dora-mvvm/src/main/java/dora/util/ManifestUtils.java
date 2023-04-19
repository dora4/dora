package dora.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.HashSet;
import java.util.Set;

/**
 * 清单文件相关信息读取工具。
 */
public final class ManifestUtils {

    private static ManifestUtils sInstance;

    private ManifestUtils() {
    }

    private static ManifestUtils getInstance() {
        if (sInstance == null) {
            synchronized (ManifestUtils.class) {
                if (sInstance == null) sInstance = new ManifestUtils();
            }
        }
        return sInstance;
    }

    private String _getApplicationMetadataValue(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return (String) appInfo.metaData.get(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getApplicationMetadataValue(Context context, String name) {
        return getInstance()._getApplicationMetadataValue(context, name);
    }


    private Set<String> _getApplicationMetadataKeyWhileValueEquals(Context context, String value) {
        Set<String> keySet = new HashSet<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (value.equals(appInfo.metaData.get(key))) {
                        keySet.add(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return keySet;
    }

    public static Set<String> getApplicationMetadataKeyWhileValueEquals(Context context, String value) {
        return getInstance()._getApplicationMetadataKeyWhileValueEquals(context, value);
    }
}
