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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Checklist File-Related Information Reading Tool.
 * 简体中文：清单文件相关信息读取工具。
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
