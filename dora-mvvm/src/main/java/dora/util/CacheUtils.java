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

/**
 * Used for calculating and clearing app cache.
 * 简体中文：用于计算和清除应用缓存。
 */
public final class CacheUtils {

    private CacheUtils() {
    }

    public static String getCacheSize() {
        return getCacheSize(GlobalContext.get());
    }

    public static String getCacheSize(Context context) {
        long cacheSize = IoUtils.getFolderTotalSize(context.getCacheDir());
        if (IoUtils.checkMediaMounted()) {
            cacheSize += IoUtils.getFolderTotalSize(context.getExternalCacheDir());
        }
        return IoUtils.formatFileSize(cacheSize);
    }

    public static void clearAllCaches() {
        clearAllCaches(GlobalContext.get());
    }

    public static void clearAllCaches(Context context) {
        IoUtils.delete(context.getCacheDir());
        if (IoUtils.checkMediaMounted()) {
            IoUtils.delete(context.getExternalCacheDir());
        }
    }
}