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
