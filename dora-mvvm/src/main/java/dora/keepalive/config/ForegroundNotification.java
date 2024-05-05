/*
 * Copyright (C) 2024 The Dora Open Source Project
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

package dora.keepalive.config;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ForegroundNotification implements Serializable {

    private String mTitle;
    private String mDescription;
    private int mIconRes;
    private ForegroundNotificationClickListener foregroundNotificationClickListener;

    private ForegroundNotification() {
    }

    public ForegroundNotification(String title, String description, int iconRes, ForegroundNotificationClickListener foregroundNotificationClickListener) {
        this.mTitle = title;
        this.mDescription = description;
        this.mIconRes = iconRes;
        this.foregroundNotificationClickListener = foregroundNotificationClickListener;
    }

    public ForegroundNotification(String title, String description, int iconRes) {
        this.mTitle = title;
        this.mDescription = description;
        this.mIconRes = iconRes;
    }

    public static ForegroundNotification init() {
        return new ForegroundNotification();
    }

    public ForegroundNotification title(@NonNull String title) {
        this.mTitle = title;
        return this;
    }

    public ForegroundNotification description(@NonNull String description) {
        this.mDescription = description;
        return this;
    }

    public ForegroundNotification icon(@NonNull int iconRes) {
        this.mIconRes = iconRes;
        return this;
    }

    public ForegroundNotification foregroundNotificationClickListener(@NonNull ForegroundNotificationClickListener foregroundNotificationClickListener) {
        this.foregroundNotificationClickListener = foregroundNotificationClickListener;
        return this;
    }

    public String getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    public String getDescription() {
        return mDescription == null ? "" : mDescription;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public ForegroundNotificationClickListener getForegroundNotificationClickListener() {
        return foregroundNotificationClickListener;
    }
}
