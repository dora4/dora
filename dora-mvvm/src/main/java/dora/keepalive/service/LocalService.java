package dora.keepalive.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;

import dora.keepalive.KeepAlive;
import dora.keepalive.receiver.NotificationClickReceiver;
import dora.keepalive.receiver.OnePixelReceiver;
import dora.mvvm.R;
import dora.util.NotificationUtils;
import dora.util.ProcessUtils;

public final class LocalService extends Service {

    private OnePixelReceiver mOnePixelReceiver;
    private ScreenStateReceiver mScreenStateReceiver;
    private boolean mPause = true;
    private MediaPlayer mMediaPlayer;
    private LocalBinder mBinder;
    private Handler mHandler;
    private boolean mBoundRemoteService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mBinder == null) {
            mBinder = new LocalBinder();
        }
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        mPause = pm.isScreenOn();
        if (mHandler == null) {
            mHandler = new Handler();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (KeepAlive.useSilenceSound) {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.silence);
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(0f, 0f);
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (!mPause) {
                                if (KeepAlive.runMode == KeepAlive.RunMode.ROGUE) {
                                    play();
                                } else {
                                    if (mHandler != null) {
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                play();
                                            }
                                        }, 5000);
                                    }
                                }
                            }
                        }
                    });
                    mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            return false;
                        }
                    });
                    play();
                }
            }
        }
        if (mOnePixelReceiver == null) {
            mOnePixelReceiver = new OnePixelReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(mOnePixelReceiver, intentFilter);
        if (mScreenStateReceiver == null) {
            mScreenStateReceiver = new ScreenStateReceiver();
        }
        IntentFilter keepAliveIntentFilter = new IntentFilter();
        keepAliveIntentFilter.addAction("_ACTION_SCREEN_OFF");
        keepAliveIntentFilter.addAction("_ACTION_SCREEN_ON");
        registerReceiver(mScreenStateReceiver, keepAliveIntentFilter);
        if (KeepAlive.foregroundNotification != null) {
            Intent notificationIntent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            notificationIntent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(this, KeepAlive.foregroundNotification.getTitle(), KeepAlive.foregroundNotification.getDescription(), KeepAlive.foregroundNotification.getIconRes(), notificationIntent);
            startForeground(13691, notification);
        }
        try {
            Intent remoteIntent = new Intent(this, RemoteService.class);
            mBoundRemoteService = this.bindService(remoteIntent, mConnection, Context.BIND_ABOVE_CLIENT);
        } catch (Exception ignore) {
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                startService(new Intent(this, HideForegroundService.class));
            }
        } catch (Exception ignore) {
        }
        if (KeepAlive.keepAliveService != null) {
            KeepAlive.keepAliveService.onWorking();
        }
        return START_STICKY;
    }

    private void play() {
        if (KeepAlive.useSilenceSound) {
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    private void pause() {
        if (KeepAlive.useSilenceSound) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("_ACTION_SCREEN_OFF")) {
                mPause = false;
                play();
            } else if (intent.getAction().equals("_ACTION_SCREEN_ON")) {
                mPause = true;
                pause();
            }
        }
    }

    private static final class LocalBinder extends GuardAidl.Stub {

        @Override
        public void wakeUp(String title, String description, int iconRes) throws RemoteException {
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (ProcessUtils.isTaskRunning(getApplicationContext(), "dora.keepalive.service.LocalService")) {
                Intent remoteService = new Intent(LocalService.this,
                        RemoteService.class);
                LocalService.this.startService(remoteService);
                Intent intent = new Intent(LocalService.this, RemoteService.class);
                mBoundRemoteService = LocalService.this.bindService(intent, mConnection,
                        Context.BIND_ABOVE_CLIENT);
            }
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (isScreenOn) {
                sendBroadcast(new Intent("_ACTION_SCREEN_ON"));
            } else {
                sendBroadcast(new Intent("_ACTION_SCREEN_OFF"));
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                if (mBinder != null && KeepAlive.foregroundNotification != null) {
                    GuardAidl guardAidl = GuardAidl.Stub.asInterface(service);
                    guardAidl.wakeUp(KeepAlive.foregroundNotification.getTitle(), KeepAlive.foregroundNotification.getDescription(), KeepAlive.foregroundNotification.getIconRes());
                }
            } catch (RemoteException ignore) {
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mBoundRemoteService) {
                unbindService(mConnection);
            }
        } catch (Exception ignore) {
        }
        try {
            unregisterReceiver(mOnePixelReceiver);
            unregisterReceiver(mScreenStateReceiver);
        } catch (Exception ignore) {
        }
        if (KeepAlive.keepAliveService != null) {
            KeepAlive.keepAliveService.onStop();
        }
    }
}
