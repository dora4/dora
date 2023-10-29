package dora.lifecycle.config;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import dora.lifecycle.application.ApplicationLifecycleCallbacks;

public interface GlobalConfig {

    void injectApplicationLifecycle(@NonNull Context context, @NonNull List<ApplicationLifecycleCallbacks> lifecycles);

    void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles);

    void injectFragmentLifecycle(@NonNull Context context, @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}