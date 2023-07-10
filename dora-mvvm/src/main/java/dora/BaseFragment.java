package dora;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import java.util.Set;

import dora.memory.DataLoader;
import dora.memory.Cache;
import dora.memory.CacheType;
import dora.memory.LruCache;
import dora.util.IntentUtils;
import dora.util.KVUtils;
import dora.util.ToastUtils;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements
        DataLoader<T>, PageSwitcher {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();
    protected Cache mCache;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
        assert mBinding != null;
        mBinding.setLifecycleOwner(this);
        initData(savedInstanceState);
        initData(savedInstanceState, mBinding);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
    }

    public PageSwitcher getPageSwitcher() {
        if (getActivity() instanceof PageSwitcher) {
            return (PageSwitcher) getActivity();
        }
        return null;
    }

    @Override
    public final boolean isLoop() {
        // Dependents on activity.
        return getPageSwitcher().isLoop();
    }

    @Override
    public void showPage(@NonNull String key) {
        if (getPageSwitcher() != null) {
            getPageSwitcher().showPage(key);
        }
    }

    @Override
    public void showPage(@NonNull String key, IntentUtils.Extras extras) {
        if (getPageSwitcher() != null) {
            getPageSwitcher().showPage(key);
        }
    }

    @Override
    public void lastPage() {
        if (getPageSwitcher() != null) {
            getPageSwitcher().lastPage();
        }
    }

    @Override
    public void lastPage(IntentUtils.Extras extras) {
        if (getPageSwitcher() != null) {
            getPageSwitcher().lastPage(extras);
        }
    }

    @Override
    public void nextPage() {
        if (getPageSwitcher() != null) {
            getPageSwitcher().nextPage();
        }
    }

    @Override
    public void nextPage(IntentUtils.Extras extras) {
        if (getPageSwitcher() != null) {
            getPageSwitcher().nextPage(extras);
        }
    }

    public void showShortToast(String msg) {
        ToastUtils.showShort(getContext(), msg);
    }

    public void showLongToast(String msg) {
        ToastUtils.showLong(getContext(), msg);
    }

    /**
     * 由于fragment的构造方法不能传参，所以通过调用{@link Fragment#setArguments(Bundle)}传参后，可以在
     * {@link #onGetExtras(Bundle)}中拿到这些Extras。
     *
     * @param bundle
     */
    protected void onGetExtras(@Nullable Bundle bundle) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract int getLayoutId();

    @NonNull
    @Override
    public synchronized Cache<String, Object> loadCache() {
        if (mCache == null) {
            mCache = cacheFactory().build(CacheType.FRAGMENT_CACHE, getContext());
            Set<String> keys = KVUtils.getInstance(getContext()).cacheKeys();
            for (String key : keys) {
                Object cache = KVUtils.getInstance(getContext()).getCacheFromMemory(key);
                mCache.put(key, cache);
            }
        }
        return mCache;
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
}
