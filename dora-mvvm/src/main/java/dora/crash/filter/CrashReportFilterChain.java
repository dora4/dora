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

import java.util.LinkedList;
import java.util.List;

/**
 * It is used to combine the superposition of two or more filters.
 * 通过它来组合两种或两种以上的过滤器的叠加。
 */
public class CrashReportFilterChain {

    private List<CrashReportFilter> mFilters;

    public CrashReportFilterChain() {
        mFilters = new LinkedList<CrashReportFilter>();
    }

    public CrashReportFilterChain addFirst(CrashReportFilter filter) {
        mFilters.add(0, filter);
        return this;
    }

    public CrashReportFilterChain addLast(CrashReportFilter filter) {
        mFilters.add(filter);
        return this;
    }

    public CrashReportFilter getFilter() {
        CrashReportFilter first = null;
        CrashReportFilter last = null; //上次的
        for (int i = 0; i < mFilters.size(); i++) {
            CrashReportFilter filter = mFilters.get(i);
            if (i == 0) {
                first = filter;
            } else {
                last.setNextFilter(filter);
            }
            last = filter;
        }
        return first;
    }
}
