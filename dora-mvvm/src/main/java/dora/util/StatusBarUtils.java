package dora.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class StatusBarUtils {

    public static boolean hasNotchInScreen(Activity activity) {
        // android  P 以上有标准 API 来判断是否有刘海屏
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                DisplayCutout displayCutout = activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
                if (displayCutout != null) {
                    // 说明有刘海屏
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo
            String manufacturer = Build.MANUFACTURER;
            if (manufacturer == null || manufacturer.length() == 0) {
                return false;
            } else if (manufacturer.equalsIgnoreCase("HUAWEI")) {
                return hasNotchHw(activity);
            } else if (manufacturer.equalsIgnoreCase("xiaomi")) {
                return hasNotchXiaoMi(activity);
            } else if (manufacturer.equalsIgnoreCase("oppo")) {
                return hasNotchOPPO(activity);
            } else if (manufacturer.equalsIgnoreCase("vivo")) {
                return hasNotchVIVO(activity);
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean hasNotchVIVO(Activity activity) {
        try {
            Class<?> c = Class.forName("android.util.FtFeature");
            Method get = c.getMethod("isFeatureSupport", int.class);
            return (boolean) (get.invoke(c, 0x20));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasNotchOPPO(Activity activity) {
        try {
            return activity.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasNotchXiaoMi(Activity activity) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("getInt", String.class, int.class);
            return (int) (get.invoke(c, "ro.miui.notch", 1)) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasNotchHw(Activity activity) {
        try {
            ClassLoader cl = activity.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            return (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
            return false;
        }
    }

    public static void setStatusBar(Activity activity, int bgColor, boolean isFullScreen, boolean isDarkStatusBarIcon) {
        //5.0以下不处理
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        int option = 0;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //只有在6.0以上才改变状态栏颜色，否则在5.0机器上，电量条图标是白色的，标题栏也是白色的，就看不见电量条了了
        //在5.0上显示默认灰色背景色
        if (Build.VERSION.SDK_INT >= 23) {
            // 设置状态栏底色颜色
            activity.getWindow().setStatusBarColor(bgColor);
            //浅色状态栏，则让状态栏图标变黑，深色状态栏，则让状态栏图标变白
            if (isDarkStatusBarIcon) {
                if (isFullScreen) {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            } else {
                if (isFullScreen) {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_VISIBLE;
                } else {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_VISIBLE;
                }
            }
        } else {
            if (isFullScreen) {
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            } else {
                activity.getWindow().setStatusBarColor(bgColor);
                option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(option);
    }

    /**
     * 检测状态栏背景颜色是否为深色。
     */
    public static boolean isDarkColor(int colorInt) {
        int gray = (int) (Color.red(colorInt) * 0.299 + Color.green(colorInt) * 0.587 + Color.blue(colorInt) * 0.114);
        return gray >= 192;
    }

    public static void setFullScreenWithCheckNotch(Activity activity, int statusBarColor) {
        if (hasNotchInScreen(activity)) {
            setStatusBar(activity, statusBarColor, false, true);
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setTransparencyStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setStatusBarColorRes(Activity activity, int colorResId) {
        setStatusBarColor(activity, activity.getResources().getColor(colorResId));
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setLightStatusBar(final Window window, final boolean dark, boolean isFullMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            switch (RomUtils.getLightStatusBarAvailableRomType()) {
                case RomUtils.AvailableRomType.MIUI:
                    setMIUIStatusBarLightMode(window, dark);
                    break;

                case RomUtils.AvailableRomType.FLYME:
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        setAndroidNativeLightStatusBar(window, dark, isFullMode);
                    } else {
                        setFlymeLightStatusBar(window, dark);
                    }
                    break;

                case RomUtils.AvailableRomType.ANDROID_NATIVE:
                    setAndroidNativeLightStatusBar(window, dark, isFullMode);
                    break;

                case RomUtils.AvailableRomType.NA:
                    // N/A do nothing
                    break;
            }
        }
    }

    /**
     * @param dark       true 字体颜色为黑色，false为白色
     * @param isFullMode 是否在全屏模式下
     */
    public static void setLightStatusBar(final Activity activity, final boolean dark, boolean isFullMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int romType = RomUtils.getLightStatusBarAvailableRomType();
            switch (romType) {
                case RomUtils.AvailableRomType.MIUI:
                    setMIUIStatusBarLightMode(activity, dark);
                    break;

                case RomUtils.AvailableRomType.FLYME:
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        setAndroidNativeLightStatusBar(activity, dark, isFullMode);
                    } else {
                        setFlymeLightStatusBar(activity, dark);
                    }
                    break;

                case RomUtils.AvailableRomType.ANDROID_NATIVE:
                    setAndroidNativeLightStatusBar(activity, dark, isFullMode);
                    break;

                case RomUtils.AvailableRomType.NA:
                    // N/A do nothing
                    break;
            }
        }
    }


    /**
     * 需要MIUIV6以上。
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static void setMIUIStatusBarLightMode(Object object, boolean dark) {
        Window window = null;
        if (object instanceof Activity) {
            window = ((Activity) object).getWindow();
        } else if (object instanceof Window) {
            window = (Window) object;
        }

        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMIUIV7OrAbove()) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }

            } catch (Exception ignore) {

            }
        }
    }

    private static boolean setFlymeLightStatusBar(Object obj, boolean dark) {
        boolean result = false;
        Window window = null;
        if (obj instanceof Activity) {
            window = ((Activity) obj).getWindow();
        } else if (obj instanceof Window) {
            window = ((Window) obj);
        }
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void setAndroidNativeLightStatusBar(Object obj, boolean dark, boolean isFullMode) {
        View decor = null;
        if (obj instanceof Activity) {
            decor = ((Activity) obj).getWindow().getDecorView();
        } else if (obj instanceof Window) {
            decor = ((Window) obj).getDecorView();
        }
        if (decor == null) {
            return;
        }
        if (dark) {
            if (isFullMode) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            if (isFullMode) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 获得状态栏高度。
     */
    public static int getStatusBarHeight() {
        Class<?> clazz;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            field = clazz.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = GlobalContext.get().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
