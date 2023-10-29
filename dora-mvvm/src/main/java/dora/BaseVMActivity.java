package dora;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;

public abstract class BaseVMActivity<T extends ViewDataBinding, VM extends ViewModel> extends BaseActivity<T> {

    protected VM mViewModel;

    protected VM provideViewModel() {
        Class<VM> clazz = (Class<VM>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return new ViewModelProvider(this).get(clazz);
    }

    protected void onBindViewModel(@NonNull T binding, @NonNull VM viewModel) {
    }

    @CallSuper
    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
        this.mViewModel = provideViewModel();
        onBindViewModel(binding, mViewModel);
    }
}
