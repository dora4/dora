package dora.lifecycle.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentCallbacks2;
import android.content.ContentProvider;
import android.content.Context;
import android.content.res.Configuration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import dora.lifecycle.activity.ActivityLifecycle;
import dora.lifecycle.config.GlobalConfig;
import dora.lifecycle.fragment.FragmentLifecycle;
import dora.util.ProcessUtils;

public class AppDelegate implements ApplicationLifecycleCallbacks {

    private Application mApplication;
    private List<GlobalConfig> mConfigs;
    private List<ApplicationLifecycleCallbacks> mApplicationLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Context context) {
        this.mConfigs = ManifestParser.parse(context);
        this.mConfigs.add(0, new DefaultGlobalConfig());
        for (GlobalConfig config : mConfigs) {
            config.injectApplicationLifecycle(context, mApplicationLifecycles);
            config.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        for (ApplicationLifecycleCallbacks lifecycle : mApplicationLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        this.mApplication = application;
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }
        mComponentCallback = new AppComponentCallbacks(mApplication);
        // 注册回调，内存紧张时释放部分内存
        mApplication.registerComponentCallbacks(mComponentCallback);
        // 执行框架外部，开发者扩展的 App onCreate 逻辑
        for (ApplicationLifecycleCallbacks lifecycle : mApplicationLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mApplicationLifecycles != null && mApplicationLifecycles.size() > 0) {
            for (ApplicationLifecycleCallbacks lifecycle : mApplicationLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mApplicationLifecycles = null;
        this.mApplication = null;
    }

    /**
     * {@link ComponentCallbacks2} 是一个细粒度的内存回收管理回调
     * {@link Application}、{@link Activity}、{@link Service}、{@link ContentProvider}、
     * {@link Fragment} 实现了 {@link ComponentCallbacks2} 接口
     * 开发者应该实现 {@link ComponentCallbacks2#onTrimMemory(int)} 方法, 细粒度 release 内存,
     * 参数的值不同可以体现出不同程度的内存可用情况响应 {@link ComponentCallbacks2#onTrimMemory(int)} 回调,
     * 开发者的 App 会存活的更持久, 有利于用户体验不响应 {@link ComponentCallbacks2#onTrimMemory(int)} 回
     * 调, 系统 kill 掉进程的几率更大
     */
    private static class AppComponentCallbacks implements ComponentCallbacks2 {

        private Application mApp;

        AppComponentCallbacks(Application app) {
            this.mApp = app;
        }

        /**
         * 在你的 App 生命周期的任何阶段, {@link ComponentCallbacks2#onTrimMemory(int)} 发生的回调都预示着
         * 你设备的内存资源已经开始紧张你应该根据 {@link ComponentCallbacks2#onTrimMemory(int)} 发生回调时的
         * 内存级别来进一步决定释放哪些资源{@link ComponentCallbacks2#onTrimMemory(int)} 的回调可以发生在
         * {@link Application}、{@link Activity}、{@link Service}、{@link ContentProvider}、
         * {@link Fragment}
         *
         * @param level 内存级别
         * @see <a href="https://developer.android.com/reference/android/content/ComponentCallbacks2.html#TRIM_MEMORY_RUNNING_MODERATE">level 官方文档</a>
         */
        @Override
        public void onTrimMemory(int level) {
            // 状态1. 当开发者的 App 正在运行
            // 设备开始运行缓慢, 不会被 kill, 也不会被列为可杀死的, 但是设备此时正运行于低内存状态下, 系统开始触发
            // 杀死 LRU 列表中的进程的机制
//                case TRIM_MEMORY_RUNNING_MODERATE:

            // 设备运行更缓慢了, 不会被 kill, 但请你回收 unused 资源, 以便提升系统的性能, 你应该释放不用的资源用
            // 来提升系统性能 (但是这也会直接影响到你的 App 的性能)
//                case TRIM_MEMORY_RUNNING_LOW:

            // 设备运行特别慢, 当前 App 还不会被杀死, 但是系统已经把 LRU 列表中的大多数进程都已经杀死, 因此你应该
            // 立即释放所有非必须的资源
            // 如果系统不能回收到足够的 RAM 数量, 系统将会清除所有的 LRU 列表中的进程, 并且开始杀死那些之前被认为
            // 不应该杀死的进程, 例如那个包含了一个运行态 Service 的进程
//                case TRIM_MEMORY_RUNNING_CRITICAL:

            // 状态2. 当前 App UI 不再可见, 这是一个回收大个资源的好时机
//                case TRIM_MEMORY_UI_HIDDEN:

            // 状态3. 当前的 App 进程被置于 Background LRU 列表中
            // 进程位于 LRU 列表的上端, 尽管你的 App 进程并不是处于被杀掉的高危险状态, 但系统可能已经开始杀掉
            // LRU 列表中的其他进程了
            // 你应该释放那些容易恢复的资源, 以便于你的进程可以保留下来, 这样当用户回退到你的 App 的时候才能够迅速
            // 恢复
//                case TRIM_MEMORY_BACKGROUND:

            // 系统正运行于低内存状态并且你的进程已经已经接近 LRU 列表的中部位置, 如果系统的内存开始变得更加紧张,
            // 你的进程是有可能被杀死的
//                case TRIM_MEMORY_MODERATE:

            // 系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的
            // App 恢复状态的资源
            // 低于 API 14 的 App 可以使用 onLowMemory 回调
//                case TRIM_MEMORY_COMPLETE:
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        }

        /**
         * 当系统开始清除 LRU 列表中的进程时, 尽管它会首先按照 LRU 的顺序来清除, 但是它同样会考虑进程的内存使用量,
         * 因此消耗越少的进程则越容易被留下来{@link ComponentCallbacks2#onTrimMemory(int)} 的回调是在
         * API 14 才被加进来的, 对于老的版本, 你可以使用 {@link ComponentCallbacks2#onLowMemory} 方法来进
         * 行兼容{@link ComponentCallbacks2#onLowMemory} 相当于 {@code onTrimMemory(TRIM_MEMORY_COMPLETE)}
         *
         * @see #TRIM_MEMORY_COMPLETE
         */
        @Override
        public void onLowMemory() {
            // 系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的
            // App 恢复状态的资源
            ProcessUtils.killAllProcesses(mApp);
        }
    }

    /**
     * 默认全局配置实现，让Activity自动监听了网络状况。继承并使用[dora.BaseApplication]自动配置。
     */
    private static class DefaultGlobalConfig implements GlobalConfig {

        @Override
        public void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles) {
            // AppLifecycle 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩
            // 展一些自己需要的逻辑
            // 可以根据不同的逻辑添加多个实现类
            lifecycles.add(new AppLifecycle());
        }

        @Override
        public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
            // ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用,
            // 所以在对应的方法中可以扩展一些自己需要的逻辑
            // 可以根据不同的逻辑添加多个实现类
            lifecycles.add(new ActivityLifecycle());
        }

        @Override
        public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
            // FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用,
            // 所以在对应的方法中可以扩展一些自己需要的逻辑
            // 可以根据不同的逻辑添加多个实现类
            lifecycles.add(new FragmentLifecycle());
        }
    }
}