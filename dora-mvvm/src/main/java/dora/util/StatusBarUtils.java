package dora.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dora.mvvm.R;

/**
 * Mobile System Status Bar Related Tools.
 * 简体中文：手机系统状态栏相关工具。
 */
public final class StatusBarUtils {

    private static final int DORA_STATUS_BAR_VIEW_ID = R.id.dora_status_bar_view_id;

    /**
     * Set the status bar color for non-fullscreen content, automatically adapt to light or dark
     * color based on the color, commonly used in phones with Android 6.0 and above.
     * 简体中文：设置不全屏内容的状态栏颜色，6.0以上手机自动根据颜色适应亮暗色，常用。
     *
     * @param activity       The activity that needs to be set.
     * @param statusBarColor Status bar color value.
     * @param statusBarAlpha Status bar transparency/opacity.
     */
    public static void setStatusBar(Activity activity, @ColorInt int statusBarColor, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // >= 6.0 supports customizing light and dark text and icons based on the status bar
            // color.
            // 简体中文：>= 6.0 支持根据状态栏颜色定制浅色和深色的文字和图标
            activity.getWindow().setStatusBarColor(statusBarColor);
            int option;
            if (isDarkColor(statusBarColor)) {
                // If the status bar is in dark mode, then make the status bar text and icons white.
                // 简体中文：深色状态栏，则让状态栏文字和图标变白
                option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                // If the status bar is in light mode, then make the status bar text and icons black.
                // 简体中文：浅色状态栏，则让状态栏文字和图标变黑
                option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_VISIBLE;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(option);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.x
            // 简体中文：Android 5.x版本
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateColor(statusBarColor, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In Android 4.4, create a color block and add it to the DecorView.
            // 简体中文：Android 4.4 自己创建一个色块加到DecorView
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View doraStatusBarView = decorView.findViewById(DORA_STATUS_BAR_VIEW_ID);
            if (doraStatusBarView != null) {
                if (doraStatusBarView.getVisibility() == View.GONE) {
                    doraStatusBarView.setVisibility(View.VISIBLE);
                }
                doraStatusBarView.setBackgroundColor(calculateColor(statusBarColor, statusBarAlpha));
            } else {
                decorView.addView(createStatusBarView(activity, statusBarColor, statusBarAlpha));
            }
            setFitsSystemWindow(activity);
        } else {
            // < 4.4 Not customizable, black status bar, no solution.
            // 简体中文：< 4.4 不可定制，黑色状态栏，无解
        }
    }

    /**
     * Make the top of the layout content become part of the status bar, with a transparent status
     * bar. This is commonly used.
     * 简体中文：让布局内容的顶部成为状态栏的一部分，状态栏透明，常用。
     */
    public static void setFullScreenStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * Simply set the status bar to a translucent color without considering the issue of the light
     * and dark status bar icon and text introduced in Android 6.0.
     * 简体中文：简单设置状态栏为半透明颜色，不考虑6.0新增的亮暗色状态栏图标文字的问题。
     */
    public static void setTransparencyStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * Simply change the color of the status bar in Android 5.0 and above, without considering the
     * issue of the light and dark status bar icon and text introduced in Android 6.0.
     * 简体中文：简单改变Android 5.0以上状态栏的色值，不考虑6.0新增的亮暗色状态栏图标文字的问题。
     */
    public static void setStatusBarColorRes(Activity activity, @ColorRes int colorResId) {
        setStatusBarColor(activity, activity.getResources().getColor(colorResId));
    }

    /**
     * Simply change the color of the status bar in Android 5.0 and above, without considering the
     * issue of the light and dark status bar icon and text introduced in Android 6.0.
     * 简体中文：简单改变Android 5.0以上状态栏的色值，不考虑6.0新增的亮暗色状态栏图标文字的问题。
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * Whether the status bar is in dark mode.
     * 简体中文：状态栏是否是深色。
     *
     * @param color Status bar color value.
     */
    public static boolean isDarkColor(@ColorInt int color) {
        int gray = (int) (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114);
        return gray >= 192;
    }

    /**
     * Generate a rectangle bar with the same size as the status bar.
     * 简体中文：生成一个和状态栏大小相同的矩形条。
     *
     * @param activity       The activity that needs to be set.
     * @param color          Status bar color value.
     * @param alpha          Status bar transparency/opacity.
     * @return Status bar rectangle view.
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        // Draw a rectangle with the same height as the status bar.
        // 简体中文：绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateColor(color, alpha));
        statusBarView.setId(DORA_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    private static void setFitsSystemWindow(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static int calculateColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * Set the color of half of the status bar for the DrawerLayout.
     * 简体中文：设置DrawerLayout的状态栏一半的颜色。
     */
    public static void setStatusBarWithDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int statusBarColor,
                                    @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        View doraStatusBarView = contentLayout.findViewById(DORA_STATUS_BAR_VIEW_ID);
        if (doraStatusBarView != null) {
            if (doraStatusBarView.getVisibility() == View.GONE) {
                doraStatusBarView.setVisibility(View.VISIBLE);
            }
            doraStatusBarView.setBackgroundColor(statusBarColor);
        } else {
            contentLayout.addView(createStatusBarView(activity, statusBarColor, 0));
        }
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight() + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        setFitsSystemWindow(drawerLayout, contentLayout);
        addStatusBarView(activity, statusBarAlpha);
    }

