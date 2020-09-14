package dora;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import dora.autosize.AutoSizeActivity;
import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;
import dora.log.Logger;
import dora.net.NetworkChangeObserver;
import dora.net.NetworkStateReceiver;
import dora.permission.Action;
import dora.permission.PermissionManager;
import dora.util.NetworkUtils;
import dora.util.StatusBarUtils;

import java.util.List;

public abstract class BaseAutoSizeActivity<T extends ViewDataBinding> extends AutoSizeActivity
        implements ActivityCache {

    protected final String TAG = this.getClass().getSimpleName();
    protected T mBinding;
    protected NetworkChangeObserver mNetworkChangeObserver = null;
    private Cache<String, Object> mCache;

    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
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
     *
     * @param type
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
