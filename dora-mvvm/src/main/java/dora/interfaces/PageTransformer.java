/*
 * Copyright (C) 2023 The Dora Open Source Project
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

package dora.interfaces;

import androidx.annotation.NonNull;

import dora.util.IntentUtils;

/**
 * It is used to switch multiple fragments in an activity. Either activity or fragment，you should
 * implement this interface.
 * 简体中文：用于在Activity中切换多个Fragment。无论是activity或fragment，你都应该实现此接口。
 */
public interface PageTransformer {

    /**
     * @see #showPage(String, IntentUtils.Extras)
     */
    void showPage(@NonNull String key);

    /**
     * Display a fragment in the activity according to key.
     * 简体中文：根据key显示activity中的某个fragment。
     */
    void showPage(@NonNull String key, @NonNull IntentUtils.Extras extras);

    /**
     * @see #lastPage(IntentUtils.Extras)
     */
    void lastPage();

    /**
     * Display last fragment in the activity.
     * 简体中文：显示activity中的上一个fragment。
     */
    void lastPage(@NonNull IntentUtils.Extras extras);

    /**
     * @see #nextPage(IntentUtils.Extras)
     */
    void nextPage();

    /**
     * Display next fragment in the activity.
     * 简体中文：显示activity中的下一个fragment。
     */
    void nextPage(@NonNull IntentUtils.Extras extras);

    /**
     * Whether to display the last page after reaching the first page, whether to display the
     * first page after reaching the last page.
     * 简体中文：到达第一页再往上一页是否显示最后一页，到达最后一页再往下一页是否显示第一页。
     */
    boolean isPageLoop();
}
