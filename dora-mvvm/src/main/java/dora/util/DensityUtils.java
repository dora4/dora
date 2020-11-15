package dora.util;

import android.content.Context;
import android.util.TypedValue;

public final class DensityUtils {

    private DensityUtils() {
    }

    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, GlobalContext.get().getResources().getDisplayMetrics());
    }

    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, GlobalContext.get().getResources().getDisplayMetrics());
    }

    public static float px2dp(float pxVal) {
        float scale = GlobalContext.get().getResources().getDisplayMetrics().density;
        return pxVal / scale;
    }

    public static float px2sp(float pxVal) {
        float scale = GlobalContext.get().getResources().getDisplayMetrics().scaledDensity;
        return pxVal / scale;
    }
}
