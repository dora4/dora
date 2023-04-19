package dora.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * 尺寸单位转换工具。
 */
public final class DensityUtils {

    private DensityUtils() {
    }

    public static int dp2px(float dpVal) {
        return dp2px(GlobalContext.get(), dpVal);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(float spVal) {
        return sp2px(GlobalContext.get(), spVal);
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(float pxVal) {
        return px2dp(GlobalContext.get(), pxVal);
    }

    public static float px2dp(Context context, float pxVal) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxVal / scale;
    }

    public static float px2sp(float pxVal) {
        return px2sp(GlobalContext.get(), pxVal);
    }

    public static float px2sp(Context context, float pxVal) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxVal / scale;
    }
}
