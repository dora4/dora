package dora.permission.notify.listener;

import dora.permission.Action;
import dora.permission.Rationale;

public interface ListenerRequest {

    /**
     * Set request rationale.
     */
    ListenerRequest rationale(Rationale<Void> rationale);

    /**
     * Action to be taken when all permissions are granted.
     */
    ListenerRequest onGranted(Action<Void> granted);

    /**
     * Action to be taken when all permissions are denied.
     */
    ListenerRequest onDenied(Action<Void> denied);

    /**
     * Start install.
     */
    void start();

}