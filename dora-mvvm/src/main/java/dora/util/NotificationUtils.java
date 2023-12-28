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

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.Manifest.permission.EXPAND_STATUS_BAR;

/**
 * System notification related tools.
 * 简体中文：系统通知相关工具。
 */
public class NotificationUtils {

    public static final int IMPORTANCE_UNSPECIFIED = -1000;
    public static final int IMPORTANCE_NONE        = 0;
    public static final int IMPORTANCE_MIN         = 1;
    public static final int IMPORTANCE_LOW         = 2;
    public static final int IMPORTANCE_DEFAULT     = 3;
    public static final int IMPORTANCE_HIGH        = 4;

    @IntDef({IMPORTANCE_UNSPECIFIED, IMPORTANCE_NONE, IMPORTANCE_MIN, IMPORTANCE_LOW, IMPORTANCE_DEFAULT, IMPORTANCE_HIGH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {
    }

    /**
     * Return whether the notifications enabled.
     * 简体中文：返回通知是否已启用。
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean areNotificationsEnabled() {
        return NotificationManagerCompat.from(GlobalContext.get()).areNotificationsEnabled();
    }

    /**
     * Post a notification to be shown in the status bar.
     * 简体中文：发布一个通知，以显示在状态栏中。
     *
     * @param id       An identifier for this notification.
     * @param consumer The consumer of create the builder of notification.
     */
    public static void notify(int id, ThreadUtils.Consumer<NotificationCompat.Builder> consumer) {
        notify(null, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, consumer);
    }

    /**
     * Post a notification to be shown in the status bar.
     * 简体中文：发布通知，显示在状态栏中。
     *
     * @param tag      A string identifier for this notification.  May be {@code null}.
     * @param id       An identifier for this notification.
     * @param consumer The consumer of create the builder of notification.
     */
    public static void notify(String tag, int id, ThreadUtils.Consumer<NotificationCompat.Builder> consumer) {
        notify(tag, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, consumer);
    }

    /**
     * Post a notification to be shown in the status bar.
     * 简体中文：发布一条通知，以显示在状态栏中。
     *
     * @param id            An identifier for this notification.
     * @param channelConfig The notification channel of config.
     * @param consumer      The consumer of create the builder of notification.
     */
    public static void notify(int id, ChannelConfig channelConfig, ThreadUtils.Consumer<NotificationCompat.Builder> consumer) {
        notify(null, id, channelConfig, consumer);
    }

    /**
     * Post a notification to be shown in the status bar.
     * 简体中文：发布通知，显示在状态栏中。
     *
     * @param tag           A string identifier for this notification.  May be {@code null}.
     * @param id            An identifier for this notification.
     * @param channelConfig The notification channel of config.
     * @param consumer      The consumer of create the builder of notification.
     */
    public static void notify(String tag, int id, ChannelConfig channelConfig, ThreadUtils.Consumer<NotificationCompat.Builder> consumer) {
        NotificationManagerCompat.from(GlobalContext.get()).notify(tag, id, getNotification(channelConfig, consumer));
    }


    public static Notification getNotification(ChannelConfig channelConfig, ThreadUtils.Consumer<NotificationCompat.Builder> consumer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) GlobalContext.get().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channelConfig.getNotificationChannel());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(GlobalContext.get());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelConfig.mNotificationChannel.getId());
        }
        if (consumer != null) {
            consumer.accept(builder);
        }

        return builder.build();
    }

    /**
     * Cancel the notification.
     * 简体中文：取消该通知。
     *
     * @param tag The tag for the notification will be cancelled.
     * @param id  The identifier for the notification will be cancelled.
     */
    public static void cancel(String tag, final int id) {
        NotificationManagerCompat.from(GlobalContext.get()).cancel(tag, id);
    }

    /**
     * Cancel The notification.
     * 简体中文：取消该通知。
     *
     * @param id The identifier for the notification will be cancelled.
     */
    public static void cancel(final int id) {
        NotificationManagerCompat.from(GlobalContext.get()).cancel(id);
    }

    /**
     * Cancel all of the notifications.
     * 简体中文：取消所有通知。
     */
    public static void cancelAll() {
        NotificationManagerCompat.from(GlobalContext.get()).cancelAll();
    }

    /**
     * Set the notification bar's visibility.
     * <p>Must hold {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />}</p>
     * 简体中文：设置通知栏的可见性。<p>必须具有权限
     * {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />}。</p>
     *
     * @param isVisible True to set notification bar visible, false otherwise.
     */
    @RequiresPermission(EXPAND_STATUS_BAR)
    public static void setNotificationBarVisibility(final boolean isVisible) {
        String methodName;
        if (isVisible) {
            methodName = (Build.VERSION.SDK_INT <= 16) ? "expand" : "expandNotificationsPanel";
        } else {
            methodName = (Build.VERSION.SDK_INT <= 16) ? "collapse" : "collapsePanels";
        }
        invokePanels(methodName);
    }

    private static void invokePanels(final String methodName) {
        try {
            @SuppressLint("WrongConstant")
            Object service = GlobalContext.get().getSystemService("statusbar");
            @SuppressLint("PrivateApi")
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName);
            expand.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ChannelConfig {

        public static final ChannelConfig DEFAULT_CHANNEL_CONFIG = new ChannelConfig(
                GlobalContext.get().getPackageName(), GlobalContext.get().getPackageName(), IMPORTANCE_DEFAULT
        );

        private NotificationChannel mNotificationChannel;

        public ChannelConfig(String id, CharSequence name, @Importance int importance) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel = new NotificationChannel(id, name, importance);
            }
        }

        public NotificationChannel getNotificationChannel() {
            return mNotificationChannel;
        }

        /**
         * Sets whether or not notifications posted to this channel can interrupt the user in
         * {@link NotificationManager.Policy#INTERRUPTION_FILTER_PRIORITY} mode.
         * <p>
         * Only modifiable by the system and notification ranker.
         * 简体中文：设置是否允许发布到此通道的通知在 {@link NotificationManager.Policy
         * #INTERRUPTION_FILTER_PRIORITY} 模式下打断用户。<p>
         * 仅系统和通知排序器可修改。
         */
        public ChannelConfig setBypassDnd(boolean bypassDnd) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setBypassDnd(bypassDnd);
            }
            return this;
        }

        /**
         * Sets the user visible description of this channel.
         * 简体中文：设置此通道的用户可见描述。
         *
         * <p>The recommended maximum length is 300 characters; the value may be truncated if it is too
         * long.
         */
        public ChannelConfig setDescription(String description) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setDescription(description);
            }
            return this;
        }

        /**
         * Sets what group this channel belongs to.
         * <p>
         * Group information is only used for presentation, not for behavior.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}, unless the
         * channel is not currently part of a group.
         * 简体中文：设置此通道所属的组。
         * <p>
         * 组信息仅用于呈现，不影响行为。
         * <p>
         * 除非通道当前不属于任何组，否则仅可在通道提交给
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)} 之前进行修改。
         *
         * @param groupId the id of a group created by
         *                {@link NotificationManager#createNotificationChannelGroup)}.
         */
        public ChannelConfig setGroup(String groupId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setGroup(groupId);
            }
            return this;
        }

        /**
         * Sets the level of interruption of this notification channel.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         * 简体中文：设置此通知通道的打扰级别。
         * <p>
         * 仅可在通道提交给
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)} 之前进行修改。
         *
         * @param importance the amount the user should be interrupted by
         *                   notifications from this channel.
         */
        public ChannelConfig setImportance(@Importance int importance) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setImportance(importance);
            }
            return this;
        }

        /**
         * Sets the notification light color for notifications posted to this channel, if lights are
         * {@link NotificationChannel#enableLights(boolean) enabled} on this channel and the device
         * supports that feature.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         * 简体中文：如果在此通道上启用了灯光且设备支持该功能，则为发布到此通道的通知设置通知灯光颜色。
         * <p>
         * 仅可在通道提交给
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)} 之前进行修改。
         */
        public ChannelConfig setLightColor(int argb) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setLightColor(argb);
            }
            return this;
        }

        /**
         * Sets whether notifications posted to this channel appear on the lockscreen or not, and if so,
         * whether they appear in a redacted form. See e.g. {@link Notification#VISIBILITY_SECRET}.
         * <p>
         * Only modifiable by the system and notification ranker.
         * 简体中文：设置发布到此通道的通知是否会显示在锁屏上，以及如果显示在锁屏上，是否以删除的形式显示。例如，
         * 参见 {@link Notification#VISIBILITY_SECRET}。<p>
         * 仅系统和通知排序器可修改。
         */
        public ChannelConfig setLockscreenVisibility(int lockscreenVisibility) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setLockscreenVisibility(lockscreenVisibility);
            }
            return this;
        }

        /**
         * Sets the user visible name of this channel.
         *
         * <p>The recommended maximum length is 40 characters; the value may be truncated if it is too
         * long.
         * 简体中文：设置此通道的用户可见名称。
         *  <p>建议的最大长度为40个字符；如果太长，值可能会被截断。
         */
        public ChannelConfig setName(CharSequence name) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setName(name);
            }
            return this;
        }

        /**
         * Sets whether notifications posted to this channel can appear as application icon badges
         * in a Launcher.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         * 简体中文：设置是否允许发布到此通道的通知在启动器中显示为应用程序图标徽章。
         * <p>
         * 仅可在通道提交给
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)} 之前进行修改。
         *
         * @param showBadge true if badges should be allowed to be shown.
         */
        public ChannelConfig setShowBadge(boolean showBadge) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setShowBadge(showBadge);
            }
            return this;
        }

        /**
         * Sets the sound that should be played for notifications posted to this channel and its
         * audio attributes. Notification channels with an {@link NotificationChannel#getImportance() importance} of at
         * least {@link NotificationManager#IMPORTANCE_DEFAULT} should have a sound.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         * 简体中文：设置应该为发布到此通道的通知播放的声音及其音频属性。具有至少
         * {@link NotificationManager#IMPORTANCE_DEFAULT} 重要性的通知通道应该具有声音。
         * <p>
         * 仅可在通道提交给
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)} 之前进行修改。
         */
        public ChannelConfig setSound(Uri sound, AudioAttributes audioAttributes) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setSound(sound, audioAttributes);
            }
            return this;
        }

        /**
         * Sets the vibration pattern for notifications posted to this channel. If the provided
         * pattern is valid (non-null, non-empty), will {@link NotificationChannel#enableVibration(boolean)} enable
         * vibration} as well. Otherwise, vibration will be disabled.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         * 简体中文：设置发布到此通道的通知的振动模式。如果提供的模式有效（非空，非空数组），将启用振动
         * {@link NotificationChannel#enableVibration(boolean)}。否则，振动将被禁用。
         * <p>
         * 仅可在通道提交给
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)} 之前进行修改。
         */
        public ChannelConfig setVibrationPattern(long[] vibrationPattern) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setVibrationPattern(vibrationPattern);
            }
            return this;
        }
    }
}
