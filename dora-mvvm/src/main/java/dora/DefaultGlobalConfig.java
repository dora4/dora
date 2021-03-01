package dora;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.List;

class DefaultGlobalConfig implements GlobalConfig {

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
