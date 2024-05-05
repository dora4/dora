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

package dora.keepalive.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Random;

import dora.keepalive.KeepAlive;
import dora.keepalive.receiver.NotificationClickReceiver;
import dora.util.NotificationUtils;

public class HideForegroundService extends Service {

    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopForeground(true);
                stopSelf();
            }
        }, 2000);
        return START_NOT_STICKY;
    }

    private void startForeground() {
        if (KeepAlive.foregroundNotification != null) {
            Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(this, KeepAlive.foregroundNotification.getTitle(), KeepAlive.foregroundNotification.getDescription(), KeepAlive.foregroundNotification.getIconRes(), intent);
            startForeground(new Random().nextInt(65535), notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
