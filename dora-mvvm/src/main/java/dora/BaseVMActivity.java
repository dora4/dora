package dora;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseVMActivity<T extends ViewDataBinding, VM extends ViewModel> extends BaseActivity<T> {

    protected VM mVM;

    protected abstract VM provideViewModel();

    @CallSuper
    protected void onBindViewModel(@NonNull T binding, @NonNull VM viewModel) {
    }

    @CallSuper
    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull T binding) {
        this.mVM = provideViewModel();
        onBindViewModel(binding, mVM);
    }
}
