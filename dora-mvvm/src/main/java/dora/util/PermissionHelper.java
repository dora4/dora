package dora.util;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class PermissionHelper {

    private final ComponentActivity activity;
    private final Map<String, ActivityResultLauncher<String>> permissionLaunchers = new HashMap<>();

    public PermissionHelper(ComponentActivity activity) {
        this.activity = activity;
    }

    public void registerPermission(String permission, PermissionCallback callback) {
        ActivityResultLauncher<String> launcher =
        activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> callback.onResult(isGranted));
        permissionLaunchers.put(permission, launcher);
    }

    public void request(String permission) {
        ActivityResultLauncher<String> launcher = permissionLaunchers.get(permission);
        if (launcher != null) {
            launcher.launch(permission);
        }
    }

    public boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public interface PermissionCallback {

        void onResult(boolean granted);
    }
}
