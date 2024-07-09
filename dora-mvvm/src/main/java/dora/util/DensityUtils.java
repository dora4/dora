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

    public static final int DP1 = dp2px(1);
    public static final int DP2 = dp2px(2);
    public static final int DP3 = dp2px(3);
    public static final int DP4 = dp2px(4);
    public static final int DP5 = dp2px(5);
    public static final int DP6 = dp2px(6);
    public static final int DP7 = dp2px(7);
    public static final int DP8 = dp2px(8);
    public static final int DP9 = dp2px(9);
    public static final int DP10 = dp2px(10);
    public static final int DP11 = dp2px(11);
    public static final int DP12 = dp2px(12);
    public static final int DP13 = dp2px(13);
    public static final int DP14 = dp2px(14);
    public static final int DP15 = dp2px(15);
    public static final int DP16 = dp2px(16);
    public static final int DP17 = dp2px(17);
    public static final int DP18 = dp2px(18);
    public static final int DP19 = dp2px(19);
    public static final int DP20 = dp2px(20);
    public static final int DP22 = dp2px(22);
    public static final int DP24 = dp2px(24);
    public static final int DP26 = dp2px(26);
    public static final int DP28 = dp2px(28);
    public static final int DP30 = dp2px(30);
    public static final int DP32 = dp2px(32);
    public static final int DP34 = dp2px(34);
    public static final int DP36 = dp2px(36);
    public static final int DP38 = dp2px(38);
    public static final int DP40 = dp2px(40);
    public static final int DP45 = dp2px(45);
    public static final int DP50 = dp2px(50);
    public static final int DP60 = dp2px(60);
    public static final int DP70 = dp2px(70);
    public static final int DP80 = dp2px(80);
    public static final int DP90 = dp2px(90);
    public static final int DP100 = dp2px(100);
    public static final int DP120 = dp2px(120);
    public static final int DP140 = dp2px(140);
    public static final int DP160 = dp2px(160);
    public static final int DP180 = dp2px(180);
    public static final int DP200 = dp2px(200);

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
