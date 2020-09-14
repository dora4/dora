package dora.permission.notify.option;

import dora.permission.notify.PermissionRequest;
import dora.permission.notify.listener.ListenerRequest;

public interface NotifyOption {

    /**
     * Handle permissions.
     */
    PermissionRequest permission();

    /**
     * Handle notify listener.
     */
    ListenerRequest listener();
}