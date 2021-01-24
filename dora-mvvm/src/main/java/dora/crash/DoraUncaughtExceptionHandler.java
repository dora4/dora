/*
 * Copyright (C) 2020 The Dora Open Source Project
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

package dora.crash;

import android.content.Context;
import android.os.Process;

/**
 * It is used to intercept all exceptions thrown by the application.
 * 它被用于拦截应用抛出的所有异常。
 */
class DoraUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private DoraConfig mConfig;

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    private static DoraUncaughtExceptionHandler sInstance
            = new DoraUncaughtExceptionHandler();

    private DoraUncaughtExceptionHandler() {
    }

    static DoraUncaughtExceptionHandler getInstance() {
        return sInstance;
    }

    void init(Context context, DoraConfig config) {
        this.mContext = context.getApplicationContext();
        this.mConfig = config;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public Context getContext() {
        return mContext;
    }

    public void uncaughtException(Thread t, Throwable e) {
        if (mConfig.enabled) {
            boolean filterResult = mConfig.filter.filterCrashInfo(mConfig.info);
            if (filterResult) {
                interceptException(t, e);
            }
        }
        if (!mConfig.interceptCrash) {
            // If the Android system provides an exception handling class, it shall have handled
            // by the system.
            // 如果系统提供了异常处理类，则交给系统去处理
            if (mDefaultExceptionHandler != null) {
                mDefaultExceptionHandler.uncaughtException(t, e);
            } else {
                // Otherwise we handle it ourselves, we handle it ourselves usually by letting
                // the app exit
                // 否则我们自己处理，自己处理通常是让app退出
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }
    }

    public void interceptException(Thread t, Throwable e) {
        // Collect exception information and do our own handling
        // 收集异常信息，做我们自己的处理
        Collector collector = new CrashCollector();
        CrashInfo info = mConfig.info;
        info.setThrowable(e);
        info.setThread(t);
        collector.collect(info);
        collector.report(mConfig.policy);
    }
}