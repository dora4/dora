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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Developers can extend this custom crash information so that the toString() method must be
 * overridden to take effect.
 * 简体中文：开发者可以扩展此类来自定义崩溃信息，这样的话，必须重写toString()方法生效。
 */
public class CrashInfo implements Info {

    private String versionName; // 版本名称

    private int versionCode;    // 版本号

    private int sdkVersion; // SDK版本号

    private String release; // Android版本号

    private String model;   // 手机型号

    private String brand;   // 手机制造商

    private Thread thread;  // 崩溃线程

    private Throwable throwable;    // 崩溃异常信息
    private Context context;

    public CrashInfo(Context context) {
        this.context = context;
        // Gets some information about mobile phone.
        // 简体中文：获取手机的一些信息
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo;
        try {
            pkgInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = pkgInfo.versionName;
            versionCode = pkgInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "unknown";
            versionCode = -1;
        }
        sdkVersion = Build.VERSION.SDK_INT;
        release = Build.VERSION.RELEASE;
        model = Build.MODEL;
        brand = Build.MANUFACTURER;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public String getRelease() {
        return release;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setThrowable(Throwable e) {
        this.throwable = e;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getException() {
        if (throwable != null) {
            return buildStackTrace(throwable.getStackTrace());
        }
        return "";
    }

    public Context getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "\nCrash线程：" + thread.getName() + "#" + thread.getId()
                + "\n手机型号：" + model
                + "\n手机品牌：" + brand
                + "\nSDK版本：" + sdkVersion
                + "\nAndroid版本：" + release
                + "\n版本名称：" + versionName
                + "\n版本号：" + versionCode
                + "\n异常信息：" + throwable.toString()
                + getException();
    }

    private String buildStackTrace(StackTraceElement[] lines) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement line : lines) {
            sb.append("\n").append("at ").append(line.getClassName()).append(".").append(line.getMethodName())
                    .append("(").append(line.getFileName() + ":" + line.getLineNumber()).append(")");
        }
        return sb.toString();
    }
}