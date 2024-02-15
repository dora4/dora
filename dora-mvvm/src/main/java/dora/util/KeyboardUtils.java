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
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Keyboard Input Method Related Tools.
 * 简体中文：键盘输入法相关工具。
 */
public final class KeyboardUtils {

    private static final int TAG_ON_GLOBAL_LAYOUT_LISTENER = -8;

    private KeyboardUtils() {
    }

    /**
     * Show the soft input.
     * 简体中文：显示软输入法。
     */
    public static void showSoftInput() {
        InputMethodManager imm = ServiceUtils.getInputMethodManager();
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Show the soft input.
     * 简体中文：显示软输入法。
     */
    public static void showSoftInput(Activity activity) {
        if (!isSoftInputVisible(activity)) {
            toggleSoftInput();
        }
    }

    /**
     * Show the soft input.
     * 简体中文：显示软输入法。
     */
    public static void showSoftInput(@NonNull final View view) {
        showSoftInput(view, 0);
    }

    /**
     * Show the soft input.
     * 简体中文：显示软输入法。
     *
     * @param view The view.
     * @param flags Provides additional operating flags.  Currently may be
     * 0 or have the {@link InputMethodManager#SHOW_IMPLICIT} bit set.
     */
    public static void showSoftInput(@NonNull final View view, final int flags) {
        InputMethodManager imm = ServiceUtils.getInputMethodManager(view.getContext());
        if (imm == null) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                    || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    toggleSoftInput();
                }
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Hide the soft input.
     * 简体中文：隐藏软输入法。
     *
     * @param activity The activity.
     */
    public static void hideSoftInput(@Nullable final Activity activity) {
        if (activity == null) {
            return;
        }
        hideSoftInput(activity.getWindow());
    }

    /**
     * Hide the soft input.
     * 简体中文：隐藏软输入法。
     *
     * @param window The window.
     */
    public static void hideSoftInput(@Nullable final Window window) {
        if (window == null) {
            return;
        }
        View view = window.getCurrentFocus();
        if (view == null) {
            View decorView = window.getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(window.getContext());
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(view);
    }

    /**
     * Hide the soft input.
     * 简体中文：隐藏软输入法。
     *
     * @param view The view.
     */
    public static void hideSoftInput(@NonNull final View view) {
        InputMethodManager imm = ServiceUtils.getInputMethodManager(view.getContext());
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static long millis;

    /**
     * Hide the soft input.
     * 简体中文：隐藏软输入法。
     *
     * @param activity The activity.
     */
    public static void hideSoftInputByToggle(@Nullable final Activity activity) {
        if (activity == null) {
            return;
        }
        long nowMillis = SystemClock.elapsedRealtime();
        long delta = nowMillis - millis;
        if (Math.abs(delta) > 500 && KeyboardUtils.isSoftInputVisible(activity)) {
            KeyboardUtils.toggleSoftInput();
        }
        millis = nowMillis;
    }

    /**
     * Toggle the soft input display or not.
     * 简体中文：切换软输入显示与否。
     */
    public static void toggleSoftInput() {
        InputMethodManager imm = ServiceUtils.getInputMethodManager();
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(0, 0);
    }

    private static int sDecorViewDelta = 0;

    /**
     * Return whether soft input is visible.
     * 简体中文：返回软输入是否可见。
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(@NonNull final Activity activity) {
        return getDecorViewInvisibleHeight(activity.getWindow()) > 0;
    }

    private static int getDecorViewInvisibleHeight(@NonNull final Window window) {
        final View decorView = window.getDecorView();
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        Log.d("KeyboardUtils",
            "getDecorViewInvisibleHeight: " + (decorView.getBottom() - outRect.bottom));
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= NavigationBarUtils.getNavigationBarHeight()
                + StatusBarUtils.getStatusBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    /**
     * Register soft input changed listener.
     * 简体中文：注册软输入变更监听器。
     *
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(@NonNull final Activity activity,
                                                        @NonNull
                                                        final OnSoftInputChangedListener listener) {
        registerSoftInputChangedListener(activity.getWindow(), listener);
    }

    /**
     * Register soft input changed listener.
     * 简体中文：注册软输入变化监听器。
     *
     * @param window The window.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(@NonNull final Window window,
                                                        @NonNull
                                                        final OnSoftInputChangedListener listener) {
        final int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        final FrameLayout contentView = window.findViewById(android.R.id.content);
        final int[] decorViewInvisibleHeightPre = { getDecorViewInvisibleHeight(window) };
        OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getDecorViewInvisibleHeight(window);
                if (decorViewInvisibleHeightPre[0] != height) {
                    listener.onSoftInputChanged(height);
                    decorViewInvisibleHeightPre[0] = height;
                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, onGlobalLayoutListener);
    }

    /**
     * Unregister soft input changed listener.
     * 简体中文：取消注册软输入变化监听器。
     *
     * @param window The window.
     */
    public static void unregisterSoftInputChangedListener(@NonNull final Window window) {
        final View contentView = window.findViewById(android.R.id.content);
        if (contentView == null) {
            return;
        }
        Object tag = contentView.getTag(TAG_ON_GLOBAL_LAYOUT_LISTENER);
        if (tag instanceof OnGlobalLayoutListener) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                contentView.getViewTreeObserver().removeOnGlobalLayoutListener((OnGlobalLayoutListener) tag);
                //这里会发生内存泄漏 如果不设置为null
                contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, null);
            }
        }
    }

