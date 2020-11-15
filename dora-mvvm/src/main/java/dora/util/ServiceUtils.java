package dora.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public final class ServiceUtils {

    private ServiceUtils() {
    }

    public static InputMethodManager getInputMethodManager() {
        return (InputMethodManager) GlobalContext.get().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static ActivityManager getActivityManager() {
        return (ActivityManager) GlobalContext.get().getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static WindowManager getWindowManager() {
        return (WindowManager) GlobalContext.get().getSystemService(Context.WINDOW_SERVICE);
    }

    public static NotificationManager getNotificationManager() {
        return (NotificationManager) GlobalContext.get().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) GlobalContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static AudioManager getAudioManager() {
        return (AudioManager) GlobalContext.get().getSystemService(Context.AUDIO_SERVICE);
    }

    public static PowerManager getPowerManager() {
        return (PowerManager) GlobalContext.get().getSystemService(Context.POWER_SERVICE);
    }

    public static AlarmManager getAlarmManager() {
        return (AlarmManager) GlobalContext.get().getSystemService(Context.ALARM_SERVICE);
    }

    public static Vibrator getVibrator() {
        return (Vibrator) GlobalContext.get().getSystemService(Context.VIBRATOR_SERVICE);
    }
}
