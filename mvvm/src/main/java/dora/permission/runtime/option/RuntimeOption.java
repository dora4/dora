package dora.permission.runtime.option;

import android.support.annotation.NonNull;

import dora.permission.runtime.PermissionRequest;
import dora.permission.runtime.setting.SettingRequest;

public interface RuntimeOption {

    /**
     * One or more permissions.
     */
    PermissionRequest permission(@NonNull String... permissions);

    /**
     * One or more permission groups.
     */
    PermissionRequest permission(@NonNull String[]... groups);

    /**
     * Permission settings.
     */
    SettingRequest setting();
}