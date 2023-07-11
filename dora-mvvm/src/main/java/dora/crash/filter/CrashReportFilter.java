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
 * At the beginning, affects whether the crash info will report.
 * 简体中文：在一开始的时候，影响崩溃信息是否会上报。
 */
public abstract class CrashReportFilter {

    private CrashReportFilter mNext;

    public void setNextFilter(CrashReportFilter filter) {
        this.mNext = filter;
    }

    public CrashReportFilter nextFilter() {
        return mNext;
    }

    public boolean filterCrashInfo(CrashInfo info) {
        boolean result = handle(info);
        if (result && mNext != null) {
            return mNext.filterCrashInfo(info);
        } else {
            return result;
        }
    }

    public abstract boolean handle(CrashInfo info);
}
