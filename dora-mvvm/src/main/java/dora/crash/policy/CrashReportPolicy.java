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

package dora.crash.policy;

import dora.crash.CrashInfo;
import dora.crash.group.Group;

/**
 * A channel used to represent crash information.
 * 简体中文：用来表示崩溃信息的通道。
 */
public abstract class CrashReportPolicy extends PolicyWrapper<CrashInfo, CrashReportPolicy> {

    protected CrashReportPolicy(Group group, CrashReportPolicy policy) {
        super(group, policy);
    }
}