/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;

public abstract class BaseVMActivity<B extends ViewDataBinding, VM extends ViewModel> extends BaseActivity<B> {

    protected VM mViewModel;

    private VM provideViewModel() {
        Class<VM> clazz = (Class<VM>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return new ViewModelProvider(this).get(clazz);
    }

    protected void onBindViewModel(@NonNull B binding, @NonNull VM viewModel) {
    }

    @CallSuper
    @Override
    public void initData(@Nullable Bundle savedInstanceState, @NonNull B binding) {
        this.mViewModel = provideViewModel();
        onBindViewModel(binding, mViewModel);
    }
}
