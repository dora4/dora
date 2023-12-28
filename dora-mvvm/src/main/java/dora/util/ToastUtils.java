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
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Toast tools.
 * 简体中文：吐司工具。
 */
public final class ToastUtils {

    private static Toast mToast;

    private ToastUtils() {
    }

    public static void showShort(String msg) {
        showShort(GlobalContext.get(), msg);
    }

    public static void showShort(@StringRes int resId) {
        showShort(GlobalContext.get().getString(resId));
    }

    public static void showShort(@StringRes int resId, Object... args) {
        showShort(String.format(GlobalContext.get().getString(resId), args));
    }

    public static void showShort(final Context context, final String msg) {
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showShortInternal(context, msg);
                }
            });
        } else {
            showShortInternal(context, msg);
        }
    }

    public static void showLong(String msg) {
        showLong(GlobalContext.get(), msg);
    }

    public static void showLong(@StringRes int resId) {
        showLong(GlobalContext.get().getString(resId));
    }

    public static void showLong(@StringRes int resId, Object... args) {
        showLong(String.format(GlobalContext.get().getString(resId), args));
    }

    public static void showLong(final Context context, final String msg) {
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showLongInternal(context, msg);
                }
            });
        } else {
            showLongInternal(context, msg);
        }
    }

    private static void showShortInternal(Context context, String msg) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }

            }
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    private static void showLongInternal(Context context, String msg) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }
            }
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
