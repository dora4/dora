package dora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dora.memory.DataLoader;
import dora.memory.Cache;
import dora.memory.CacheType;
import dora.memory.LruCache;
import dora.net.NetworkChangeObserver;
import dora.net.NetworkStateReceiver;
import dora.util.FragmentUtils;
import dora.util.IntentUtils;
import dora.util.KVUtils;
import dora.util.LanguageUtils;
import dora.util.NetUtils;
import dora.util.ReflectionUtils;
import dora.util.ToastUtils;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity
        implements DataLoader<T>, PageTransformer {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();
    protected Cache<String, Object> mCache;
    private final Map<String, BaseFragment<?>> mFragmentCache = new HashMap<>();
    protected NetworkChangeObserver mNetworkChangeObserver = null;
    private int mFragmentPageIndex;

    public Context getContext() {
        return this;
    }

    private boolean isTranslucentOrFloating() {
        Class<?> styleableClazz = ReflectionUtils.findClass("com.android.internal.R.styleable");
        Field windowField = ReflectionUtils.findField(styleableClazz, false, "Window");
        if (windowField == null) {
            return false;
        }
        int[] styleableRes = (int[]) ReflectionUtils.getFieldValue(windowField, null);
        TypedArray a = obtainStyledAttributes(styleableRes);
        Method m = ReflectionUtils.findMethod(ActivityInfo.class, false, "isTranslucentOrFloating", TypedArray.class);
        return  (boolean) ReflectionUtils.invokeMethod(null, m, a);
    }

    protected boolean isDetectNet() {
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtils.attachBaseContext(newBase));
    }

    /**
     * Fix the translucent theme rotation screen issue on Android 8.x.
     * 简体中文：修复8.x半透明主题旋转屏幕问题。
     */
    private void fixOrientation() {
        Field field = ReflectionUtils.findField(Activity.class, true, "mActivityInfo");
        if (field != null) {
            ActivityInfo activityInfo = (ActivityInfo) ReflectionUtils.getFieldValue(field, this);
            if (activityInfo != null) {
                activityInfo.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            }
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT
                == Build.VERSION_CODES.O_MR1) && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT
                == Build.VERSION_CODES.O_MR1) && isTranslucentOrFloating()) {
            // Fix the translucent theme rotation screen bug on Android 8.0 and 8.1 systems.
            // To reproduce the issue, the screen orientation lock button needs to be turned
            // off. This problem only occurs on 8.x devices, and as you can imagine, it has
            // been fixed by the official in 9.0.
            // 简体中文：修复Android8.0和8.1系统的半透明主题旋转屏幕BUG，需要关掉锁定屏幕方向按钮复现，该问题只
            // 出现在8.x手机上，可想而知，官方在9.0中修复了此问题
            fixOrientation();
        }
        super.onCreate(savedInstanceState);
        onSetStatusBar();
        onSetNavigationBar();
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.setLifecycleOwner(this);
        if (isDetectNet()) {
            mNetworkChangeObserver = new NetworkChangeObserver() {
                @Override
                public void onNetworkConnect(@NonNull NetUtils.ApnType type) {
                    BaseActivity.this.onNetworkConnected(type);
                }

                @Override
                public void onNetworkDisconnect() {
                    BaseActivity.this.onNetworkDisconnected();
                }
            };
            NetworkStateReceiver.registerObserver(mNetworkChangeObserver);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // Read necessary extras before the initData function.
        // 简体中文：在initData之前读取必要的extras
        onGetExtras(intent.getAction(), bundle, intent);
        initData(savedInstanceState);
        initData(savedInstanceState, mBinding);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
    }

    /**
     * If you need to support seamless switching of fragments in an Activity, override it.
     * 简体中文：如果需要支持在Activity中流式切换Fragment，重写它。
     *
     * @param key fragment page key
     * @see #getFlowFragmentContainerId()
     * @see #getFlowFragmentPageKeys()
     */
    protected BaseFragment<?> getFlowFragment(@NonNull String key) {
        return null;
    }

    /**
     * Override it to specify the container ID for seamless switching of fragments.
     * 简体中文：重写它指定流式切换Fragment的容器ID。
     *
     * @see #getFlowFragment(String)
     * @see #getFlowFragmentPageKeys()
     */
    protected int getFlowFragmentContainerId() {
        return 0;
    }

    private int getDefaultFlowFragmentContainerId() {
        int defFragmentId = getFlowFragmentContainerId();
        if (defFragmentId != 0) {
            return getFlowFragmentContainerId();
        } else {
            return android.R.id.content;
        }
    }

    /**
     * Get the custom key name for all fragments that support seamless switching in the Activity.
     * 简体中文：获取Activity中所有支持流式切换的Fragment的自定义key的名称。
     *
     * @see #getFlowFragment(String)
     * @see #getFlowFragmentContainerId()
     */
    protected String[] getFlowFragmentPageKeys() {
        return new String[0];
    }

    /**
     * Whether to display the last page after reaching the first page, whether to display the
     * first page after reaching the last page.
     * 简体中文：到达第一页再往上一页是否显示最后一页，到达最后一页再往下一页是否显示第一页。
     *
     * @see #getFlowFragment(String)
     * @see #getFlowFragmentContainerId()
     * @see #getFlowFragmentPageKeys()
     */
    @Override
    public boolean isLoop() {
        return true;
    }

    @Override
    public void showPage(@NonNull String key) {
        if (mFragmentCache.containsKey(key)) {
            BaseFragment<?> fragment = mFragmentCache.get(key);
            if (fragment != null) {
                FragmentUtils.show(fragment);
            }
        } else {
            BaseFragment<?> fragment = getFlowFragment(key);
            hideFragments(mFragmentCache.values());
            mFragmentCache.clear();
            FragmentUtils.add(getSupportFragmentManager(), fragment, getDefaultFlowFragmentContainerId());
            mFragmentCache.put(key, fragment);
        }
    }

    @Override
    public void showPage(@NonNull String key, IntentUtils.Extras extras) {
        if (mFragmentCache.containsKey(key)) {
            BaseFragment<?> fragment = mFragmentCache.get(key);
            if (fragment != null) {
                fragment.setArguments(extras.convertToBundle());
                fragment.onGetExtras(extras.convertToBundle());
                FragmentUtils.show(fragment);
            }
        } else {
            BaseFragment<?> fragment = getFlowFragment(key);
            fragment.setArguments(extras.convertToBundle());
            fragment.onGetExtras(extras.convertToBundle());
            hideFragments(mFragmentCache.values());
            mFragmentCache.clear();
            FragmentUtils.add(getSupportFragmentManager(), fragment, getDefaultFlowFragmentContainerId());
            mFragmentCache.put(key, fragment);
        }
    }

    /**
     * Call this method before FragmentUtils.show().
     * 简体中文：在显示Fragment之前要先调用这个方法。
     *
     * Like this:
     *     private void showSampleFragment() {
     *         hideFragments(allFragments);
     *         if (sampleFragment == null) {
     *             sampleFragment = new SampleFragment();
     *             FragmentUtils.add(getSupportFragmentManager(), sampleFragment, R.id.fragmentContainer);
     *         }
     *         FragmentUtils.show(sampleFragment);
     *     }
     */
    protected void hideFragments(Collection<BaseFragment<?>> fragments) {
        if (fragments == null) {
            return;
        }
        for (BaseFragment<?> fragment : fragments) {
            FragmentUtils.hide(fragment);
        }
    }

    @Override
    public void lastPage() {
        if (isLoop() && mFragmentPageIndex == 0) {
            mFragmentPageIndex = getFlowFragmentPageKeys().length;
        }
        if (getFlowFragmentPageKeys().length > 1 && mFragmentPageIndex > 0) {
            String pageName = getFlowFragmentPageKeys()[--mFragmentPageIndex];
            showPage(pageName);
        }
    }

    @Override
    public void lastPage(IntentUtils.Extras extras) {
        if (isLoop() && mFragmentPageIndex == 0) {
            mFragmentPageIndex = getFlowFragmentPageKeys().length;
        }
        if (getFlowFragmentPageKeys().length > 1 && mFragmentPageIndex > 0) {
            String pageName = getFlowFragmentPageKeys()[--mFragmentPageIndex];
            showPage(pageName, extras);
        }
    }

    @Override
    public void nextPage() {
        if (isLoop() && mFragmentPageIndex == getFlowFragmentPageKeys().length - 1) {
            mFragmentPageIndex = -1;
        }
        if (getFlowFragmentPageKeys().length > 1 && mFragmentPageIndex < getFlowFragmentPageKeys().length - 1) {
            String pageName = getFlowFragmentPageKeys()[++mFragmentPageIndex];
            showPage(pageName);
        }
    }

    @Override
    public void nextPage(IntentUtils.Extras extras) {
        if (isLoop() && mFragmentPageIndex == getFlowFragmentPageKeys().length - 1) {
            mFragmentPageIndex = -1;
        }
        if (getFlowFragmentPageKeys().length > 1 && mFragmentPageIndex < getFlowFragmentPageKeys().length - 1) {
            String pageName = getFlowFragmentPageKeys()[++mFragmentPageIndex];
            showPage(pageName, extras);
        }
    }

    public void showShortToast(String msg) {
        ToastUtils.showShort(this, msg);
    }

    public void showLongToast(String msg) {
        ToastUtils.showLong(this, msg);
    }

    /**
     * Set the system status bar.
     * 简体中文：设置系统状态栏。
     */
    protected void onSetStatusBar() {
    }

    /**
     * Set the system navigation bar.
     * 简体中文：设置系统导航栏。
     */
    protected void onSetNavigationBar() {
    }

    @Override
    protected void onDestroy() {
        NetworkStateReceiver.unregisterObserver(mNetworkChangeObserver);
        super.onDestroy();
    }

    protected void onNetworkConnected(NetUtils.ApnType type) {
    }

    protected void onNetworkDisconnected() {
    }

    protected abstract int getLayoutId();

    protected void onGetExtras(@Nullable String action, @Nullable Bundle bundle, @NonNull Intent intent) {
    }

    @Override
    public Cache.Factory cacheFactory() {
        return new Cache.Factory() {
            @Override
            public Cache build(CacheType type, Context context) {
                return new LruCache(type.calculateCacheSize(context));
            }
        };
    }

    @NonNull
    @Override
    public synchronized Cache<String, Object> loadCache() {
        if (mCache == null) {
            mCache = cacheFactory().build(CacheType.ACTIVITY_CACHE, this);
            Set<String> keys = KVUtils.getInstance(this).cacheKeys();
            for (String key : keys) {
                Object cache = KVUtils.getInstance(this).getCacheFromMemory(key);
                mCache.put(key, cache);
            }
        }
        return mCache;
    }
}
