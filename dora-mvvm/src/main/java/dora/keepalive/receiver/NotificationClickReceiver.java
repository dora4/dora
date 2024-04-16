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
