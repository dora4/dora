package dora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import dora.memory.DataLoader;
import dora.util.IntentUtils;
import dora.util.ToastUtils;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements
        DataLoader<T>, PageTransformer {

    protected T mBinding;
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
        assert mBinding != null;
        mBinding.setLifecycleOwner(this);
        initData(savedInstanceState, mBinding);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
    }

    public PageTransformer getPageTransformer() {
        if (getActivity() instanceof PageTransformer) {
            return (PageTransformer) getActivity();
        }
        return null;
    }

    @Override
    public final boolean isLoop() {
        // Dependents on activity.
        return getPageTransformer().isLoop();
    }

    @Override
    public void showPage(@NonNull String key) {
        if (getPageTransformer() != null) {
            getPageTransformer().showPage(key);
        }
    }

    @Override
    public void showPage(@NonNull String key, IntentUtils.Extras extras) {
        if (getPageTransformer() != null) {
            getPageTransformer().showPage(key);
        }
    }

    @Override
    public void lastPage() {
        if (getPageTransformer() != null) {
            getPageTransformer().lastPage();
        }
    }

    @Override
    public void lastPage(IntentUtils.Extras extras) {
        if (getPageTransformer() != null) {
            getPageTransformer().lastPage(extras);
        }
    }

    @Override
    public void nextPage() {
        if (getPageTransformer() != null) {
            getPageTransformer().nextPage();
        }
    }

    @Override
    public void nextPage(IntentUtils.Extras extras) {
        if (getPageTransformer() != null) {
            getPageTransformer().nextPage(extras);
        }
    }

    public void showShortToast(String msg) {
        ToastUtils.showShort(getContext(), msg);
    }

    public void showLongToast(String msg) {
        ToastUtils.showLong(getContext(), msg);
    }

    /**
     * Since the constructor of a Fragment cannot accept parameters, you can pass arguments by
     * calling {@link Fragment#setArguments(Bundle)}. Afterwards, you can access these arguments
     * in {@link #onGetExtras(Bundle)}.
     * 简体中文：由于fragment的构造方法不能传参，所以通过调用{@link Fragment#setArguments(Bundle)}传参后，可
     * 以在{@link #onGetExtras(Bundle)}中拿到这些Extras。
     */
    protected void onGetExtras(@Nullable Bundle bundle) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    /**
     * Specify the name of the fragment layout file.
     * 简体中文：指定fragment布局文件的名称。
     */
    protected abstract int getLayoutId();
}
