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

package dora.crash.group;

/**
 * Groups are used to distribute policies.
 * 分组被用来分发策略。
 */
public interface Group {

    /**
     * Through calculation, determine whether the group of rules.
     * 通过计算，判断是否符合该组规则。
     */
    boolean counts();

    /**
     * The name of group.
     * 分组的名称。
     */
    String name();
}
