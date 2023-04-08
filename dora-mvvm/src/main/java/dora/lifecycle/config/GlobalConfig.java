package dora.lifecycle.config;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import dora.lifecycle.application.ApplicationLifecycleCallbacks;

public interface GlobalConfig {

    void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles);

    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);

    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}