package dora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;

import dora.autosize.AutoSizeActivity;
import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;
import dora.log.Logger;
import dora.net.NetworkChangeObserver;
import dora.net.NetworkStateReceiver;
import dora.permission.Action;
import dora.permission.PermissionManager;
import dora.util.FragmentUtils;
import dora.util.IntentUtils;
import dora.util.NetworkUtils;
import dora.util.StatusBarUtils;
import dora.util.ToastUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseAutoSizeActivity<T extends ViewDataBinding> extends AutoSizeActivity
        implements ActivityCache, PageSwitcher {

    protected final String TAG = this.getClass().getSimpleName();
    protected T mBinding;
    private Cache<String, Object> mCache;
    private Map<String, BaseFragment<?>> mFragmentCache = new HashMap<>();
    protected NetworkChangeObserver mNetworkChangeObserver = null;
    private int mFragmentPageIndex;

    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.setLifecycleOwner(this);
        onShowStatusBar();
        onSetupComponent();
        mNetworkChangeObserver = new NetworkChangeObserver() {
            @Override
            public void onNetworkConnect(NetworkUtils.ApnType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetworkDisconnect() {
                onNetworkDisconnected();
            }
        };
        NetworkStateReceiver.registerObserver(mNetworkChangeObserver);
        onNewIntent(getIntent());
        if (requirePermissions().length > 0) {
            PermissionManager.with(this)
                    .runtime()
                    .permission(requirePermissions())
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            initData(savedInstanceState);
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            for (String permission : permissions) {
                                Logger.e("未授予权限" + permission);
                            }
                        }
                    })
                    .start();
        } else {
            initData(savedInstanceState);
        }
    }

    /**
     * 如果需要支持在Activity内部切换Fragment，重写它。
     *
     * @param name
     * @return
     */
    protected BaseFragment<?> getFragment(String name) {
        return null;
    }

    protected int getFragmentContainerId() {
        return 0;
    }

    private int getCacheFragmentId() {
        int cacheFragmentId = getFragmentContainerId();
        if (cacheFragmentId != 0) {
            return getFragmentContainerId();
        } else {
            return android.R.id.content;
        }
    }

    private FragmentTransaction getHideTransaction() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment<?> fragment : mFragmentCache.values()) {
            transaction.hide(fragment);
        }
        mFragmentCache.clear();
        return transaction;
    }

    @Override
    public void showPage(String name) {
        if (mFragmentCache.containsKey(name)) {
            BaseFragment<?> fragment = mFragmentCache.get(name);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
            }
        } else {
            BaseFragment<?> fragment = getFragment(name);
            getHideTransaction().commit();
            FragmentUtils.add(getSupportFragmentManager(), fragment, getCacheFragmentId());
            mFragmentCache.put(name, fragment);
        }
    }

    @Override
    public void showPage(String name, IntentUtils.Extras extras) {
        if (mFragmentCache.containsKey(name)) {
            BaseFragment<?> fragment = mFragmentCache.get(name);
            if (fragment != null) {
                fragment.setArguments(extras.convertToBundle());
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
            }
        } else {
            BaseFragment<?> fragment = getFragment(name);
            fragment.setArguments(extras.convertToBundle());
            getHideTransaction().commit();
            FragmentUtils.add(getSupportFragmentManager(), fragment, getCacheFragmentId());
            mFragmentCache.put(name, fragment);
        }
    }

    protected String[] getFragmentPages() {
        return new String[0];
    }

    @Override
    public void nextPage() {
        if (mFragmentPageIndex == getFragmentPages().length - 1) {
            mFragmentPageIndex = -1;
        }
        if (getFragmentPages().length > 1 && mFragmentPageIndex < getFragmentPages().length - 1) {
            String pageName = getFragmentPages()[++mFragmentPageIndex];
            showPage(pageName);
        }
    }

    @Override
    public void nextPage(IntentUtils.Extras extras) {
        if (mFragmentPageIndex == getFragmentPages().length - 1) {
            mFragmentPageIndex = -1;
        }
        if (getFragmentPages().length > 1 && mFragmentPageIndex < getFragmentPages().length - 1) {
            String pageName = getFragmentPages()[++mFragmentPageIndex];
            showPage(pageName, extras);
        }
    }

    public void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    public void toastL(String msg) {
        ToastUtils.showLong(msg);
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
     * 安装Dagger的Component。
     */
    protected void onSetupComponent() {
    }

    protected void onShowStatusBar() {
        StatusBarUtils.setStatusBarColor(this, Color.BLACK);
    }

    protected String[] requirePermissions() {
        return new String[0];
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
    protected void onNetworkConnected(NetworkUtils.ApnType type) {
    }

    /**
     * 网络连接已断开，需要使用到{@link dora.BaseApplication}，才会有回调。
     */
    protected void onNetworkDisconnected() {
    }

    protected abstract int getLayoutId();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        onGetExtras(bundle, intent);
    }

    protected void onGetExtras(@Nullable Bundle bundle, @NonNull Intent intent) {
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
        }
        return mCache;
    }
}
