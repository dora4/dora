package dora;

import android.app.Application;
import android.content.Context;

import dora.net.NetworkStateReceiver;

public class AppLifecycle implements ApplicationLifecycleCallbacks {

    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {
        NetworkStateReceiver.registerNetworkStateReceiver(application);
    }

    @Override
    public void onTerminate(Application application) {
        NetworkStateReceiver.unregisterNetworkStateReceiver(application);
    }
}
