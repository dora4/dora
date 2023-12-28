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

package dora.lifecycle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import dora.util.LogUtils;

public class FragmentDelegateImpl implements FragmentDelegate {

    private Fragment mFragment;

    public FragmentDelegateImpl(@NonNull Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        LogUtils.iformat(LogUtils.TAG, "%s - onAttach", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onCreate", mFragment.getClass().getSimpleName());
        // Save this fragment during configuration changes and reuse the created fragment when the
        // activity is recreated due to configuration changes.
        // If using the <Fragment/> tag in XML to create the fragment, be sure to include the
        // android:id or android:tag attribute in the tag. Otherwise, setRetainInstance(true) will
        // not work.It is recommended to set this parameter when binding a small number of fragments
        // in an activity. If you need to bind a large number of fragments, it is not recommended to
        // set this parameter, such as when using a ViewPager to display multiple fragments.
        // 简体中文：在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的
        // Fragment。https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
        // 如果在XML中使用 <Fragment/> 标签的方式创建Fragment请务必在标签中加上 android:id 或者
        // android:tag 属性，否则setRetainInstance(true)无效在Activity中绑定少量的Fragment建议这样做，
        // 如果需要绑定较多的Fragment不建议设置此参数，如ViewPager需要展示较多Fragment
        mFragment.setRetainInstance(true);
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onCreateView", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onActivityCreate", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStart", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        LogUtils.iformat(LogUtils.TAG, "%s - onResume", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        LogUtils.iformat(LogUtils.TAG, "%s - onPause", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStop", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onSaveInstanceState", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDestroyView", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDestroy", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDetach", mFragment.getClass().getSimpleName());
        this.mFragment = null;
    }

    /**
     * Return true if the fragment is currently added to its activity.
     * 简体中文：如果Fragment当前已经添加到其Activity中，则返回true。
     */
    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
