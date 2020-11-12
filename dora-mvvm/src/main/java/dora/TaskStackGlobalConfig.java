package dora;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.List;

public class TaskStackGlobalConfig implements GlobalConfig {

    @Override
    public void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new TaskStackActivityLifecycle());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    }
}
