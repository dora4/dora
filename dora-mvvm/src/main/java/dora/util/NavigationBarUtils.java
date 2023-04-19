package dora.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 系统底部导航栏相关工具。
 */
public final class NavigationBarUtils {

    private static WindowManager wm;

    private NavigationBarUtils() {
    }

    public static int getRealHeight() {
        return getRealHeight(GlobalContext.get());
    }

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

    public static boolean isShowNavigationBar(Activity activity) {
        if (activity == null) {
            return false;
        }
        Rect contentRect = new Rect();
        try {
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(contentRect);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        int activityHeight = contentRect.height();
        int statusBarHeight = StatusBarUtils.getStatusBarHeight(activity);
        int remainHeight = getRealHeight(activity) - statusBarHeight;
        if (activityHeight == remainHeight) {
            return false;
        } else {
            return true;
        }
    }

    public static int getNavigationBarHeight() {
        return getNavigationBarHeight(GlobalContext.get());
    }

    /**
     * 获得导航栏高度。
     */
    public static int getNavigationBarHeight(Context context) {
        Class<?> clazz;
        Object obj;
        Field field;
        int x, navigationBarHeight = 0;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            field = clazz.getField("navigation_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            navigationBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return navigationBarHeight;
    }
}
