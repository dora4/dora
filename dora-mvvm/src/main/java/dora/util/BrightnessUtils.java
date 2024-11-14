package dora.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

public class BrightnessUtils {

    private static final String TAG = "BrightnessUtils";

    /**
     * Determine whether automatic brightness adjustment is enabled.
     * 简体中文：判断是否开启了自动亮度调节。
     */
    public static boolean isAutoBrightness(Activity activity) {
        boolean isAuto = false;
        try {
            isAuto = Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return isAuto;
    }

    /**
     * Get the screen brightness. In the system brightness mode, the brightness value obtained in
     * automatic mode differs from that in manual mode.
     * 简体中文：获取屏幕的亮度。系统亮度模式中，自动模式与手动模式获取到的系统亮度的值不同。
     */
    public static int getScreenBrightness(Activity activity) {
        if (isAutoBrightness(activity)) {
            return getAutoScreenBrightness(activity);
        } else {
            return getManualScreenBrightness(activity);
        }
    }

    /**
     * To get the screen brightness in manual mode.
     * 简体中文：获取手动模式下的屏幕亮度。
     *
     * @return value:0~255
     */
    public static int getManualScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * Get the screen brightness in automatic mode.
     * 简体中文：获取自动模式下的屏幕亮度。
     *
     * @return value:0~255
     */
    public static int getAutoScreenBrightness(Activity activity) {
        float nowBrightnessValue = 0;
        // Get the brightness level in automatic adjustment mode, with a range from 0 to 1.
        // 简体中文：获取自动调节下的亮度范围在 0~1 之间
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getFloat(resolver, Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "getAutoScreenBrightness: " + nowBrightnessValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Convert the brightness range to (0~255).
        // 简体中文：转换范围为 (0~255)
        float fValue = nowBrightnessValue * 255.0f;
        Log.d(TAG, "brightness: " + fValue);
        return (int) fValue;
    }

    /**
     * Set the brightness by modifying the current Windows brightness level through screenBrightness.
     * lp.screenBrightness: The parameter range is 0 to 1.
     * 简体中文：设置亮度。通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
     * lp.screenBrightness:参数范围为 0~1。
     */
    public static void setBrightness(Activity activity, int brightness) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            // Convert data in the range of 0 to 255 to the range of 0 to 1.
            // 简体中文：将 0~255 范围内的数据，转换为 0~1
            lp.screenBrightness = (float) brightness * (1f / 255f);
            Log.d(TAG, "lp.screenBrightness == " + lp.screenBrightness);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the default brightness for the given activity's window.
     * This method removes any manual brightness adjustment, allowing the system to manage
     * brightness automatically.
     * 简体中文：设置给定活动窗口的默认亮度。此方法移除任何手动亮度调整，允许系统自动管理亮度。
     */
    public static void setAutoBrightness(Activity activity) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
