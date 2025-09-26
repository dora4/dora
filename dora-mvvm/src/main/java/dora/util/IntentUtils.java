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

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * It is used to connect various components within Android and facilitate data transfer between
 * components.
 * 简体中文：用于连接Android各大组件以及组件直接的传参。
 */
public final class IntentUtils {

    private IntentUtils() {
    }

    public static boolean hasExtra(@NonNull Intent intent, @NonNull String key) {
        return intent.hasExtra(key);
    }

    public static boolean hasExtras(@NonNull Intent intent, @NonNull String[] keys) {
        int length = keys.length;
        int count = 0;
        for (String key : keys) {
            if (hasExtra(intent, key)) {
                count++;
            }
        }
        return count == length;
    }

    public static Intent getActivityIntent(@NonNull Context context,
                                           @NonNull Class<? extends Activity> activityClazz) {
        return new Intent(context, activityClazz);
    }

    public static Intent getActivityIntent(@NonNull Class<? extends Activity> activityClazz) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        return new Intent(topActivity, activityClazz);
    }

    public static void startActivity(@NonNull Context context,
                                     @NonNull Class<? extends Activity> activityClazz) {
        context.startActivity(getActivityIntent(context, activityClazz));
    }

    public static void startActivity(@NonNull Context context,
                                     @NonNull Class<? extends Activity> activityClazz,
                                     @Nullable String action) {
        Intent intent = getActivityIntent(context, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        context.startActivity(intent);
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz,
                                     @Nullable String action) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivity(topActivity, activityClazz, action);
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivity(topActivity, activityClazz);
    }

    public static void startActivityForResult(@NonNull Activity activity,
                                              @NonNull Class<? extends Activity> activityClazz,
                                              @Nullable String action,
                                              int requestCode) {
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(@NonNull Activity activity,
                                              @NonNull Class<? extends
                                                      Activity> activityClazz, int requestCode) {
        startActivityForResult(activity, activityClazz, null, requestCode);
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity> activityClazz,
                                              @Nullable String action,
                                              int requestCode) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivityForResult(topActivity, activityClazz, action, requestCode);
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity> activityClazz,
                                              int requestCode) {
        startActivityForResult(activityClazz, null, requestCode);
    }

    public static void startActivity(@NonNull Context context,
                                     @NonNull Class<? extends Activity> activityClazz,
                                     @Nullable String action, Extras extras) {
        Intent intent = getActivityIntent(context, activityClazz);
        intent = extras.parseData(intent);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        context.startActivity(intent);
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz,
                                     @Nullable String action,
                                     @NonNull Extras extras) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivity(topActivity, activityClazz, action, extras);
    }

    public static void startActivityForResult(@NonNull Activity activity,
                                              @NonNull Class<? extends Activity> activityClazz,
                                              @Nullable String action,
                                              @NonNull Extras extras,
                                              int requestCode) {
        Intent intent = getActivityIntent(activity, activityClazz);
        intent = extras.parseData(intent);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity> activityClazz,
                                              @Nullable String action,
                                              @NonNull Extras extras,
                                              int requestCode) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivityForResult(topActivity, activityClazz, action, extras, requestCode);
    }

    public static void startActivityWithString(@NonNull Activity activity,
                                               @NonNull Class<? extends Activity> activityClazz,
                                               @NonNull String name,
                                               @NonNull String extra) {
        startActivityWithString(activity, activityClazz, null, name, extra);
    }

    public static void startActivityWithInteger(@NonNull Activity activity,
                                                @NonNull Class<? extends Activity> activityClazz,
                                                @NonNull String name,
                                                int extra) {
        startActivityWithInteger(activity, activityClazz, null, name, extra);
    }

    public static void startActivityWithLong(@NonNull Activity activity,
                                             @NonNull Class<? extends Activity> activityClazz,
                                             @NonNull String name,
                                             long extra) {
        startActivityWithLong(activity, activityClazz, null, name, extra);
    }

    public static void startActivityWithBoolean(@NonNull Activity activity,
                                                @NonNull Class<? extends Activity> activityClazz,
                                                @NonNull String name,
                                                boolean extra) {
        startActivityWithBoolean(activity, activityClazz, null, name, extra);
    }

    public static void startActivityWithSerializable(@NonNull Activity activity,
                                                     @NonNull Class<? extends Activity> activityClazz,
                                                     @NonNull String name,
                                                     @NonNull Serializable extra) {
        startActivityWithSerializable(activity, activityClazz, null, name, extra);
    }

    public static void startActivityForResultWithString(@NonNull Activity activity,
                                                        @NonNull Class<? extends Activity> activityClazz,
                                                        int requestCode,
                                                        @NonNull String name,
                                                        @NonNull String extra) {
        startActivityForResultWithString(activity, activityClazz, null, requestCode, name, extra);
    }

    public static void startActivityForResultWithInteger(@NonNull Activity activity,
                                                         @NonNull Class<? extends Activity> activityClazz,
                                                         int requestCode,
                                                         @NonNull String name,
                                                         int extra) {
        startActivityForResultWithInteger(activity, activityClazz, null, requestCode, name, extra);
    }

    public static void startActivityForResultWithBoolean(@NonNull Activity activity,
                                                         @NonNull Class<? extends Activity> activityClazz,
                                                         int requestCode,
                                                         @NonNull String name,
                                                         boolean extra) {
        startActivityForResultWithBoolean(activity, activityClazz, null, requestCode, name, extra);
    }

    public static void startActivityForResultWithSerializable(@NonNull Activity activity,
                                                              @NonNull Class<? extends Activity> activityClazz,
                                                              int requestCode,
                                                              @NonNull String name,
                                                              @NonNull Serializable extra) {
        startActivityForResultWithSerializable(activity, activityClazz, null, requestCode, name, extra);
    }

    public static void startActivityWithString(@NonNull Activity activity,
                                               @NonNull Class<? extends Activity> activityClazz,
                                               @Nullable String action,
                                               @NonNull String name,
                                               @NonNull String extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithInteger(@NonNull Activity activity,
                                                @NonNull Class<? extends Activity> activityClazz,
                                                @Nullable String action,
                                                @NonNull String name,
                                                int extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithLong(@NonNull Activity activity,
                                             @NonNull Class<? extends Activity> activityClazz,
                                             @Nullable String action,
                                             @NonNull String name,
                                             long extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithBoolean(@NonNull Activity activity,
                                                @NonNull Class<? extends Activity> activityClazz,
                                                @Nullable String action,
                                                @NonNull String name,
                                                boolean extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithSerializable(@NonNull Activity activity,
                                                     @NonNull Class<? extends Activity> activityClazz,
                                                     @Nullable String action,
                                                     @NonNull String name,
                                                     @NonNull Serializable extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityForResultWithString(@NonNull Activity activity,
                                                        @NonNull Class<? extends Activity> activityClazz,
                                                        @Nullable String action,
                                                        int requestCode,
                                                        @NonNull String name,
                                                        @NonNull String extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(Extras.fromMap(map).parseData(intent), requestCode);
    }

    public static void startActivityForResultWithInteger(@NonNull Activity activity,
                                                         @NonNull Class<? extends Activity> activityClazz,
                                                         @Nullable String action,
                                                         int requestCode,
                                                         @NonNull String name, int extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(Extras.fromMap(map).parseData(intent), requestCode);
    }

    public static void startActivityForResultWithLong(@NonNull Activity activity,
                                                      @NonNull Class<? extends Activity> activityClazz,
                                                      @Nullable String action,
                                                      int requestCode,
                                                      @NonNull String name, long extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(Extras.fromMap(map).parseData(intent), requestCode);
    }

    public static void startActivityForResultWithBoolean(@NonNull Activity activity,
                                                         @NonNull Class<? extends Activity> activityClazz,
                                                         @Nullable String action,
                                                         int requestCode,
                                                         @NonNull String name,
                                                         boolean extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(Extras.fromMap(map).parseData(intent), requestCode);
    }

    public static void startActivityForResultWithSerializable(@NonNull Activity activity,
                                                              @NonNull Class<? extends Activity> activityClazz,
                                                              @Nullable String action,
                                                              int requestCode,
                                                              @NonNull String name,
                                                              @NonNull Serializable extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        if (TextUtils.isNotEmpty(action)) {
            intent.setAction(action);
        }
        activity.startActivityForResult(Extras.fromMap(map).parseData(intent), requestCode);
    }

    public static void startActivity(@NonNull Activity activity, Class<? extends Activity> activityClazz,
                                     @AnimRes int inAnim, @AnimRes int outAnim) {
        startActivity(activityClazz);
        activity.overridePendingTransition(inAnim, outAnim);
    }

    public static void finishActivity(@NonNull Activity activity, @AnimRes int inAnim, @AnimRes int outAnim) {
        activity.finish();
        activity.overridePendingTransition(inAnim, outAnim);
    }

    public static void startService(@NonNull String action) {
        startService(GlobalContext.get(), action);
    }

    public static void startService(@NonNull Context context, @NonNull String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.startService(intent);
    }

    public static void startService(@NonNull Class<? extends Service> serviceClazz) {
        startService(GlobalContext.get(), serviceClazz);
    }

    public static void startService(@NonNull Context context, @NonNull Class<? extends Service> serviceClazz) {
        Intent intent = new Intent();
        intent.setClass(context, serviceClazz);
        startService(context, intent);
    }

    public static void startService(@NonNull Intent intent) {
        startService(GlobalContext.get(), intent);
    }

    public static void startService(@NonNull Context context, @NonNull Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void sendBroadcast(@NonNull String action) {
        sendBroadcast(GlobalContext.get(), action);
    }

    public static void sendBroadcast(@NonNull Context context, @NonNull String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(@NonNull Class<? extends BroadcastReceiver> broadcastClazz) {
        sendBroadcast(GlobalContext.get(), broadcastClazz);
    }

    public static void sendBroadcast(@NonNull Context context, @NonNull Class<? extends BroadcastReceiver> broadcastClazz) {
        Intent intent = new Intent();
        intent.setClass(context, broadcastClazz);
        context.sendBroadcast(intent);
    }

    public static boolean getBooleanExtra(@NonNull Intent intent, @NonNull String name, boolean defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getBooleanExtra(name, defaultValue);
    }

    public static byte getByteExtra(@NonNull Intent intent, @NonNull String name) {
        return getByteExtra(intent, name, (byte) 0);
    }

    public static byte getByteExtra(@NonNull Intent intent, @NonNull String name, byte defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getByteExtra(name, defaultValue);
    }

    public static short getShortExtra(@NonNull Intent intent, @NonNull String name) {
        return getShortExtra(intent, name, (short) 0);
    }

    public static short getShortExtra(@NonNull Intent intent, @NonNull String name, short defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getShortExtra(name, defaultValue);
    }

    public static char getCharExtra(@NonNull Intent intent, @NonNull String name) {
        return getCharExtra(intent, name, '\n');
    }

    public static char getCharExtra(@NonNull Intent intent, @NonNull String name, char defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getCharExtra(name, defaultValue);
    }

    public static int getIntExtra(@NonNull Intent intent, @NonNull String name) {
        return getIntExtra(intent, name, 0);
    }

    public static int getIntExtra(@NonNull Intent intent, @NonNull String name, int defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getIntExtra(name, defaultValue);
    }

    public static long getLongExtra(@NonNull Intent intent, @NonNull String name) {
        return getLongExtra(intent, name, 0);
    }

    public static long getLongExtra(@NonNull Intent intent, @NonNull String name, long defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getLongExtra(name, defaultValue);
    }

    public static float getFloatExtra(@NonNull Intent intent, @NonNull String name) {
        return getFloatExtra(intent, name, 0);
    }

    public static float getFloatExtra(@NonNull Intent intent, @NonNull String name, float defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getFloatExtra(name, defaultValue);
    }

    public static double getDoubleExtra(@NonNull Intent intent, @NonNull String name) {
        return getDoubleExtra(intent, name, 0);
    }

    public static double getDoubleExtra(@NonNull Intent intent, @NonNull String name, double defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getDoubleExtra(name, defaultValue);
    }

    public static String getStringExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return "";
        }
        return intent.getStringExtra(name);
    }

    public static CharSequence getCharSequenceExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return "";
        }
        return intent.getCharSequenceExtra(name);
    }

    public static <T extends Parcelable> T getParcelableExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getParcelableExtra(name);
    }

    public static Parcelable[] getParcelableArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getParcelableArrayExtra(name);
    }

    public static Serializable getSerializableExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getSerializableExtra(name);
    }

    public static ArrayList<Integer> getIntegerArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return new ArrayList<>();
        }
        return intent.getIntegerArrayListExtra(name);
    }

    public static ArrayList<CharSequence> getCharSequenceArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return new ArrayList<>();
        }
        return intent.getCharSequenceArrayListExtra(name);
    }

    public static ArrayList<String> getStringArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return new ArrayList<>();
        }
        return intent.getStringArrayListExtra(name);
    }

    public static String[] getStringArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getStringArrayExtra(name);
    }

    public static boolean[] getBooleanArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getBooleanArrayExtra(name);
    }

    public static byte[] getByteArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getByteArrayExtra(name);
    }

    public static short[] getShortArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getShortArrayExtra(name);
    }

    public static char[] getCharArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getCharArrayExtra(name);
    }

    public static int[] getIntArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getIntArrayExtra(name);
    }

    public static long[] getLongArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getLongArrayExtra(name);
    }

    public static float[] getFloatArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getFloatArrayExtra(name);
    }

    public static double[] getDoubleArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getDoubleArrayExtra(name);
    }

    public static CharSequence[] getCharSequenceArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getCharSequenceArrayExtra(name);
    }

    public static Bundle getBundleExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getBundleExtra(name);
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getParcelableArrayListExtra(name);
    }

    public static void selectFile(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void shareText(@NonNull Context context, String title, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    /**
     * Return the intent of launch app details settings.
     * 简体中文：返回启动应用程序详细设置的意图。
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName) {
        return getLaunchAppDetailsSettingsIntent(pkgName, false);
    }

    /**
     * Return the intent of request "All files access" permission
     * for the specific application on Android 11 (API 30) and above.
     * 简体中文：返回请求特定应用“所有文件访问权限”的意图，
     * 仅在 Android 11 (API 30) 及以上版本有效。
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Intent getRequestStoragePermissionIntent(String packageName) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * Return the intent of launch app details settings.
     * 简体中文：返回启动应用程序详细设置的意图。
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName, final boolean isNewTask) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkgName));
        if (isNewTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public static class Extras implements Serializable {

        private static Extras extras;
        private final Map<String, Object> extrasMap;

        private Extras(Map<String, Object> map) {
            this.extrasMap = map;
        }

        public static Extras fromMap(Map<String, Object> map) {
            extras = new Extras(map);
            return extras;
        }

        public Bundle convertToBundle() {
            Bundle bundle = new Bundle();
            Set<String> keys = extrasMap.keySet();
            for (String key : keys) {
                Object value = extrasMap.get(key);
                if (value == null) {
                    continue;
                }
                if (String.class.isAssignableFrom(value.getClass())) {
                    String val = (String) value;
                    bundle.putString(key, val);
                } else if (int.class.isAssignableFrom(value.getClass())) {
                    int val = (int) value;
                    bundle.putInt(key, val);
                } else if (Serializable.class.isAssignableFrom(value.getClass())) {
                    Serializable val = (Serializable) value;
                    bundle.putSerializable(key, val);
                } else if (boolean.class.isAssignableFrom(value.getClass())) {
                    boolean val = (boolean) value;
                    bundle.putBoolean(key, val);
                } else if (long.class.isAssignableFrom(value.getClass())) {
                    long val = (long) value;
                    bundle.putLong(key, val);
                } else if (short.class.isAssignableFrom(value.getClass())) {
                    short val = (short) value;
                    bundle.putShort(key, val);
                } else if (byte.class.isAssignableFrom(value.getClass())) {
                    byte val = (byte) value;
                    bundle.putByte(key, val);
                } else if (float.class.isAssignableFrom(value.getClass())) {
                    float val = (float) value;
                    bundle.putFloat(key, val);
                } else if (double.class.isAssignableFrom(value.getClass())) {
                    double val = (double) value;
                    bundle.putDouble(key, val);
                } else if (char.class.isAssignableFrom(value.getClass())) {
                    char val = (char) value;
                    bundle.putChar(key, val);
                } else if (int[].class.isAssignableFrom(value.getClass())) {
                    int[] val = (int[]) value;
                    bundle.putIntArray(key, val);
                } else if (char[].class.isAssignableFrom(value.getClass())) {
                    char[] val = (char[]) value;
                    bundle.putCharArray(key, val);
                } else if (byte[].class.isAssignableFrom(value.getClass())) {
                    byte[] val = (byte[]) value;
                    bundle.putByteArray(key, val);
                } else if (short[].class.isAssignableFrom(value.getClass())) {
                    short[] val = (short[]) value;
                    bundle.putShortArray(key, val);
                } else if (long[].class.isAssignableFrom(value.getClass())) {
                    long[] val = (long[]) value;
                    bundle.putLongArray(key, val);
                } else if (float[].class.isAssignableFrom(value.getClass())) {
                    float[] val = (float[]) value;
                    bundle.putFloatArray(key, val);
                } else if (double[].class.isAssignableFrom(value.getClass())) {
                    double[] val = (double[]) value;
                    bundle.putDoubleArray(key, val);
                } else if (Serializable[].class.isAssignableFrom(value.getClass())) {
                    Serializable[] val = (Serializable[]) value;
                    bundle.putSerializable(key, val);
                } else if (String[].class.isAssignableFrom(value.getClass())) {
                    String[] val = (String[]) value;
                    bundle.putStringArray(key, val);
                } else if (Parcelable.class.isAssignableFrom(value.getClass())) {
                    Parcelable val = (Parcelable) value;
                    bundle.putParcelable(key, val);
                } else if (Parcelable[].class.isAssignableFrom(value.getClass())) {
                    Parcelable[] val = (Parcelable[]) value;
                    bundle.putParcelableArray(key, val);
                }
            }
            return bundle;
        }

        public Intent parseData(Intent intent) {
            Set<String> keys = extrasMap.keySet();
            for (String key : keys) {
                Object value = extrasMap.get(key);
                if (value == null) {
                    continue;
                }
                if (String.class.isAssignableFrom(value.getClass())) {
                    String val = (String) value;
                    intent.putExtra(key, val);
                } else if (int.class.isAssignableFrom(value.getClass())) {
                    int val = (int) value;
                    intent.putExtra(key, val);
                } else if (Serializable.class.isAssignableFrom(value.getClass())) {
                    Serializable val = (Serializable) value;
                    intent.putExtra(key, val);
                } else if (boolean.class.isAssignableFrom(value.getClass())) {
                    boolean val = (boolean) value;
                    intent.putExtra(key, val);
                } else if (long.class.isAssignableFrom(value.getClass())) {
                    long val = (long) value;
                    intent.putExtra(key, val);
                } else if (short.class.isAssignableFrom(value.getClass())) {
                    short val = (short) value;
                    intent.putExtra(key, val);
                } else if (byte.class.isAssignableFrom(value.getClass())) {
                    byte val = (byte) value;
                    intent.putExtra(key, val);
                } else if (float.class.isAssignableFrom(value.getClass())) {
                    float val = (float) value;
                    intent.putExtra(key, val);
                } else if (double.class.isAssignableFrom(value.getClass())) {
                    double val = (double) value;
                    intent.putExtra(key, val);
                } else if (char.class.isAssignableFrom(value.getClass())) {
                    char val = (char) value;
                    intent.putExtra(key, val);
                } else if (int[].class.isAssignableFrom(value.getClass())) {
                    int[] val = (int[]) value;
                    intent.putExtra(key, val);
                } else if (char[].class.isAssignableFrom(value.getClass())) {
                    char[] val = (char[]) value;
                    intent.putExtra(key, val);
                } else if (byte[].class.isAssignableFrom(value.getClass())) {
                    byte[] val = (byte[]) value;
                    intent.putExtra(key, val);
                } else if (short[].class.isAssignableFrom(value.getClass())) {
                    short[] val = (short[]) value;
                    intent.putExtra(key, val);
                } else if (long[].class.isAssignableFrom(value.getClass())) {
                    long[] val = (long[]) value;
                    intent.putExtra(key, val);
                } else if (float[].class.isAssignableFrom(value.getClass())) {
                    float[] val = (float[]) value;
                    intent.putExtra(key, val);
                } else if (double[].class.isAssignableFrom(value.getClass())) {
                    double[] val = (double[]) value;
                    intent.putExtra(key, val);
                } else if (Serializable[].class.isAssignableFrom(value.getClass())) {
                    Serializable[] val = (Serializable[]) value;
                    intent.putExtra(key, val);
                } else if (String[].class.isAssignableFrom(value.getClass())) {
                    String[] val = (String[]) value;
                    intent.putExtra(key, val);
                } else if (Parcelable.class.isAssignableFrom(value.getClass())) {
                    Parcelable val = (Parcelable) value;
                    intent.putExtra(key, val);
                } else if (Parcelable[].class.isAssignableFrom(value.getClass())) {
                    Parcelable[] val = (Parcelable[]) value;
                    intent.putExtra(key, val);
                }
            }
            return intent;
        }
    }
}
