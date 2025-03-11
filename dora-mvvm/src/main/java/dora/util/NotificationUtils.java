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

package dora.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * System notification related tools.
 * 简体中文：系统通知相关工具。
 */
public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    private String mId;
    private String mName;
    private Context mContext;
    private NotificationChannel mChannel;

    private NotificationUtils(Context context) {
        super(context);
        this.mContext = context;
        mId = context.getPackageName();
        mName = context.getPackageName();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        if (mChannel == null) {
            mChannel = new NotificationChannel(mId, mName, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(false);
            mChannel.enableLights(false);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{0});
            mChannel.setSound(null, null);
            getNotificationManager().createNotificationChannel(mChannel);
        }
    }

    private NotificationManager getNotificationManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content, int icon, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, getPendingIntentFlag());
        return new Notification.Builder(mContext, mId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    public static PendingIntent createPendingIntent(Context context, int requestCode, Intent intent) {
        return PendingIntent.getBroadcast(context, requestCode, intent, getPendingIntentFlag());
    }

    private static int getPendingIntentFlag() {
        // Android 12+ requires `FLAG_MUTABLE` to pass Extras; otherwise, the `Intent` may be null.
        // 简体中文：Android 12+ 需要 `FLAG_MUTABLE` 才能传递 Extras，否则 `Intent` 可能为空。
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
    }

    public NotificationCompat.Builder getNotificationOld(String title, String content, int icon, Intent intent) {
        PendingIntent pendingIntent = createPendingIntent(mContext, 0, intent);
        return new NotificationCompat.Builder(mContext, mId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setVibrate(new long[]{0})
                .setContentIntent(pendingIntent);
    }

    public static void sendNotification(@NonNull Context context, @NonNull String title, @NonNull String content, @NonNull int icon, @NonNull Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationUtils.createNotificationChannel();
            notification = notificationUtils.getChannelNotification(title, content, icon, intent).build();
        } else {
            notification = notificationUtils.getNotificationOld(title, content, icon, intent).build();
        }
        notificationUtils.getNotificationManager().notify(new java.util.Random().nextInt(10000), notification);
    }

    public static Notification createNotification(@NonNull Context context, @NonNull String title, @NonNull String content, @NonNull int icon, @NonNull Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationUtils.createNotificationChannel();
            notification = notificationUtils.getChannelNotification(title, content, icon, intent).build();
        } else {
            notification = notificationUtils.getNotificationOld(title, content, icon, intent).build();
        }
        return notification;
    }
}