    private static void setFitsSystemWindow(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * Add a semi-transparent rectangle bar.
     * 简体中文：添加半透明矩形条。
     *
     * @param activity       The activity that needs to be set.
     * @param statusBarAlpha          Status bar transparency/opacity.
     */
    private static void addStatusBarView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View doraStatusBarView = contentView.findViewById(DORA_STATUS_BAR_VIEW_ID);
        if (doraStatusBarView != null) {
            if (doraStatusBarView.getVisibility() == View.GONE) {
                doraStatusBarView.setVisibility(View.VISIBLE);
            }
            doraStatusBarView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    /**
     * Create a rectangle bar with the same size as the status bar.
     * 简体中文：生成一个和状态栏大小相同的矩形条。
     *
     * @param activity       The activity that needs to be set.
     * @param alpha          Status bar transparency/opacity.
     * @return Status bar rectangle view.
     */
    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        // Draw a rectangle with the same height as the status bar.
        // 简体中文：绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateColor(Color.BLACK, alpha));
        statusBarView.setId(DORA_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setLightDarkStatusBar(final Window window, final boolean dark, boolean isFullMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            switch (RomUtils.getLightStatusBarAvailableRomType()) {
                case RomUtils.AvailableRomType.MIUI:
                    setMIUIStatusBarLightDarkMode(window, dark);
                    break;

                case RomUtils.AvailableRomType.FLYME:
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        setAndroidNativeLightDarkStatusBar(window, dark, isFullMode);
                    } else {
                        setFlymeLightDarkStatusBar(window, dark);
                    }
                    break;

                case RomUtils.AvailableRomType.ANDROID_NATIVE:
                    setAndroidNativeLightDarkStatusBar(window, dark, isFullMode);
                    break;

                case RomUtils.AvailableRomType.NA:
                    // N/A do nothing
                    break;
            }
        }
    }

    /**
     * Change the light and dark mode of the status bar.
     * 简体中文：改变状态栏的亮暗色模式。
     *
     * @param dark       The font color is black for "true", and white for "false".
     * @param isFullMode Whether in fullscreen mode.
     */
    public static void setLightDarkStatusBar(final Activity activity, final boolean dark, boolean isFullMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int romType = RomUtils.getLightStatusBarAvailableRomType();
            switch (romType) {
                case RomUtils.AvailableRomType.MIUI:
                    setMIUIStatusBarLightDarkMode(activity, dark);
                    break;

                case RomUtils.AvailableRomType.FLYME:
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        setAndroidNativeLightDarkStatusBar(activity, dark, isFullMode);
                    } else {
                        setFlymeLightDarkStatusBar(activity, dark);
                    }
                    break;

                case RomUtils.AvailableRomType.ANDROID_NATIVE:
                    setAndroidNativeLightDarkStatusBar(activity, dark, isFullMode);
                    break;

                case RomUtils.AvailableRomType.NA:
                    // N/A do nothing
                    break;
            }
        }
    }

    /**
     * Changing the light and dark mode of the MIUI 6 and above status bar.
     * 简体中文：改变MIUI6以上的状态栏的亮暗色模式。
     *
     * @param dark Whether to set the status bar text and icon color to dark mode.
     */
    private static void setMIUIStatusBarLightDarkMode(Object object, boolean dark) {
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
                    // Transparent status bar with black font.
                    // 简体中文：状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    // Remove black font.
                    // 简体中文：清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMIUIV7OrAbove()) {
                    // Starting from development version 7.7.13 and later, the system API is used.
                    // The old method is ineffective but won't cause errors, so both methods should
                    // be included.
                    // 简体中文：开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
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

    private static boolean setFlymeLightDarkStatusBar(Object obj, boolean dark) {
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
    private static void setAndroidNativeLightDarkStatusBar(Object obj, boolean dark, boolean isFullMode) {
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
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            // 简体中文：我们想要再次将图标和文字的颜色改为白色。
            // 你也可以事先记录状态栏的标志，这样如果之前设置了其他标志，如透明或全屏等，你可以完全恢复界面。
            if (isFullMode) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * Get the status bar height.
     * 简体中文：获得状态栏高度。
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
