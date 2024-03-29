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

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment Management-related Tool.
 * 简体中文：Fragment管理相关工具。
 */
public final class FragmentUtils {

    private static final int TYPE_ADD_FRAGMENT = 0x01;
    private static final int TYPE_SHOW_FRAGMENT = 0x01 << 1;
    private static final int TYPE_HIDE_FRAGMENT = 0x01 << 2;
    private static final int TYPE_SHOW_HIDE_FRAGMENT = 0x01 << 3;
    private static final int TYPE_REPLACE_FRAGMENT = 0x01 << 4;
    private static final int TYPE_REMOVE_FRAGMENT = 0x01 << 5;
    private static final int TYPE_REMOVE_TO_FRAGMENT = 0x01 << 6;

    private static final String ARGS_ID = "args_id";
    private static final String ARGS_IS_HIDE = "args_is_hide";
    private static final String ARGS_IS_ADD_STACK = "args_is_add_stack";
    private static final String ARGS_TAG = "args_tag";

    private FragmentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId) {
        add(fm, add, containerId, null, false, false);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param isHide      True to hide, false otherwise.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final boolean isHide) {
        add(fm, add, containerId, null, isHide, false);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param isHide      True to hide, false otherwise.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final boolean isHide,
                           final boolean isAddStack) {
        add(fm, add, containerId, null, isHide, isAddStack);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim) {
        add(fm, add, containerId, null, false, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param containerId The id of container.
     * @param add         The fragment will be add.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final boolean isAddStack,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim) {
        add(fm, add, containerId, null, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm           The manager of fragment.
     * @param containerId  The id of container.
     * @param add          The fragment will be add.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim,
                           @AnimatorRes @AnimRes final int popEnterAnim,
                           @AnimatorRes @AnimRes final int popExitAnim) {
        add(fm, add, containerId, null, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm           The manager of fragment.
     * @param containerId  The id of container.
     * @param add          The fragment will be add.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final boolean isAddStack,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim,
                           @AnimatorRes @AnimRes final int popEnterAnim,
                           @AnimatorRes @AnimRes final int popExitAnim) {
        add(fm, add, containerId, null, isAddStack, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm             The manager of fragment.
     * @param add            The fragment will be add.
     * @param containerId    The id of container.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           @NonNull final View... sharedElements) {
        add(fm, add, containerId, null, false, sharedElements);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm             The manager of fragment.
     * @param add            The fragment will be add.
     * @param containerId    The id of container.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final boolean isAddStack,
                           @NonNull final View... sharedElements) {
        add(fm, add, containerId, null, isAddStack, sharedElements);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param adds        The fragments will be add.
     * @param containerId The id of container.
     * @param showIndex   The index of fragment will be shown.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final List<Fragment> adds,
                           @IdRes final int containerId,
                           final int showIndex) {
        add(fm, adds.toArray(new Fragment[adds.size()]), containerId, null, showIndex);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param adds        The fragments will be add.
     * @param containerId The id of container.
     * @param showIndex   The index of fragment will be shown.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment[] adds,
                           @IdRes final int containerId,
                           final int showIndex) {
        add(fm, adds, containerId, null, showIndex);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param tag         The tag of fragment.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag) {
        add(fm, add, containerId, tag, false, false);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param tag         The tag of fragment.
     * @param isHide      True to hide, false otherwise.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           final boolean isHide) {
        add(fm, add, containerId, tag, isHide, false);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param tag         The tag of fragment.
     * @param isHide      True to hide, false otherwise.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           final boolean isHide,
                           final boolean isAddStack) {
        putArgs(add, new Args(containerId, tag, isHide, isAddStack));
        operateNoAnim(fm, TYPE_ADD_FRAGMENT, null, add);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param tag         The tag of fragment.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim) {
        add(fm, add, containerId, tag, false, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param tag         The tag of fragment.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           final boolean isAddStack,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim) {
        add(fm, add, containerId, tag, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm           The manager of fragment.
     * @param add          The fragment will be add.
     * @param containerId  The id of container.
     * @param tag          The tag of fragment.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim,
                           @AnimatorRes @AnimRes final int popEnterAnim,
                           @AnimatorRes @AnimRes final int popExitAnim) {
        add(fm, add, containerId, tag, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm           The manager of fragment.
     * @param add          The fragment will be add.
     * @param containerId  The id of container.
     * @param tag          The tag of fragment.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           final boolean isAddStack,
                           @AnimatorRes @AnimRes final int enterAnim,
                           @AnimatorRes @AnimRes final int exitAnim,
                           @AnimatorRes @AnimRes final int popEnterAnim,
                           @AnimatorRes @AnimRes final int popExitAnim) {
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(add, new Args(containerId, tag, false, isAddStack));
        addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim);
        operate(TYPE_ADD_FRAGMENT, fm, ft, null, add);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm             The manager of fragment.
     * @param add            The fragment will be add.
     * @param tag            The tag of fragment.
     * @param containerId    The id of container.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           @NonNull final View... sharedElements) {
        add(fm, add, containerId, tag, false, sharedElements);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm             The manager of fragment.
     * @param add            The fragment will be add.
     * @param containerId    The id of container.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment add,
                           @IdRes final int containerId,
                           final String tag,
                           final boolean isAddStack,
                           @NonNull final View... sharedElements) {
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(add, new Args(containerId, tag, false, isAddStack));
        addSharedElement(ft, sharedElements);
        operate(TYPE_ADD_FRAGMENT, fm, ft, null, add);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param adds        The fragments will be add.
     * @param containerId The id of container.
     * @param showIndex   The index of fragment will be shown.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final List<Fragment> adds,
                           @IdRes final int containerId,
                           final String[] tags,
                           final int showIndex) {
        add(fm, adds.toArray(new Fragment[adds.size()]), containerId, tags, showIndex);
    }

    /**
     * Add fragment.
     * 简体中文：添加片段。
     *
     * @param fm          The manager of fragment.
     * @param adds        The fragments will be add.
     * @param containerId The id of container.
     * @param showIndex   The index of fragment will be shown.
     */
    public static void add(@NonNull final FragmentManager fm,
                           @NonNull final Fragment[] adds,
                           @IdRes final int containerId,
                           final String[] tags,
                           final int showIndex) {
        if (tags == null) {
            for (int i = 0, len = adds.length; i < len; ++i) {
                putArgs(adds[i], new Args(containerId, null, showIndex != i, false));
            }
        } else {
            for (int i = 0, len = adds.length; i < len; ++i) {
                putArgs(adds[i], new Args(containerId, tags[i], showIndex != i, false));
            }
        }
        operateNoAnim(fm, TYPE_ADD_FRAGMENT, null, adds);
    }

    /**
     * Show fragment.
     * 简体中文：展示片段。
     *
     * @param show The fragment will be show.
     */
    public static void show(@NonNull final Fragment show) {
        putArgs(show, false);
        operateNoAnim(show.getFragmentManager(), TYPE_SHOW_FRAGMENT, null, show);
    }

    /**
     * Show fragment.
     * 简体中文：展示片段。
     *
     * @param fm The manager of fragment.
     */
    public static void show(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = getFragments(fm);
        for (Fragment show : fragments) {
            putArgs(show, false);
        }
        operateNoAnim(fm,
                TYPE_SHOW_FRAGMENT,
                null,
                fragments.toArray(new Fragment[fragments.size()])
        );
    }

    /**
     * Hide fragment.
     * 简体中文：隐藏片段。
     *
     * @param hide The fragment will be hide.
     */
    public static void hide(@NonNull final Fragment hide) {
        putArgs(hide, true);
        operateNoAnim(hide.getFragmentManager(), TYPE_HIDE_FRAGMENT, null, hide);
    }

    /**
     * Hide fragment.
     * 简体中文：隐藏片段。
     *
     * @param fm The manager of fragment.
     */
    public static void hide(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = getFragments(fm);
        for (Fragment hide : fragments) {
            putArgs(hide, true);
        }
        operateNoAnim(fm,
                TYPE_HIDE_FRAGMENT,
                null,
                fragments.toArray(new Fragment[fragments.size()])
        );
    }

    /**
     * Show fragment then hide other fragment.
     * 简体中文：显示片段，然后隐藏其他片段。
     *
     * @param showIndex The index of fragment will be shown.
     * @param fragments The fragments will be hide.
     */
    public static void showHide(final int showIndex, @NonNull final List<Fragment> fragments) {
        showHide(fragments.get(showIndex), fragments);
    }

    /**
     * Show fragment then hide other fragment.
     * 简体中文：显示片段，然后隐藏其他片段。
     *
     * @param show The fragment will be show.
     * @param hide The fragment will be hide.
     */
    public static void showHide(@NonNull final Fragment show, @NonNull final List<Fragment> hide) {
        for (Fragment fragment : hide) {
            putArgs(fragment, fragment != show);
        }
        operateNoAnim(show.getFragmentManager(), TYPE_SHOW_HIDE_FRAGMENT, show,
                hide.toArray(new Fragment[hide.size()]));
    }

    /**
     * Show fragment then hide other fragment.
     * 简体中文：显示片段，然后隐藏其他片段。
     *
     * @param showIndex The index of fragment will be shown.
     * @param fragments The fragment will be hide.
     */
    public static void showHide(final int showIndex, @NonNull final Fragment... fragments) {
        showHide(fragments[showIndex], fragments);
    }

    /**
     * Show fragment then hide other fragment.
     * 简体中文：显示片段，然后隐藏其他片段。
     *
     * @param show The fragment will be show.
     * @param hide The fragment will be hide.
     */
    public static void showHide(@NonNull final Fragment show, @NonNull final Fragment... hide) {
        for (Fragment fragment : hide) {
            putArgs(fragment, fragment != show);
        }
        operateNoAnim(show.getFragmentManager(), TYPE_SHOW_HIDE_FRAGMENT, show, hide);
    }

    /**
     * Show fragment then hide other fragment.
     * 简体中文：显示片段，然后隐藏其他片段。
     *
     * @param show The fragment will be show.
     * @param hide The fragment will be hide.
     */
    public static void showHide(@NonNull final Fragment show,
                                @NonNull final Fragment hide) {
        putArgs(show, false);
        putArgs(hide, true);
        operateNoAnim(show.getFragmentManager(), TYPE_SHOW_HIDE_FRAGMENT, show, hide);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment) {
        replace(srcFragment, destFragment, null, false);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final boolean isAddStack) {
        replace(srcFragment, destFragment, null, isAddStack);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(srcFragment, destFragment, null, false, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(srcFragment, destFragment, null, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        replace(srcFragment, destFragment, null, false,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        replace(srcFragment, destFragment, null, isAddStack,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment    The source of fragment.
     * @param destFragment   The destination of fragment.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final View... sharedElements) {
        replace(srcFragment, destFragment, null, false, sharedElements);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment    The source of fragment.
     * @param destFragment   The destination of fragment.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final boolean isAddStack,
                               final View... sharedElements) {
        replace(srcFragment, destFragment, null, isAddStack, sharedElements);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param fragment    The new fragment to place in the container.
     * @param containerId The id of container.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId) {
        replace(fm, fragment, containerId, null, false);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param containerId The id of container.
     * @param fragment    The new fragment to place in the container.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final boolean isAddStack) {
        replace(fm, fragment, containerId, null, isAddStack);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param containerId The id of container.
     * @param fragment    The new fragment to place in the container.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(fm, fragment, containerId, null, false, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param containerId The id of container.
     * @param fragment    The new fragment to place in the container.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(fm, fragment, containerId, null, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm           The manager of fragment.
     * @param containerId  The id of container.
     * @param fragment     The new fragment to place in the container.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        replace(fm, fragment, containerId, null, false,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm           The manager of fragment.
     * @param containerId  The id of container.
     * @param fragment     The new fragment to place in the container.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        replace(fm, fragment, containerId, null, isAddStack,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm             The manager of fragment.
     * @param containerId    The id of container.
     * @param fragment       The new fragment to place in the container.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final View... sharedElements) {
        replace(fm, fragment, containerId, null, false, sharedElements);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm             The manager of fragment.
     * @param containerId    The id of container.
     * @param fragment       The new fragment to place in the container.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final boolean isAddStack,
                               final View... sharedElements) {
        replace(fm, fragment, containerId, null, isAddStack, sharedElements);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag) {
        replace(srcFragment, destFragment, destTag, false);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               final boolean isAddStack) {
        FragmentManager fm = srcFragment.getFragmentManager();
        if (fm == null) return;
        Args args = getArgs(srcFragment);
        replace(fm, destFragment, args.id, destTag, isAddStack);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(srcFragment, destFragment, destTag, false, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(srcFragment, destFragment, destTag, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        replace(srcFragment, destFragment, destTag, false,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        FragmentManager fm = srcFragment.getFragmentManager();
        if (fm == null) return;
        Args args = getArgs(srcFragment);
        replace(fm, destFragment, args.id, destTag, isAddStack,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment    The source of fragment.
     * @param destFragment   The destination of fragment.
     * @param destTag        The destination of fragment's tag.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               final View... sharedElements) {
        replace(srcFragment, destFragment, destTag, false, sharedElements);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param srcFragment    The source of fragment.
     * @param destFragment   The destination of fragment.
     * @param destTag        The destination of fragment's tag.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final Fragment srcFragment,
                               @NonNull final Fragment destFragment,
                               final String destTag,
                               final boolean isAddStack,
                               final View... sharedElements) {
        FragmentManager fm = srcFragment.getFragmentManager();
        if (fm == null) return;
        Args args = getArgs(srcFragment);
        replace(fm,
                destFragment,
                args.id,
                destTag,
                isAddStack,
                sharedElements
        );
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param fragment    The new fragment to place in the container.
     * @param containerId The id of container.
     * @param destTag     The destination of fragment's tag.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag) {
        replace(fm, fragment, containerId, destTag, false);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param fragment    The new fragment to place in the container.
     * @param containerId The id of container.
     * @param destTag     The destination of fragment's tag.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               final boolean isAddStack) {
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(fragment, new Args(containerId, destTag, false, isAddStack));
        operate(TYPE_REPLACE_FRAGMENT, fm, ft, null, fragment);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param fragment    The new fragment to place in the container.
     * @param containerId The id of container.
     * @param destTag     The destination of fragment's tag.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(fm, fragment, containerId, destTag, false, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm          The manager of fragment.
     * @param fragment    The new fragment to place in the container.
     * @param containerId The id of container.
     * @param destTag     The destination of fragment's tag.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     *                    view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     *                    view of the fragment being removed or detached.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim) {
        replace(fm, fragment, containerId, destTag, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm           The manager of fragment.
     * @param fragment     The new fragment to place in the container.
     * @param containerId  The id of container.
     * @param destTag      The destination of fragment's tag.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        replace(fm, fragment, containerId, destTag, false,
                enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm           The manager of fragment.
     * @param fragment     The new fragment to place in the container.
     * @param containerId  The id of container.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     *                     view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being readded or reattached caused by
     *                     popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     *                     view of the fragment being removed or detached caused by
     *                     popBackStack() or similar methods.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               final boolean isAddStack,
                               @AnimatorRes @AnimRes final int enterAnim,
                               @AnimatorRes @AnimRes final int exitAnim,
                               @AnimatorRes @AnimRes final int popEnterAnim,
                               @AnimatorRes @AnimRes final int popExitAnim) {
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(fragment, new Args(containerId, destTag, false, isAddStack));
        addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim);
        operate(TYPE_REPLACE_FRAGMENT, fm, ft, null, fragment);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm             The manager of fragment.
     * @param fragment       The new fragment to place in the container.
     * @param containerId    The id of container.
     * @param destTag        The destination of fragment's tag.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               final View... sharedElements) {
        replace(fm, fragment, containerId, destTag, false, sharedElements);
    }

    /**
     * Replace fragment.
     * 简体中文：替换片段。
     *
     * @param fm             The manager of fragment.
     * @param fragment       The new fragment to place in the container.
     * @param containerId    The id of container.
     * @param destTag        The destination of fragment's tag.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     *                       appearing Fragment.
     */
    public static void replace(@NonNull final FragmentManager fm,
                               @NonNull final Fragment fragment,
                               @IdRes final int containerId,
                               final String destTag,
                               final boolean isAddStack,
                               final View... sharedElements) {
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(fragment, new Args(containerId, destTag, false, isAddStack));
        addSharedElement(ft, sharedElements);
        operate(TYPE_REPLACE_FRAGMENT, fm, ft, null, fragment);
    }

    /**
     * Pop fragment.
     * 简体中文：弹出片段。
     *
     * @param fm The manager of fragment.
     */
    public static void pop(@NonNull final FragmentManager fm) {
        pop(fm, true);
    }

    /**
     * Pop fragment.
     * 简体中文：弹出片段。
     *
     * @param fm          The manager of fragment.
     * @param isImmediate True to pop_2item immediately, false otherwise.
     */
    public static void pop(@NonNull final FragmentManager fm,
                           final boolean isImmediate) {
        if (isImmediate) {
            fm.popBackStackImmediate();
        } else {
            fm.popBackStack();
        }
    }

    /**
     * Pop to fragment.
     * 简体中文：弹出片段。
     *
     * @param fm            The manager of fragment.
     * @param popClz        The class of fragment will be popped to.
     * @param isIncludeSelf True to include the fragment, false otherwise.
     */
    public static void popTo(@NonNull final FragmentManager fm,
                             final Class<? extends Fragment> popClz,
                             final boolean isIncludeSelf) {
        popTo(fm, popClz, isIncludeSelf, true);
    }

    /**
     * Pop to fragment.
     * 简体中文：弹出片段。
     *
     * @param fm            The manager of fragment.
     * @param popClz        The class of fragment will be popped to.
     * @param isIncludeSelf True to include the fragment, false otherwise.
     * @param isImmediate   True to pop_2item immediately, false otherwise.
     */
    public static void popTo(@NonNull final FragmentManager fm,
                             final Class<? extends Fragment> popClz,
                             final boolean isIncludeSelf,
                             final boolean isImmediate) {
        if (isImmediate) {
            fm.popBackStackImmediate(popClz.getName(),
                    isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
        } else {
            fm.popBackStack(popClz.getName(),
                    isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
        }
    }

    /**
     * Pop all fragments.
     * 简体中文：弹出所有片段。
     *
     * @param fm The manager of fragment.
     */
    public static void popAll(@NonNull final FragmentManager fm) {
        popAll(fm, true);
    }

    /**
     * Pop all fragments.
     * 简体中文：弹出所有片段。
     *
     * @param fm The manager of fragment.
     */
    public static void popAll(@NonNull final FragmentManager fm, final boolean isImmediate) {
        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(0);
            if (isImmediate) {
                fm.popBackStackImmediate(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fm.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    /**
     * Remove fragment.
     * 简体中文：移除片段。
     *
     * @param remove The fragment will be removed.
     */
    public static void remove(@NonNull final Fragment remove) {
        operateNoAnim(remove.getFragmentManager(), TYPE_REMOVE_FRAGMENT, null, remove);
    }

    /**
     * Remove to fragment.
     * 简体中文：移除片段。
     *
     * @param removeTo      The fragment will be removed to.
     * @param isIncludeSelf True to include the fragment, false otherwise.
     */
    public static void removeTo(@NonNull final Fragment removeTo, final boolean isIncludeSelf) {
        operateNoAnim(removeTo.getFragmentManager(), TYPE_REMOVE_TO_FRAGMENT,
                isIncludeSelf ? removeTo : null, removeTo);
    }

    /**
     * Remove all fragments.
     * 简体中文：移除所有片段。
     *
     * @param fm The manager of fragment.
     */
    public static void removeAll(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = getFragments(fm);
        operateNoAnim(fm,
                TYPE_REMOVE_FRAGMENT,
                null,
                fragments.toArray(new Fragment[fragments.size()])
        );
    }

    private static void putArgs(final Fragment fragment, final Args args) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        bundle.putInt(ARGS_ID, args.id);
        bundle.putBoolean(ARGS_IS_HIDE, args.isHide);
        bundle.putBoolean(ARGS_IS_ADD_STACK, args.isAddStack);
        bundle.putString(ARGS_TAG, args.tag);
    }

    private static void putArgs(final Fragment fragment, final boolean isHide) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        bundle.putBoolean(ARGS_IS_HIDE, isHide);
    }

    private static Args getArgs(final Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) bundle = Bundle.EMPTY;
        return new Args(bundle.getInt(ARGS_ID, fragment.getId()),
                bundle.getBoolean(ARGS_IS_HIDE),
                bundle.getBoolean(ARGS_IS_ADD_STACK));
    }

    private static void operateNoAnim(@Nullable final FragmentManager fm,
                                      final int type,
                                      final Fragment src,
                                      Fragment... dest) {
        if (fm == null) return;
        FragmentTransaction ft = fm.beginTransaction();
        operate(type, fm, ft, src, dest);
    }

    private static void operate(final int type,
                                @NonNull final FragmentManager fm,
                                final FragmentTransaction ft,
                                final Fragment src,
                                final Fragment... dest) {
        if (src != null && src.isRemoving()) {
            Log.e("FragmentUtils", src.getClass().getName() + " is isRemoving");
            return;
        }

        String name;
        Bundle args;
        switch (type) {
            case TYPE_ADD_FRAGMENT:
                for (Fragment fragment : dest) {
                    args = fragment.getArguments();
                    if (args == null) return;
                    name = args.getString(ARGS_TAG, fragment.getClass().getName());
                    Fragment fragmentByTag = fm.findFragmentByTag(name);
                    if (fragmentByTag != null && fragmentByTag.isAdded()) {
                        ft.remove(fragmentByTag);
                    }
                    ft.add(args.getInt(ARGS_ID), fragment, name);
                    if (args.getBoolean(ARGS_IS_HIDE)) ft.hide(fragment);
                    if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name);
                }
                break;
            case TYPE_HIDE_FRAGMENT:
                for (Fragment fragment : dest) {
                    ft.hide(fragment);
                }
                break;
            case TYPE_SHOW_FRAGMENT:
                for (Fragment fragment : dest) {
                    ft.show(fragment);
                }
                break;
            case TYPE_SHOW_HIDE_FRAGMENT:
                ft.show(src);
                for (Fragment fragment : dest) {
                    if (fragment != src) {
                        ft.hide(fragment);
                    }
                }
                break;
            case TYPE_REPLACE_FRAGMENT:
                args = dest[0].getArguments();
                if (args == null) return;
                name = args.getString(ARGS_TAG, dest[0].getClass().getName());
                ft.replace(args.getInt(ARGS_ID), dest[0], name);
                if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name);
                break;
            case TYPE_REMOVE_FRAGMENT:
                for (Fragment fragment : dest) {
                    if (fragment != src) {
                        ft.remove(fragment);
                    }
                }
                break;
            case TYPE_REMOVE_TO_FRAGMENT:
                for (int i = dest.length - 1; i >= 0; --i) {
                    Fragment fragment = dest[i];
                    if (fragment == dest[0]) {
                        if (src != null) ft.remove(fragment);
                        break;
                    }
                    ft.remove(fragment);
                }
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private static void addAnim(final FragmentTransaction ft,
                                final int enter,
                                final int exit,
                                final int popEnter,
                                final int popExit) {
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
    }

    private static void addSharedElement(final FragmentTransaction ft,
                                         final View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (View view : views) {
                ft.addSharedElement(view, view.getTransitionName());
            }
        }
    }

    /**
     * Return the top fragment.
     * 简体中文：返回顶部片段。
     *
     * @param fm The manager of fragment.
     * @return the top fragment
     */
    public static Fragment getTop(@NonNull final FragmentManager fm) {
        return getTopIsInStack(fm, false);
    }

    /**
     * Return the top fragment in stack.
     * 简体中文：返回堆栈中的顶部片段。
     *
     * @param fm The manager of fragment.
     * @return the top fragment in stack
     */
    public static Fragment getTopInStack(@NonNull final FragmentManager fm) {
        return getTopIsInStack(fm, true);
    }

    private static Fragment getTopIsInStack(@NonNull final FragmentManager fm,
                                            final boolean isInStack) {
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; --i) {
            Fragment fragment = fragments.get(i);
            if (fragment != null) {
                if (isInStack) {
                    Bundle args = fragment.getArguments();
                    if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                        return fragment;
                    }
                } else {
                    return fragment;
                }
            }
        }
        return null;
    }

    /**
     * Return the top fragment which is shown.
     * 简体中文：返回当前显示的顶部片段。
     *
     * @param fm The manager of fragment.
     * @return the top fragment which is shown
     */
    public static Fragment getTopShow(@NonNull final FragmentManager fm) {
        return getTopShowIsInStack(fm, false);
    }

    /**
     * Return the top fragment which is shown in stack.
     * 简体中文：返回在堆栈中显示的顶部片段。
     *
     * @param fm The manager of fragment.
     * @return the top fragment which is shown in stack
     */
    public static Fragment getTopShowInStack(@NonNull final FragmentManager fm) {
        return getTopShowIsInStack(fm, true);
    }

    private static Fragment getTopShowIsInStack(@NonNull final FragmentManager fm,
                                                final boolean isInStack) {
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; --i) {
            Fragment fragment = fragments.get(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                if (fragment != null
                        && fragment.isResumed()
                        && fragment.isVisible()
                        && fragment.getUserVisibleHint()) {
                    if (isInStack) {
                        Bundle args = fragment.getArguments();
                        if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                            return fragment;
                        }
                    } else {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Return the fragments in manager.
     * 简体中文：返回管理器中的片段。
     *
     * @param fm The manager of fragment.
     * @return the fragments in manager
     */
    public static List<Fragment> getFragments(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fragments = fm.getFragments();
        }
        if (fragments == null || fragments.isEmpty()) return Collections.emptyList();
        return fragments;
    }

    /**
     * Return the fragments in stack in manager.
     * 简体中文：返回管理器中堆栈中的片段。
     *
     * @param fm The manager of fragment.
     * @return the fragments in stack in manager
     */
    public static List<Fragment> getFragmentsInStack(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = getFragments(fm);
        List<Fragment> result = new ArrayList<>();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                Bundle args = fragment.getArguments();
                if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                    result.add(fragment);
                }
            }
        }
        return result;
    }

    /**
     * Return all fragments in manager.
     * 简体中文：返回管理器中的所有片段。
     *
     * @param fm The manager of fragment.
     * @return all fragments in manager
     */
    public static List<FragmentNode> getAllFragments(@NonNull final FragmentManager fm) {
        return getAllFragments(fm, new ArrayList<FragmentNode>());
    }

    private static List<FragmentNode> getAllFragments(@NonNull final FragmentManager fm,
                                                      final List<FragmentNode> result) {
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; --i) {
            Fragment fragment = fragments.get(i);
            if (fragment != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    result.add(new FragmentNode(fragment,
                            getAllFragments(fragment.getChildFragmentManager(),
                                    new ArrayList<FragmentNode>())));
                }
            }
        }
        return result;
    }

    /**
     * Return all fragments in stack in manager.
     * 简体中文：在管理器中返回堆栈中的所有片段。
     *
     * @param fm The manager of fragment.
     * @return all fragments in stack in manager
     */
    public static List<FragmentNode> getAllFragmentsInStack(@NonNull final FragmentManager fm) {
        return getAllFragmentsInStack(fm, new ArrayList<FragmentNode>());
    }

    private static List<FragmentNode> getAllFragmentsInStack(@NonNull final FragmentManager fm,
                                                             final List<FragmentNode> result) {
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; --i) {
            Fragment fragment = fragments.get(i);
            if (fragment != null) {
                Bundle args = fragment.getArguments();
                if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        result.add(new FragmentNode(fragment,
                                getAllFragmentsInStack(fragment.getChildFragmentManager(),
                                        new ArrayList<FragmentNode>())));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Find fragment.
     * 简体中文：查找片段。
     *
     * @param fm      The manager of fragment.
     * @param findClz The class of fragment will be found.
     * @return the fragment matches class
     */
    public static Fragment findFragment(@NonNull final FragmentManager fm,
                                        final Class<? extends Fragment> findClz) {
        return fm.findFragmentByTag(findClz.getName());
    }

    /**
     * Find fragment.
     * 简体中文：查找片段。
     *
     * @param fm  The manager of fragment.
     * @param tag The tag of fragment will be found.
     * @return the fragment matches class
     */
    public static Fragment findFragment(@NonNull final FragmentManager fm,
                                        @NonNull final String tag) {
        return fm.findFragmentByTag(tag);
    }

    /**
     * Dispatch the back press for fragment.
     * 简体中文：分发返回按钮按下事件给片段。
     *
     * @param fragment The fragment.
     * @return {@code true}: the fragment consumes the back press<br>{@code false}: otherwise
     */
    public static boolean dispatchBackPress(@NonNull final Fragment fragment) {
        return fragment.isResumed()
                && fragment.isVisible()
                && fragment.getUserVisibleHint()
                && fragment instanceof OnBackClickListener
                && ((OnBackClickListener) fragment).onBackClick();
    }

    /**
     * Dispatch the back press for fragment.
     * 简体中文：分发返回按钮按下事件给片段。
     *
     * @param fm The manager of fragment.
     * @return {@code true}: the fragment consumes the back press<br>{@code false}: otherwise
     */
    public static boolean dispatchBackPress(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = getFragments(fm);
        if (fragments == null || fragments.isEmpty()) return false;
        for (int i = fragments.size() - 1; i >= 0; --i) {
            Fragment fragment = fragments.get(i);
            if (fragment != null
                    && fragment.isResumed()
                    && fragment.isVisible()
                    && fragment.getUserVisibleHint()
                    && fragment instanceof OnBackClickListener
                    && ((OnBackClickListener) fragment).onBackClick()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set background color for fragment.
     * 简体中文：为片段设置背景资源。
     *
     * @param fragment The fragment.
     * @param color    The background color.
     */
    public static void setBackgroundColor(@NonNull final Fragment fragment,
                                          @ColorInt final int color) {
        View view = fragment.getView();
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    /**
     * Set background resource for fragment.
     * 简体中文：为片段设置背景资源。
     *
     * @param fragment The fragment.
     * @param resId    The resource id.
     */
    public static void setBackgroundResource(@NonNull final Fragment fragment,
                                             @DrawableRes final int resId) {
        View view = fragment.getView();
        if (view != null) {
            view.setBackgroundResource(resId);
        }
    }

    /**
     * Set background color for fragment.
     * 简体中文：为片段设置背景颜色。
     *
     * @param fragment   The fragment.
     * @param background The background.
     */
    public static void setBackground(@NonNull final Fragment fragment, final Drawable background) {
        View view = fragment.getView();
        if (view == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * Return the simple name of fragment.
     * 简体中文：返回片段的简单名称。
     *
     * @param fragment The fragment.
     * @return the simple name of fragment
     */
    public static String getSimpleName(final Fragment fragment) {
        return fragment == null ? "null" : fragment.getClass().getSimpleName();
    }

    public interface OnBackClickListener {
        boolean onBackClick();
    }

    private static class Args {
        final int id;
        final boolean isHide;
        final boolean isAddStack;
        final String tag;

        Args(final int id, final boolean isHide, final boolean isAddStack) {
            this(id, null, isHide, isAddStack);
        }

        Args(final int id, final String tag,
             final boolean isHide, final boolean isAddStack) {
            this.id = id;
            this.tag = tag;
            this.isHide = isHide;
            this.isAddStack = isAddStack;
        }
    }

    public static class FragmentNode {
        final Fragment fragment;
        final List<FragmentNode> next;

        public FragmentNode(final Fragment fragment, final List<FragmentNode> next) {
            this.fragment = fragment;
            this.next = next;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public List<FragmentNode> getNext() {
            return next;
        }

        @Override
        public String toString() {
            return fragment.getClass().getSimpleName()
                    + "->"
                    + ((next == null || next.isEmpty()) ? "no child" : next.toString());
        }
    }
}