    /**
     * Fix the bug of 5497 in Android.
     * <p>Don't set adjustResize</p>
     * 简体中文：修复 Android 中的 5497 号错误。
     * <p>不要设置 adjustResize</p>
     *
     * @param activity The activity.
     */
    public static void fixAndroidBug5497(@NonNull final Activity activity) {
        fixAndroidBug5497(activity.getWindow());
    }

    /**
     * Fix the bug of 5497 in Android.
     * <p>Don't set adjustResize</p>
     * 简体中文：修复 Android 中的 5497 号错误。
     * <p>不要设置 adjustResize</p>
     *
     * @param window The window.
     */
    public static void fixAndroidBug5497(@NonNull final Window window) {
        int softInputMode = window.getAttributes().softInputMode;
        window.setSoftInputMode(
            softInputMode & ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final FrameLayout contentView = window.findViewById(android.R.id.content);
        final View contentViewChild = contentView.getChildAt(0);
        final int paddingBottom = contentViewChild.getPaddingBottom();
        final int[] contentViewInvisibleHeightPre5497 = { getContentViewInvisibleHeight(window) };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getContentViewInvisibleHeight(window);
                if (contentViewInvisibleHeightPre5497[0] != height) {
                    contentViewChild.setPadding(contentViewChild.getPaddingLeft(),
                        contentViewChild.getPaddingTop(), contentViewChild.getPaddingRight(),
                        paddingBottom + getDecorViewInvisibleHeight(window));
                    contentViewInvisibleHeightPre5497[0] = height;
                }
            }
        });
    }

    private static int getContentViewInvisibleHeight(final Window window) {
        final View contentView = window.findViewById(android.R.id.content);
        if (contentView == null) {
            return 0;
        }
        final Rect outRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(outRect);
        Log.d("KeyboardUtils",
            "getContentViewInvisibleHeight: " + (contentView.getBottom() - outRect.bottom));
        int delta = Math.abs(contentView.getBottom() - outRect.bottom);
        if (delta <= StatusBarUtils.getStatusBarHeight()
                + NavigationBarUtils.getNavigationBarHeight()) {
            return 0;
        }
        return delta;
    }

    /**
     * Fix the leaks of soft input.
     * 简体中文：修复软输入的内存泄漏问题。
     *
     * @param activity The activity.
     */
    public static void fixSoftInputLeaks(@NonNull final Activity activity) {
        fixSoftInputLeaks(activity.getWindow());
    }

    /**
     * Fix the leaks of soft input.
     * 简体中文：修复软输入的内存泄漏问题。
     *
     * @param window The window.
     */
    public static void fixSoftInputLeaks(@NonNull final Window window) {
        InputMethodManager imm = ServiceUtils.getInputMethodManager(window.getContext());
        if (imm == null) {
            return;
        }
        String[] leakViews =
            new String[] { "mLastSrvView", "mCurRootView", "mServedView", "mNextServedView" };
        for (String leakView : leakViews) {
            try {
                Field leakViewField = InputMethodManager.class.getDeclaredField(leakView);
                if (!leakViewField.isAccessible()) {
                    leakViewField.setAccessible(true);
                }
                Object obj = leakViewField.get(imm);
                if (!(obj instanceof View)) {
                    continue;
                }
                View view = (View) obj;
                if (view.getRootView() == window.getDecorView().getRootView()) {
                    leakViewField.set(imm, null);
                }
            } catch (Throwable ignore) {/**/}
        }
    }

    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }
}
