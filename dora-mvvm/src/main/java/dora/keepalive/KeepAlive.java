package dora.keepalive;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import dora.keepalive.config.ForegroundNotification;
import dora.keepalive.service.JobHandlerService;
import dora.keepalive.service.LocalService;
import dora.keepalive.service.RemoteService;
import dora.util.ProcessUtils;

/**
 *   KeepAlive.startWork(this,
 *                 RunMode.SAVE_POWER,
 *                 foregroundNotification,
 *                 new KeepAliveService() {
 *
 *                     @Override
 *                     public void onWorking() {
 *                         // start your service
 *                     }
 *
 *                     @Override
 *                     public void onStop() {
 *                         // stop your service
 *                     }
 *         });
 */
public final class KeepAlive {

    public enum RunMode {

        SAVE_POWER,

        ROGUE
    }

    public static ForegroundNotification foregroundNotification = null;
    public static KeepAliveService keepAliveService = null;
    public static RunMode runMode = null;
    public static boolean useSilenceSound = true;

    public static void startWork(@NonNull Application application, @NonNull RunMode runMode, @NonNull ForegroundNotification foregroundNotification, @NonNull KeepAliveService keepAliveService) {
        if (ProcessUtils.isMainProcess(application)) {
            KeepAlive.foregroundNotification = foregroundNotification;
            KeepAlive.keepAliveService = keepAliveService;
            KeepAlive.runMode = runMode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(application, JobHandlerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    application.startForegroundService(intent);
                } else {
                    application.startService(intent);
                }
            } else {
                Intent localIntent = new Intent(application, LocalService.class);
                Intent remoteIntent = new Intent(application, RemoteService.class);
                application.startService(localIntent);
                application.startService(remoteIntent);
            }
        }
    }

    public static void useSilenceSound(boolean enable) {
        KeepAlive.useSilenceSound = enable;
    }
}
