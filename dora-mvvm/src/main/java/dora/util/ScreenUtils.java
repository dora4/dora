package dora.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Screen width and height reading tool.
 * 简体中文：屏幕宽高读取工具。
 */
public final class ScreenUtils {

    private static ScreenUtils sInstance;
    private static WindowManager wm;

    private ScreenUtils() {
    }

    private static ScreenUtils getInstance() {
        if (sInstance == null) {
            synchronized (ScreenUtils.class) {
                if (sInstance == null) {
                    sInstance = new ScreenUtils();
                }
            }
        }
        return sInstance;
    }

    public static int getRealHeight() {
        return getRealHeight(GlobalContext.get());
    }

    /**
     * Obtain the real height, equivalent to the screen height. It refers to the height that
     * includes what is drawn by the Display class, including the system status bar, DecorView,
     * and system navigation bar. Its value remains unaffected regardless of whether the system
     * navigation bar is displayed or not.
     * 简体中文：获取真实高度，等价于屏幕高度。它是指包括Display类绘制的高度，包括系统状态栏、DecorView和系统导航
     * 栏。无论是否显示系统导航栏，都不影响其值。
     */
    public static int getRealHeight(Context context) {
        if (wm == null) {
            wm = (WindowManager)
                    context.getSystemService(Context.WINDOW_SERVICE);
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static int getScreenWidth() {
        return getScreenWidth(GlobalContext.get());
    }

    public static int getScreenWidth(Context context) {
        return getInstance().getScreenSize(context)[0];
    }

    public static int getScreenHeight() {
        return getScreenHeight(GlobalContext.get());
    }

    public static int getScreenHeight(Context context) {
        return getInstance().getScreenSize(context)[1];
    }

    public static int getContentWidth() {
        return getContentWidth(GlobalContext.get());
    }

    public static int getContentWidth(Context context) {
        return getInstance().getScreenSize(context, true)[0];
    }

    public static int getContentHeight() {
        return getContentHeight(GlobalContext.get());
    }

    public static int getContentHeight(Context context) {
        return getInstance().getScreenSize(context, true)[1];
    }

    private int[] getScreenSize(Context context) {
        return getScreenSize(context, false);
    }

    private int[] getScreenSize(Context context, boolean useContentSize) {

        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        if (!useContentSize) {
            size[0] = widthPixels;
            size[1] = heightPixels - StatusBarUtils.getStatusBarHeight();

            return size;
        }

        // includes window decorations (statusbar bar/menu bar)
        // 简体中文：包括窗口装饰（状态栏/菜单栏）
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }
}
