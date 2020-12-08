package dora;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;

import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;
import dora.log.Logger;
import dora.net.NetworkChangeObserver;
import dora.net.NetworkStateReceiver;
import dora.permission.Action;
import dora.permission.PermissionManager;
import dora.skin.SkinActivity;
import dora.util.FragmentUtils;
import dora.util.NetworkUtils;
import dora.util.StatusBarUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseSkinActivity<T extends ViewDataBinding> extends SkinActivity
        implements ActivityCache {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    private Map<String, BaseFragment<?>> mFragmentCache = new HashMap<>();
    protected NetworkChangeObserver mNetworkChangeObserver = null;

    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.setLifecycleOwner(this);
        onSetupComponent();
        onShowStatusBar();
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
                                Logger.error("未授予权限" + permission);
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

    public FragmentTransaction getHideTransaction() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment<?> fragment : mFragmentCache.values()) {
            transaction.hide(fragment);
        }
        mFragmentCache.clear();
        return transaction;
    }

    public void showPage(String name) {
        if (mFragmentCache.containsKey(name)) {
            getSupportFragmentManager().beginTransaction().show(mFragmentCache.get(name)).commit();
        } else {
            BaseFragment<?> fragment = getFragment(name);
            getHideTransaction().commit();
            FragmentUtils.add(getSupportFragmentManager(), fragment, getCacheFragmentId());
            mFragmentCache.put(name, fragment);
        }
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
     *
     * @param type
     */
    protected void onNetworkDisconnected() {
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
