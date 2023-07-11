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

import java.util.Calendar;

/**
 * Filter crash information by time.
 * 简体中文：按时间过滤崩溃信息。
 */
public class TimeFilter extends CrashReportFilter {

    @Override
    public boolean handle(CrashInfo info) {
        // 24-hour
        // 简体中文：24小时制
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        // Only process logs during office hours
        // 简体中文：只处理上班时间的日志
        if (hour >= 8 && hour < 20) {
            return true;
        }
        return false;
    }
}
