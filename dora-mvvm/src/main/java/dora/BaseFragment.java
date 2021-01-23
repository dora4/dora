package dora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import dora.cache.Cache;
import dora.cache.CacheType;
import dora.cache.LruCache;
import dora.util.IntentUtils;
import dora.util.ToastUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements
        FragmentCache, PageSwitcher {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding = DataBindingUtil.bind(Objects.requireNonNull(getView()));
        mBinding.setLifecycleOwner(this);
        onSetupComponent();
        initData(savedInstanceState);
    }

    @Override
    public void onResume() {
        onGetExtras(getArguments());
        super.onResume();
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
        ToastUtils.showShort(msg);
    }

    public void toastL(String msg) {
        ToastUtils.showLong(msg);
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

    @NonNull
    @Override
    public synchronized Cache<String, Object> loadCache() {
        if (mCache == null) {
            mCache = cacheFactory().build(CacheType.FRAGMENT_CACHE, getContext());
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
