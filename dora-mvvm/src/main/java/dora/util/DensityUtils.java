package dora.util;

import android.content.Context;
import android.util.TypedValue;

public final class DensityUtils {

    private DensityUtils() {
    }

    public static float dp2px(float dpVal, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(float spVal, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(float pxVal, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxVal / scale;
    }

    public static float px2sp(float pxVal, Context context) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxVal / scale;
    }
}
