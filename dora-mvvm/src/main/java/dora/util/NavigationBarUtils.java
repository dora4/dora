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

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

/**
 * System bottom navigation bar-related tools.
 * 简体中文：系统底部导航栏相关工具。
 */
public final class NavigationBarUtils {

    private NavigationBarUtils() {
    }

    public static void setNavigationBarColorRes(Activity activity, @ColorRes int colorResId) {
        setNavigationBarColor(activity, activity.getResources().getColor(colorResId));
    }

    public static void setNavigationBarColor(Activity activity, @ColorInt int color) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(color);
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
        int statusBarHeight = StatusBarUtils.getStatusBarHeight();
        int remainHeight = ScreenUtils.getRealHeight(activity) - statusBarHeight;
        return activityHeight != remainHeight;
    }

    /**
     * Get the height of the navigation bar.
     * 简体中文：获取导航栏高度。
     */
    public static int getNavigationBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void hideNavigationBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
