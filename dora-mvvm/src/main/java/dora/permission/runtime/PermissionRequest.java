package dora.permission.runtime;

import dora.permission.Action;
import dora.permission.Rationale;

import java.util.List;

public interface PermissionRequest {

    /**
     * One or more permissions.
     */
    PermissionRequest permission(String... permissions);

    /**
     * Set request rationale.
     */
    PermissionRequest rationale(Rationale<List<String>> rationale);

    /**
     * Action to be taken when all permissions are granted.
     */
    PermissionRequest onGranted(Action<List<String>> granted);

    /**
     * Action to be taken when all permissions are denied.
     */
    PermissionRequest onDenied(Action<List<String>> denied);

    /**
     * Request permission.
     */
    void start();
}