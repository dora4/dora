package dora.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import java.lang.reflect.Field;

/**
 * 系统底部导航栏相关工具。
 */
public final class NavigationBarUtils {

    private NavigationBarUtils() {
    }

    public static void setNavigationBarColorRes(Activity activity, @ColorRes int colorResId) {
        setNavigationBarColor(activity, activity.getResources().getColor(colorResId));
    }

    public static void setNavigationBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(color);
        }
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
        int remainHeight = ScreenUtils.getRealHeight(activity) - statusBarHeight;
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
