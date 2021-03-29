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

import androidx.annotation.CallSuper;
import dora.crash.Info;
import dora.crash.group.Group;

/**
 * Wrap it so that various strategies can be combined freely, the innermost policy is invoked first,
 * and so on. To extend this class, override the report() method.
 * 包装一下，便于多种策略自由组合，最里面的策略最先被执行，以此类推，扩展此类请重写report()方法。
 */
public abstract class PolicyWrapper<I extends Info, P extends Policy> implements Policy<I> {

    private Group mGroup;   //策略自己的分组
    private P mBasePolicy;

    protected PolicyWrapper(Group group, P policy) {
        this.mGroup = group;
        this.mBasePolicy = policy;
    }

    @CallSuper
    @Override
    public void report(I info, Group group) {
        if (mBasePolicy != null) {
            mBasePolicy.report(info, mBasePolicy.getGroup());
        }
    }

    @Override
    public Group getGroup() {
        return mGroup;
    }
}
