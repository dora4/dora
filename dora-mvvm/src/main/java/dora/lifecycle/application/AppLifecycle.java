package dora.lifecycle.application;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

public class AppLifecycle implements ApplicationLifecycleCallbacks {

    @Override
    public void attachBaseContext(@NonNull Context base) {
    }

    @Override
    public void onCreate(@NonNull Application application) {
    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }
}
