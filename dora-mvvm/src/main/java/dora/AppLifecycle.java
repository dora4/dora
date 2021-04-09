package dora;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

import dora.net.NetworkStateReceiver;
import dora.util.KVUtils;
import dora.util.ReflectUtils;

public class AppLifecycle implements ApplicationLifecycleCallbacks {

    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {
        Method getInstance = ReflectUtils.newMethod(KVUtils.class,
                true, "getInstance", Context.class);
        if (getInstance != null) {
            ReflectUtils.invokeMethod(null, getInstance, application);
        }
        NetworkStateReceiver.registerNetworkStateReceiver(application);
    }

    @Override
    public void onTerminate(Application application) {
        NetworkStateReceiver.unregisterNetworkStateReceiver(application);
    }
}
