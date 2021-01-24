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

import dora.crash.policy.LogFilePolicy;
import dora.crash.policy.LogReportPolicy;
import dora.crash.policy.Policy;

/**
 * A system that controls log output globally. When flag is closed, you can't output logs anywhere.
 * Instead, you can output logs anywhere.<note>The log system is opened by default.</note>
 * 控制全局日志输出的系统。当关闭标志时，您不能将日志输出到任何地方。
 * 相反，您可以将日志输出到任何地方。注意：日志系统默认开启。
 */
public class DoraLog {

    private static DoraLog CHANNEL = new DoraLog();
    private Collector mCollector;
    private Policy mLogPolicy;

    private DoraLog() {
        mCollector = new LogCollector();
        mLogPolicy = new LogFilePolicy();
    }

    private DoraLog(LogReportPolicy policy) {
        this();
        setLogPolicy(policy);
    }

    public void setLogPolicy(LogReportPolicy policy) {
        this.mLogPolicy = policy;
    }

    public static DoraLog getChannel() {
        return CHANNEL;
    }

    private void _print(LogInfo info) {
        mCollector.collect(info);
        mCollector.report(mLogPolicy);
    }

    public static void print(LogInfo info) {
        CHANNEL._print(info);
    }

    public static void print(String log) {
        print(new LogInfo(log));
    }
}