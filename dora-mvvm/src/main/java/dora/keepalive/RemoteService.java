package dora.keepalive;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;

import dora.keepalive.receiver.NotificationClickReceiver;
import dora.keepalive.service.GuardAidl;
import dora.util.NotificationUtils;
import dora.util.ProcessUtils;

public final class RemoteService extends Service {

    private RemoteBinder mBinder;
    private boolean mBoundLocalService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mBinder == null) {
            mBinder = new RemoteBinder();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mBoundLocalService = this.bindService(new Intent(RemoteService.this, LocalService.class),
                    mConnection, Context.BIND_ABOVE_CLIENT);
        } catch (Exception e) {
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            try {
                if (mBoundLocalService) {
                    unbindService(mConnection);
                }
            } catch (Exception e) {
            }
        }
    }

    private final class RemoteBinder extends GuardAidl.Stub {

        @Override
        public void wakeUp(String title, String description, int iconRes) throws RemoteException {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
                intent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
                Notification notification = NotificationUtils.createNotification(RemoteService.this, title, description, iconRes, intent);
                RemoteService.this.startForeground(13691, notification);
            }
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (ProcessUtils.isRunningTaskExists(getApplicationContext(), getPackageName() + ":remote")) {
                Intent localService = new Intent(RemoteService.this,
                        LocalService.class);
                RemoteService.this.startService(localService);
                mBoundLocalService = RemoteService.this.bindService(new Intent(RemoteService.this,
                        LocalService.class), mConnection, Context.BIND_ABOVE_CLIENT);
            }
            PowerManager pm = (PowerManager) RemoteService.this.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (isScreenOn) {
                sendBroadcast(new Intent("_ACTION_SCREEN_ON"));
            } else {
                sendBroadcast(new Intent("_ACTION_SCREEN_OFF"));
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }
    };
}
