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

import dora.crash.LogInfo;
import dora.crash.group.Group;

/**
 * A policy for sending log information.
 * 简体中文：发送日志信息的策略。
 */
public abstract class LogReportPolicy extends PolicyWrapper<LogInfo, LogReportPolicy> {

    protected LogReportPolicy(Group group, LogReportPolicy policy) {
        super(group, policy);
    }
}
