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

import dora.crash.policy.Policy;

/**
 * Abstract procedure of collecting and reporting crash information.
 * 抽象了收集和上报崩溃信息的过程。
 */
public abstract class Collector<I extends Info, P extends Policy> {

    /**
     * Collects crash info, just cache the crash information for Collector.
     * 收集崩溃信息，仅仅将CrashInfo缓存在Collector。
     */
    public abstract void collect(I info);

    /**
     * Feed back the crash information collected to the developer as needed.
     * 根据需要将收集到的崩溃信息反馈给开发者。
     *
     * @param policy 崩溃报告的方针/策略
     */
    public abstract void report(P policy);
}