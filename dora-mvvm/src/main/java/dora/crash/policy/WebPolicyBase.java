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
import dora.crash.group.DefaultGroup;
import dora.crash.group.Group;

/**
 * You can use it to customize the implementation of collecting crash information to a web page.
 * 简体中文：你可以用它自定义收集崩溃信息到网页的实现。
 */
public abstract class WebPolicyBase extends CrashReportPolicy {

    private String mRequestUrl;

    protected WebPolicyBase(String url) {
        this(url, new DefaultGroup());
    }

    protected WebPolicyBase(String url, CrashReportPolicy policy) {
        this(url, new DefaultGroup(), policy);
    }

    protected WebPolicyBase(String url, Group group) {
        super(group, null);
        this.mRequestUrl = url;
    }

    protected WebPolicyBase(String url, Group group, CrashReportPolicy policy) {
        super(group, policy);
        this.mRequestUrl = url;
    }

    @Override
    public void report(CrashInfo info, Group group) {
        super.report(info, group);
        sendCrashInfoToWeb(mRequestUrl, info, group);
    }

    /**
     * Sends crash log information to the remote server.
     * 简体中文：把崩溃日志信息发送到远端服务器。
     */
    public abstract void sendCrashInfoToWeb(String url, CrashInfo info, Group group);
}
