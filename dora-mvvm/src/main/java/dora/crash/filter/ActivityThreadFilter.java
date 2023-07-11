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

package dora.crash.filter;

import dora.crash.CrashInfo;

/**
 * Example for custom filter, a main thread filter.
 * 简体中文：自定义过滤器示范，主线程过滤器。
 */
public class ActivityThreadFilter extends CrashReportFilter {

    @Override
    public boolean handle(CrashInfo info) {
        String name = info.getThread().getName();
        // Only handle crash information for the main thread.
        // 简体中文：只处理主线程的崩溃信息
        if (name.equals("main")) {
            return true;
        }
        return false;
    }
}
