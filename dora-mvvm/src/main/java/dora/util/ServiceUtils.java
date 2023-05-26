package dora.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 更便捷获取系统服务的工具。
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

    public static Vibrator getVibrator() {
        return getVibrator(GlobalContext.get());
    }

    public static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
