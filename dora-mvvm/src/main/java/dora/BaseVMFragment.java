package dora;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseVMFragment<T extends ViewDataBinding, VM extends ViewModel> extends BaseFragment<T> {

    protected VM mVM;

    protected abstract VM provideViewModel();

    @CallSuper
    protected void onBindViewModel(@NonNull T binding, @NonNull VM viewModel) {
        this.mVM = provideViewModel();
    }

    @CallSuper
    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
        onBindViewModel(binding, mVM);
    }
}
