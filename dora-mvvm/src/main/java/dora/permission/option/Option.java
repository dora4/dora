package dora.permission.option;

import dora.permission.install.InstallRequest;
import dora.permission.notify.option.NotifyOption;
import dora.permission.overlay.OverlayRequest;
import dora.permission.runtime.option.RuntimeOption;
import dora.permission.setting.Setting;

@Deprecated
public interface Option {

    /**
     * Handle runtime permissions.
     */
    RuntimeOption runtime();

    /**
     * Handle request package install permission.
     */
    InstallRequest install();

    /**
     * Handle overlay permission.
     */
    OverlayRequest overlay();

    /**
     * Handle notification permission.
     */
    NotifyOption notification();

    /**
     * Handle system setting.
     */
    Setting setting();
}