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

import dora.crash.group.Group;
import dora.crash.policy.CrashReportPolicy;

/**
 * It is designed to collect exceptions thrown by the application.
 * 专门用来收集应用抛出的异常。
 */
public class CrashCollector extends Collector<CrashInfo, CrashReportPolicy> {

    private CrashInfo mInfo;

    @Override
    public void collect(CrashInfo info) {
        this.mInfo = info;
    }

    @Override
    public void report(CrashReportPolicy policy) {
        reportCrash(policy, policy.getGroup());
    }

    private void reportCrash(CrashReportPolicy policy, Group group) {
        policy.report(mInfo, group);
    }
}
