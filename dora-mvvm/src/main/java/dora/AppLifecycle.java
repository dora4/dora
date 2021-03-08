package dora;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

import dora.net.NetworkStateReceiver;
import dora.util.KeyValueUtils;
import dora.util.MultiLanguageUtils;
import dora.util.ReflectionUtils;

public class AppLifecycle implements ApplicationLifecycleCallbacks {

    @Override
    public void attachBaseContext(Context base) {
        MultiLanguageUtils.init(base);
        MultiLanguageUtils.attachBaseContext(base);
    }

    @Override
    public void onCreate(Application application) {
        Method getInstance = ReflectionUtils.newMethod(KeyValueUtils.class,
                true, "getInstance", Context.class);
        if (getInstance != null) {
            ReflectionUtils.invokeMethod(null, getInstance, application);
        }
        NetworkStateReceiver.registerNetworkStateReceiver(application);
    }

    @Override
    public void onTerminate(Application application) {
        NetworkStateReceiver.unregisterNetworkStateReceiver(application);
    }
}
