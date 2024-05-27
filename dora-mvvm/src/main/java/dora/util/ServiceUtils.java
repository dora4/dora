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

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * A tool for more convenient access to system services.
 * 简体中文：更便捷获取系统服务的工具。
 */
public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static InputMethodManager getInputMethodManager() {
        return getInputMethodManager(GlobalContext.get());
    }

    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static ActivityManager getActivityManager() {
        return getActivityManager(GlobalContext.get());
    }

    public static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static WindowManager getWindowManager() {
        return getWindowManager(GlobalContext.get());
    }

    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static NotificationManager getNotificationManager() {
        return getNotificationManager(GlobalContext.get());
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static ConnectivityManager getConnectivityManager() {
        return getConnectivityManager(GlobalContext.get());
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static AudioManager getAudioManager() {
        return getAudioManager(GlobalContext.get());
    }

    public static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static PowerManager getPowerManager() {
        return getPowerManager(GlobalContext.get());
    }

    public static PowerManager getPowerManager(Context context) {
        return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public static AlarmManager getAlarmManager() {
        return getAlarmManager(GlobalContext.get());
    }

    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static ClipboardManager getClipboardManager() {
        return getClipboardManager(GlobalContext.get());
    }

    public static ClipboardManager getClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static void copyText(String label, String content) {
        copyText(GlobalContext.get(), label, content);
    }

    public static void copyText(Context context, String label, String content) {
        ClipboardManager cm = getClipboardManager(context);
        ClipData clipData = ClipData.newPlainText(label, content);
        cm.setPrimaryClip(clipData);
    }

    public static Vibrator getVibrator() {
        return getVibrator(GlobalContext.get());
    }

    public static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
