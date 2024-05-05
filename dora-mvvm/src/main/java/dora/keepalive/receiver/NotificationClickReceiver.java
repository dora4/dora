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

package dora.keepalive.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dora.keepalive.KeepAlive;

public final class NotificationClickReceiver extends BroadcastReceiver {

    public final static String CLICK_NOTIFICATION = "CLICK_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NotificationClickReceiver.CLICK_NOTIFICATION)) {
            if (KeepAlive.foregroundNotification != null) {
                if (KeepAlive.foregroundNotification.getForegroundNotificationClickListener() != null) {
                    KeepAlive.foregroundNotification.getForegroundNotificationClickListener().onForegroundNotificationClick(context, intent);
                }
            }
        }
    }
}
