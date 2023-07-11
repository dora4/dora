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

import android.os.Build;

/**
 * The group of Android mobile phone system version, for example, Android 6.0.
 * 简体中文：安卓手机版本分组，例如Android6.0。
 */
public class AndroidVersionGroup implements Group {

    private String version;

    public AndroidVersionGroup(String version) {
        this.version = version;
    }

    @Override
    public boolean counts() {
        return Build.VERSION.RELEASE.equals(version);
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }
}
