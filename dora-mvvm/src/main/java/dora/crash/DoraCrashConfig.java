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
import dora.crash.filter.CrashReportFilter;
import dora.crash.filter.DefaultFilter;
import dora.crash.policy.CrashReportPolicy;
import dora.crash.policy.StoragePolicy;

/**
 * Configuration of global crash information collection, as well as partial log information
 * collection.
 * 简体中文：全局崩溃信息收集的配置，也包括部分日志信息收集的配置。
 */
public class DoraCrashConfig {

    // Crash information reporting strategy, providing built-in strategies such as local storage,
    // email reception, web page viewing, etc., and also allowing customization.
    // 简体中文：崩溃信息上报策略，提供本地存储、邮件接收、网页查看等内置策略，也可自定义
    CrashReportPolicy policy;

    // Decide whether to report the crash information or not.
    // 简体中文：决定是否要上报该次崩溃信息
    CrashReportFilter filter;
    // Include the content of the crash information.
    // 简体中文：包含崩溃信息内容
    CrashInfo info;
    // Whether to enable crash log collection feature.
    // 简体中文：是否开启崩溃日志收集功能
    boolean enabled;
    // After collecting crash information, whether to allow the application to crash abruptly.
    // If "true," the application will not crash abruptly.
    // 简体中文：收集崩溃信息后，是否让应用闪退，true则不闪退
    boolean interceptCrash;

    public DoraCrashConfig(Builder builder) {
        policy = builder.policy;
        filter = builder.filter;
        info = builder.info;
        enabled = builder.enabled;
        interceptCrash = builder.interceptCrash;
    }

    public static class Builder {

        CrashReportPolicy policy = new StoragePolicy();
        CrashReportFilter filter = new DefaultFilter();
        CrashInfo info;
        boolean enabled = true;
        boolean interceptCrash;
        Context context;

        public Builder(Context context) {
            this.context = context;
            this.info = new CrashInfo(context);
        }

        public Builder crashReportPolicy(CrashReportPolicy policy) {
            this.policy = policy;
            return this;
        }

        // Either you create a CrashReportFilter directly, or you use the method named
        // "CrashReportFilterChain#getFilter()".
        // 简体中文：要么你直接创建一个CrashReportFilter，要么你使用CrashReportFilterChain的getFilter()方法。
        public Builder filterChain(CrashReportFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder crashInfo(CrashInfo info) {
            this.info = info;
            return this;
        }

        // Determines if you want to activate all functions of this framework.
        // 简体中文：确定你是否想要激活属于这个框架的所有功能。
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder interceptCrash(boolean interceptCrash) {
            this.interceptCrash = interceptCrash;
            return this;
        }

        public DoraCrashConfig build() {
            DoraCrashConfig config = new DoraCrashConfig(this);
            if (context != null) {
                DoraUncaughtExceptionHandler.getInstance().init(context, config);
            }
            return config;
        }
    }
}
