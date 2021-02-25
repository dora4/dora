package dora;

import android.app.Application;
import android.content.Context;

import dora.util.MultiLanguageUtils;

public class MultiLanguageAppLifecycle implements ApplicationLifecycleCallbacks {

    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {
        MultiLanguageUtils.init(application);
    }

    @Override
    public void onTerminate(Application application) {
    }
}
