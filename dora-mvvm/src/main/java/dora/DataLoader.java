package dora;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public interface DataLoader<T extends ViewDataBinding> {

    void initData(@Nullable Bundle savedInstanceState, @NonNull T binding);
}
