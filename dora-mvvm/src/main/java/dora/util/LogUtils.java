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

import android.util.Log;

import androidx.annotation.StringRes;

/**
 * The logging tool defaults to filtering tags as [dora-log].
 * 简体中文：日志工具，默认过滤标签为[dora-log]。
 */
public final class LogUtils {

    /**
     * The default log output tag.
     * 简体中文：默认的日志输出标签。
     */
    public static final String TAG = "dora-log";

    public static void i(String msg) {
        itag(TAG, msg);
    }

    public static void i(@StringRes int resId) {
        itag(TAG, GlobalContext.get().getString(resId));
    }

    public static void itag(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void iformat(String tag, String format, Object... values) {
        itag(tag, String.format(format, values));
    }

    public static void e(String msg) {
        etag(TAG, msg);
    }

    public static void e(@StringRes int resId) {
        etag(TAG, GlobalContext.get().getString(resId));
    }

    public static void e(Exception e) {
        etag(TAG, e.toString());
    }

    public static void etag(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void eformat(String tag, String format, Object... values) {
        etag(tag, String.format(format, values));
    }

    public static void d(String msg) {
        dtag(TAG, msg);
    }

    public static void d(@StringRes int resId) {
        dtag(TAG, GlobalContext.get().getString(resId));
    }

    public static void dtag(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void dformat(String tag, String format, Object... values) {
        dtag(tag, String.format(format, values));
    }

    public static void w(String msg) {
        wtag(TAG, msg);
    }

    public static void w(@StringRes int resId) {
        wtag(TAG, GlobalContext.get().getString(resId));
    }

    public static void wtag(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void wformat(String tag, String format, Object... values) {
        wtag(tag, String.format(format, values));
    }

    public static void v(String msg) {
        vtag(TAG, msg);
    }

    public static void v(@StringRes int resId) {
        vtag(TAG, GlobalContext.get().getString(resId));
    }
    public static void vtag(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void vformat(String tag, String format, Object... values) {
        vtag(tag, String.format(format, values));
    }
}