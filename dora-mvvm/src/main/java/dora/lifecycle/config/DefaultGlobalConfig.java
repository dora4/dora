package dora.lifecycle.config;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import dora.lifecycle.activity.ActivityLifecycle;
import dora.lifecycle.application.AppLifecycle;
import dora.lifecycle.application.ApplicationLifecycleCallbacks;
import dora.lifecycle.fragment.FragmentLifecycle;

/**
 * 默认全局配置实现，让Activity自动监听了网络状况。继承并使用[dora.BaseApplication]自动配置。
 */
public class DefaultGlobalConfig implements GlobalConfig {

    @Override
    public void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles) {
        //AppLifecycle 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecycle());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        //ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycle());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        //FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(new FragmentLifecycle());
    }
}
