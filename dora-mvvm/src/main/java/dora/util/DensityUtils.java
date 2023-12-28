/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Dimension Unit Conversion Tool.
 * 简体中文：尺寸单位转换工具。
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
