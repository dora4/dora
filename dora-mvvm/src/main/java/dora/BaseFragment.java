package dora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;
import dora.util.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements
        FragmentCache, PageSwitcher {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();
    protected Cache<String, Object> mCache;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setLifecycleOwner(this);
        onSetupComponent();
        initData(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    public PageSwitcher getPageSwitcher() {
        if (getActivity() instanceof PageSwitcher) {
            return (PageSwitcher) getActivity();
        }
        return null;
    }

    @Override
    public void showPage(String name) {
        if (getPageSwitcher() != null) {
            getPageSwitcher().showPage(name);
        }
    }

    @Override
    public void showPage(String name, IntentUtils.Extras extras) {
        if (getPageSwitcher() != null) {
            getPageSwitcher().showPage(name);
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

    public void toast(String msg) {
        toast(GlobalContext.get(), msg);
    }

    public void toast(Context context, String msg) {
        ToastUtils.showShort(context, msg);
    }

    public void toastL(String msg) {
        toastL(GlobalContext.get(), msg);
    }

    public void toastL(Context context, String msg) {
        ToastUtils.showLong(context, msg);
    }

    public void openActivity(Class<? extends Activity> activityClazz) {
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        activity.startActivity(intent);
    }

    public void openActivityForResult(Class<? extends Activity> activityClazz, int requestCode) {
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        activity.startActivityForResult(intent, requestCode);
    }

    public void openActivity(Class<? extends Activity> activityClazz, IntentUtils.Extras extras) {
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivity(intent);
    }

    public void openActivityForResult(Class<? extends Activity> activityClazz, IntentUtils.Extras extras, int requestCode) {
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivityForResult(intent, requestCode);
    }

    public void openActivityWithString(Class<? extends Activity> activityClazz, String name, String extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivity(intent);
    }

    public void openActivityWithInteger(Class<? extends Activity> activityClazz, String name, int extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivity(intent);
    }

    public void openActivityWithBoolean(Class<? extends Activity> activityClazz, String name, boolean extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivity(intent);
    }

    public void openActivityWithSerializable(Class<? extends Activity> activityClazz, String name, Serializable extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        IntentUtils.Extras extras = new IntentUtils.Extras(map);
        FragmentActivity activity = requireActivity();
        Intent intent = new Intent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivity(intent);
    }

    /**
     * 安装Dagger的Component。
     */
    protected void onSetupComponent() {
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

    @Override
    public void onResume() {
        super.onResume();
        MultiLanguageUtils.getInstance().setConfiguration(getContext());
    }

    @NonNull
    @Override
    public synchronized Cache<String, Object> loadCache() {
        if (mCache == null) {
            mCache = cacheFactory().build(CacheType.FRAGMENT_CACHE, getContext());
            Set<String> keys = KeyValueUtils.getInstance().cacheKeys();
            for (String key : keys) {
                Object cache = KeyValueUtils.getInstance().getCacheFromMemory(key);
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
