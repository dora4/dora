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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dora.memory.ActivityCache;
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
        implements ActivityCache, PageSwitcher {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();
    protected Cache<String, Object> mCache;
    private Map<String, BaseFragment<?>> mFragmentCache = new HashMap<>();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtils.attachBaseContext(newBase));
    }

    /**
     * 修复8.x半透明主题旋转屏幕问题。
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
            // 修复Android8.0和8.1系统的半透明主题旋转屏幕BUG，需要关掉锁定屏幕方向按钮复现，该问题只出现在8.x手
            // 机上，可想而知，官方在9.0中修复了此问题
            fixOrientation();
        }
        super.onCreate(savedInstanceState);
        onSetStatusBar();
        onSetNavigationBar();
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.setLifecycleOwner(this);
        mNetworkChangeObserver = new NetworkChangeObserver() {
            @Override
            public void onNetworkConnect(NetUtils.ApnType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetworkDisconnect() {
                onNetworkDisconnected();
            }
        };
        NetworkStateReceiver.registerObserver(mNetworkChangeObserver);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // 在initData之前读取必要的extras
        onGetExtras(intent.getAction(), bundle, intent);
        initData(savedInstanceState);
    }

    /**
     * 如果需要支持在Activity中流式切换Fragment，重写它。
     *
     * @param key
     * @return
     */
    protected BaseFragment<?> getFlowFragment(String key) {
        return null;
    }

    /**
     * 重写它指定流式切换Fragment的容器ID。
     *
     * @return
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

    @Override
    public boolean isLoop() {
        return true;
    }

    @Override
    public void showPage(String key) {
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
    public void showPage(String key, IntentUtils.Extras extras) {
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
     * 在显示Fragment之前要先调用这个方法。
     *
     *     private void showXxxFragment() {
     *         hideFragments(allFragments);
     *         if (xxxFragment == null) {
     *             xxxFragment = new XxxFragment();
     *             FragmentUtils.add(getSupportFragmentManager(), xxxFragment, R.id.fragmentContainer);
     *         }
     *         FragmentUtils.show(xxxFragment);
     *     }
     *
     * 另外，初始化所有Fragment应该在最前面。
     *
     *     private void initFragments() {
     *         if (xxxFragment == null) {
     *             xxxFragment = XxxFragment();
     *             FragmentUtils.add(getSupportFragmentManager(), xxxFragment, R.id.fragmentContainer);
     *         }
     *         if (yyyFragment == null) {
     *             yyyFragment = YyyFragment();
     *             FragmentUtils.add(getSupportFragmentManager(), yyyFragment, R.id.fragmentContainer);
     *         }
     *         if (zzzFragment == null) {
     *             zzzFragment = ZzzFragment();
     *             FragmentUtils.add(getSupportFragmentManager(), zzzFragment, R.id.fragmentContainer);
     *         }
     *     }
     *
     * @param fragments
     */
    protected void hideFragments(Collection<BaseFragment<?>> fragments) {
        if (fragments == null) {
            return;
        }
        for (BaseFragment<?> fragment : fragments) {
            FragmentUtils.hide(fragment);
        }
    }

    /**
     * 获取Activity中所有支持流式切换的Fragment的自定义key的名称。
     *
     * @return
     */
    protected String[] getFlowFragmentPageKeys() {
        return new String[0];
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

    public void openActivity(Class<? extends Activity> activityClazz) {
        IntentUtils.startActivity(activityClazz);
    }

    public void openActivityForResult(Class<? extends Activity> activityClazz, int requestCode) {
        IntentUtils.startActivityForResult(activityClazz, requestCode);
    }

    public void openActivity(Class<? extends Activity> activityClazz, IntentUtils.Extras extras) {
        IntentUtils.startActivity(activityClazz, extras);
    }

    public void openActivityForResult(Class<? extends Activity> activityClazz, IntentUtils.Extras extras, int requestCode) {
        IntentUtils.startActivityForResult(activityClazz, extras, requestCode);
    }

    public void openActivityWithString(Class<? extends Activity> activityClazz, String name, String extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        IntentUtils.startActivity(activityClazz, extras);
    }

    public void openActivityWithInteger(Class<? extends Activity> activityClazz, String name, int extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        IntentUtils.startActivity(activityClazz, extras);
    }

    public void openActivityWithBoolean(Class<? extends Activity> activityClazz, String name, boolean extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        IntentUtils.startActivity(activityClazz, extras);
    }

    public void openActivityWithSerializable(Class<? extends Activity> activityClazz, String name, Serializable extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        IntentUtils.startActivity(activityClazz, extras);
    }

    /**
     * 设置系统状态栏。
     */
    protected void onSetStatusBar() {
    }

    /**
     * 设置系统导航栏。
     */
    protected void onSetNavigationBar() {
    }

    @Override
    protected void onDestroy() {
        NetworkStateReceiver.unregisterObserver(mNetworkChangeObserver);
        super.onDestroy();
    }

    /**
     * 网络已连接，需要使用到{@link dora.BaseApplication}，才会有回调。
     *
     * @param type
     */
    protected void onNetworkConnected(NetUtils.ApnType type) {
    }

    /**
     * 网络连接已断开，需要使用到{@link dora.BaseApplication}，才会有回调。
     */
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
