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
