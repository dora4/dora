package dora.lifecycle.application;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

public interface ApplicationLifecycleCallbacks {

    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}
