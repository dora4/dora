package dora.lifecycle.application;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

import dora.util.KVUtils;
import dora.util.ReflectionUtils;

public class AppLifecycle implements ApplicationLifecycleCallbacks {

    @Override
    public void attachBaseContext(@NonNull Context base) {
    }

    @Override
    public void onCreate(@NonNull Application application) {
        Method getInstance = ReflectionUtils.findMethod(KVUtils.class,
                true, "getInstance", Context.class);
        if (getInstance != null) {
            ReflectionUtils.invokeMethod(null, getInstance, application);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }
}
