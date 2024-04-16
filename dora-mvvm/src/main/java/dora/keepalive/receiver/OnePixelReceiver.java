package dora.keepalive.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import dora.keepalive.activity.OnePixelActivity;

public final class OnePixelReceiver extends BroadcastReceiver {

    Handler mHandler;
    boolean mScreenOn = true;

    public OnePixelReceiver() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭的时候接受到广播
            mScreenOn = false;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mScreenOn) {
                        Intent keepAliveIntent = new Intent(context, OnePixelActivity.class);
                        keepAliveIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        keepAliveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, keepAliveIntent, 0);
                        try {
                            pendingIntent.send();
                            /*} catch (PendingIntent.CanceledException e) {*/
                        } catch (Exception ignore) {
                        }
                    }
                }
            }, 1000);
            context.sendBroadcast(new Intent("_ACTION_SCREEN_OFF"));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            mScreenOn = true;
            context.sendBroadcast(new Intent("_ACTION_SCREEN_ON"));
        }
    }
}
