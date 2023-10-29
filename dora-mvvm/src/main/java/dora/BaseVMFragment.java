package dora;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseVMFragment<T extends ViewDataBinding, VM extends ViewModel> extends BaseFragment<T> {

    protected VM mViewModel;

    protected abstract VM provideViewModel();

    protected void onBindViewModel(@NonNull T binding, @NonNull VM viewModel) {
    }

    @CallSuper
    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
        this.mViewModel = provideViewModel();
        onBindViewModel(binding, mViewModel);
    }
}
