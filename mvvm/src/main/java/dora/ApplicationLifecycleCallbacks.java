package dora;

import android.app.Application;
import android.content.Context;

public interface ApplicationLifecycleCallbacks {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
