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
        // Register callback to release a portion of memory when memory is running low.
        // 简体中文：注册回调，内存紧张时释放部分内存
        mApplication.registerComponentCallbacks(mComponentCallback);
        // Execute the externally defined, developer-extended logic for App onCreate.
        // 简体中文：执行框架外部，开发者扩展的App onCreate逻辑
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
     * {@link ComponentCallbacks2} is a fine-grained memory reclaim management callback.
     * {@link Application}, {@link Activity}, {@link Service}, {@link ContentProvider}, and
     * {@link Fragment} implement the {@link ComponentCallbacks2} interface.Developers should
     * implement the {@link ComponentCallbacks2#onTrimMemory(int)} method to release memory at a
     * fine-grained level.Different values of the parameter can indicate different levels of
     * available memory.Responding to the {@link ComponentCallbacks2#onTrimMemory(int)} callback
     * appropriately can help the developer's app stay alive longer, improving the user experience.
     * Not responding to the {@link ComponentCallbacks2#onTrimMemory(int)} callback increases the
     * likelihood of the system killing the process.
     *
     * 简体中文：{@link ComponentCallbacks2} 是一个细粒度的内存回收管理回调
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
         * At any stage of your app's lifecycle, the callback of {@link ComponentCallbacks2#onTrimMemory(int)}
         * indicates that your device's memory resources have started to become constrained.
         * You should further decide which resources to release based on the memory level indicated
         * by the callback of {@link ComponentCallbacks2#onTrimMemory(int)}.The
         * {@link ComponentCallbacks2#onTrimMemory(int)} callback can occur in {@link Application},
         * {@link Activity}, {@link Service}, {@link ContentProvider}, and {@link Fragment}.
         * 简体中文：在你的 App 生命周期的任何阶段, {@link ComponentCallbacks2#onTrimMemory(int)} 发生的回
         * 调都预示着你设备的内存资源已经开始紧张你应该根据 {@link ComponentCallbacks2#onTrimMemory(int)}
         * 发生回调时的内存级别来进一步决定释放哪些资源{@link ComponentCallbacks2#onTrimMemory(int)} 的回调
         * 可以发生在{@link Application}、{@link Activity}、{@link Service}、{@link ContentProvider}、
         * {@link Fragment}
         *
         * @param level memory level
         * @see <a href="https://developer.android.com/reference/android/content/ComponentCallbacks2
         * .html#TRIM_MEMORY_RUNNING_MODERATE">level official document</a>
         */
        @Override
        public void onTrimMemory(int level) {
            // State 1: The developer's app is running.
            // The device starts running slowly but won't be killed or marked as killable. However,
            // the device is running in a low memory state, and the system triggers the mechanism
            // to kill processes in the LRU list.

            // case TRIM_MEMORY_RUNNING_MODERATE:
            // The device is running even slower. It won't be killed, but you should recycle unused
            // resources to improve system performance. Release any unused resources to enhance
            // system performance (but this will also directly impact your app's performance).

            // case TRIM_MEMORY_RUNNING_LOW:
            // The device is running extremely slow. The current app will not be killed, but most
            // processes in the LRU list have already been killed by the system. Therefore, you
            // should immediately release all non-essential resources. If the system cannot reclaim
            // enough RAM, it will clear all processes in the LRU list and start killing processes
            // that were previously considered safe, such as a process that contains a running service.

            // case TRIM_MEMORY_RUNNING_CRITICAL:
            // State 2: The current app UI is no longer visible, providing an opportunity to reclaim
            // significant resources.

            // case TRIM_MEMORY_UI_HIDDEN:
            // State 3: The current app process is placed in the Background LRU list.
            // The process is at the top of the LRU list. Although your app process is not in a
            // high-risk state of being killed, the system may have started killing other processes
            // in the LRU list. You should release easily recoverable resources so that your process
            // can be preserved, allowing for quick recovery when the user returns to your app.

            // case TRIM_MEMORY_BACKGROUND:
            // The system is running in a low memory state, and your process is nearing the middle
            // position in the LRU list. If the system's memory becomes more constrained, your
            // process may be killed.

            // case TRIM_MEMORY_MODERATE:
            // The system is running in a low memory state, and your process is in the most likely
            // position to be killed in the LRU list. You should release any resources that do not
            // affect the recovery state of your app. Apps targeting APIs below 14 can use the
            // onLowMemory callback.

            // case TRIM_MEMORY_COMPLETE:

            // 简体中文：状态1. 当开发者的 App 正在运行
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
         * When the system starts clearing processes from the LRU list, although it initially clears
         * them in LRU order, it also considers the memory usage of the processes.Therefore, processes
         * with lower resource consumption are more likely to be preserved. The callback
         * {@link ComponentCallbacks2#onTrimMemory(int)} was introduced in API 14.For older versions,
         * you can use the {@link ComponentCallbacks2#onLowMemory} method for compatibility.
         * {@link ComponentCallbacks2#onLowMemory} is equivalent to
         * {@code onTrimMemory(TRIM_MEMORY_COMPLETE)}.
         * 简体中文：当系统开始清除 LRU 列表中的进程时, 尽管它会首先按照 LRU 的顺序来清除, 但是它同样会考虑进程的内
         * 存使用量，因此消耗越少的进程则越容易被留下来{@link ComponentCallbacks2#onTrimMemory(int)} 的回调是在
         * API 14 才被加进来的，对于老的版本，你可以使用 {@link ComponentCallbacks2#onLowMemory} 方法来进
         * 行兼容{@link ComponentCallbacks2#onLowMemory} 相当于 {@code onTrimMemory(TRIM_MEMORY_COMPLETE)}
         *
         * @see #TRIM_MEMORY_COMPLETE
         */
        @Override
        public void onLowMemory() {
            // The system is running in a low memory state, and your process is in the most likely
            // position to be killed in the LRU (Least Recently Used) list.
            // You should release any resources that do not affect the recovery state of your app.
            // 简体中文：系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的
            // App恢复状态的资源
            ProcessUtils.killAllProcesses(mApp);
        }
    }

    /**
     * Default global configuration implementation.
     * 简体中文：默认全局配置实现。
     */
    private static class DefaultGlobalConfig implements GlobalConfig {

        @Override
        public void injectApplicationLifecycle(Context context, List<ApplicationLifecycleCallbacks> lifecycles) {
            // All methods in AppLifecycle will be called during the corresponding lifecycle of
            // the base Application class,
            // so you can extend your own logic in the respective methods.
            // Multiple implementation classes can be added based on different logic requirements.
            // 简体中文：AppLifecycle 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩
            // 展一些自己需要的逻辑
            // 可以根据不同的逻辑添加多个实现类
            lifecycles.add(new AppLifecycle());
        }

        @Override
        public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
            // All methods in ActivityLifecycleCallbacks will be called during the corresponding
            // lifecycle of the Activity (including third-party libraries),
            // so you can extend your own logic in the respective methods.
            // Multiple implementation classes can be added based on different logic requirements.
            // 简体中文：ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中
            // 被调用，所以在对应的方法中可以扩展一些自己需要的逻辑
            // 可以根据不同的逻辑添加多个实现类
            lifecycles.add(new ActivityLifecycle());
        }

        @Override
        public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
            // All methods in FragmentLifecycleCallbacks will be called during the corresponding
            // lifecycle of the Fragment (including third-party libraries),
            // so you can extend your own logic in the respective methods.
            // Multiple implementation classes can be added based on different logic requirements.
            // 简体中文：FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中
            // 被调用，所以在对应的方法中可以扩展一些自己需要的逻辑
            // 可以根据不同的逻辑添加多个实现类
            lifecycles.add(new FragmentLifecycle());
        }
    }
}