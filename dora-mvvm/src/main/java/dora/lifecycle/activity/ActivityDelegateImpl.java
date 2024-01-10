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

package dora.lifecycle.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import dora.util.LogUtils;

public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;

    public ActivityDelegateImpl(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onCreate", mActivity.getLocalClassName());
    }

    @Override
    public void onStart() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStart", mActivity.getLocalClassName());
    }

    @Override
    public void onResume() {
        LogUtils.iformat(LogUtils.TAG, "%s - onResume", mActivity.getLocalClassName());
    }

    @Override
    public void onPause() {
        LogUtils.iformat(LogUtils.TAG, "%s - onPause", mActivity.getLocalClassName());
    }

    @Override
    public void onStop() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStop", mActivity.getLocalClassName());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onSaveInstanceState", mActivity.getLocalClassName());
    }

    @Override
    public void onDestroy() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDestroy", mActivity.getLocalClassName());
        this.mActivity = null;
    }
}
