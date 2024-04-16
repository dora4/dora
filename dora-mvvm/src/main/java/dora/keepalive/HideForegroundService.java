package dora.keepalive;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

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
        if (KeepLive.foregroundNotification != null) {
            Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(this, KeepLive.foregroundNotification.getTitle(), KeepLive.foregroundNotification.getDescription(), KeepLive.foregroundNotification.getIconRes(), intent);
            startForeground(12390, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
