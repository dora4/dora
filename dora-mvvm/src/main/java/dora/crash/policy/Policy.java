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

import dora.crash.Info;
import dora.crash.group.Group;

/**
 * Strategies. You can use different strategies to distribute information.
 * 策略，你可以使用不同的策略来分发信息。
 *
 * @param <I>
 */
public interface Policy<I extends Info> {

    void report(I info, Group group);

    Group getGroup();
}
