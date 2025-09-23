package dora.util;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class PermissionHelper {

    private final ComponentActivity activity;
    private final Map<String, ActivityResultLauncher<String>> singleLaunchers = new HashMap<>();
    private ActivityResultLauncher<String[]> multiLauncher;
    private final Map<String, PermissionCallback> callbackMap = new HashMap<>();
    private String[] pendingPermissions;
    private Fragment fragment;

    private PermissionHelper(ComponentActivity activity) {
        this.activity = activity;
    }

    private PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.requireActivity();
    }

    public static PermissionHelper with(@NonNull Context context) {
        ComponentActivity activity = findActivity(context);
        if (activity == null)
            throw new IllegalArgumentException("Context must be a ComponentActivity");
        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(@NonNull Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    private static ComponentActivity findActivity(Context context) {
        if (context instanceof ComponentActivity) return (ComponentActivity) context;
        if (context instanceof ContextWrapper)
            return findActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    public PermissionHelper permissions(String... permissions) {
        this.pendingPermissions = permissions;
        return this;
    }

    public void request(PermissionCallback callback) {
        if (pendingPermissions == null || pendingPermissions.length == 0) return;
        boolean allGranted = true;
        for (String perm : pendingPermissions) {
            if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            callback.onResult(true);
            return;
        }
        if (pendingPermissions.length == 1) {
            ensureSingleLauncher(pendingPermissions[0]);
            callbackMap.put(pendingPermissions[0], callback);
            singleLaunchers.get(pendingPermissions[0]).launch(pendingPermissions[0]);
        } else {
            ensureMultiLauncher();
            callbackMap.put("MULTI", callback);
            multiLauncher.launch(pendingPermissions);
        }
    }

    private void ensureSingleLauncher(String permission) {
        if (singleLaunchers.containsKey(permission)) return;
        ActivityResultLauncher<String> launcher;
        if (fragment != null) {
            launcher = fragment.registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        PermissionCallback cb = callbackMap.remove(permission);
                        if (cb != null) cb.onResult(granted);
                    }
            );
        } else {
            launcher = activity.registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        PermissionCallback cb = callbackMap.remove(permission);
                        if (cb != null) cb.onResult(granted);
                    }
            );
        }
        singleLaunchers.put(permission, launcher);
    }

    private void ensureMultiLauncher() {
        if (multiLauncher != null) return;
        if (fragment != null) {
            multiLauncher = fragment.registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    results -> {
                        PermissionCallback cb = callbackMap.remove("MULTI");
                        if (cb != null) {
                            boolean allGranted = true;
                            for (Boolean granted : results.values()) {
                                if (!granted) {
                                    allGranted = false;
                                    break;
                                }
                            }
                            cb.onResult(allGranted);
                        }
                    }
            );
        } else {
            multiLauncher = activity.registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    results -> {
                        PermissionCallback cb = callbackMap.remove("MULTI");
                        if (cb != null) {
                            boolean allGranted = true;
                            for (Boolean granted : results.values()) {
                                if (!granted) {
                                    allGranted = false;
                                    break;
                                }
                            }
                            cb.onResult(allGranted);
                        }
                    }
            );
        }
    }

    public interface PermissionCallback {
        void onResult(boolean granted);
    }

    public static class PermissionCallbackAdapter {

        private final PermissionCallback callback;
        private final String[] permissions;

        public PermissionCallbackAdapter(PermissionCallback callback) {
            this.callback = callback;
            this.permissions = null;
        }

        public PermissionCallbackAdapter(PermissionCallback callback, String[] permissions) {
            this.callback = callback;
            this.permissions = permissions;
        }

        public void onResult(@NonNull Map<String, Boolean> results) {
            if (results.size() == 1 && permissions == null) {
                callback.onResult(results.values().iterator().next());
                return;
            }
            boolean allRequiredGranted = true;
            if (permissions != null) {
                for (String perm : permissions) {
                    if (!Boolean.TRUE.equals(results.get(perm))) {
                        allRequiredGranted = false;
                        break;
                    }
                }
            }
            callback.onResult(allRequiredGranted);
        }
    }

    public static class Permission {
        public static final String CAMERA = Manifest.permission.CAMERA;
        public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
        public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
        public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
        public static final String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
        public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE; // Deprecated Android 13+
        public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE; // Deprecated Android 11+
        public static final String MANAGE_EXTERNAL_STORAGE = Manifest.permission.MANAGE_EXTERNAL_STORAGE; // Android 11
        public static final String READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";   // Android 13+
        public static final String READ_MEDIA_VIDEO = "android.permission.READ_MEDIA_VIDEO";     // Android 13+
        public static final String READ_MEDIA_AUDIO = "android.permission.READ_MEDIA_AUDIO";     // Android 13+
        public static final String READ_MEDIA_VISUAL_USER_SELECTED = "android.permission.READ_MEDIA_VISUAL_USER_SELECTED"; // Android 14+
        public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        public static final String ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION; // Android 10+
        public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
        public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
        public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
        public static final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
        public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
        public static final String USE_SIP = Manifest.permission.USE_SIP;
        public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS; // Deprecated Android 9
        public static final String SEND_SMS = Manifest.permission.SEND_SMS;
        public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
        public static final String READ_SMS = Manifest.permission.READ_SMS;
        public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
        public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
        public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
        public static final String BODY_SENSORS_BACKGROUND = "android.permission.BODY_SENSORS_BACKGROUND"; // Android 14+
        public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;               // Before Android 12
        public static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;   // Deprecated Android 12+
        public static final String BLUETOOTH_SCAN = "android.permission.BLUETOOTH_SCAN";     // Android 12+
        public static final String BLUETOOTH_CONNECT = "android.permission.BLUETOOTH_CONNECT"; // Android 12+
        public static final String BLUETOOTH_ADVERTISE = "android.permission.BLUETOOTH_ADVERTISE"; // Android 12+
        public static final String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"; // Android 13+
        public static final String FOREGROUND_SERVICE = Manifest.permission.FOREGROUND_SERVICE;
        public static final String FOREGROUND_SERVICE_MEDIA_PLAYBACK = "android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK"; // Android 14+
        public static final String FOREGROUND_SERVICE_CAMERA = "android.permission.FOREGROUND_SERVICE_CAMERA";                 // Android 14+
        public static final String FOREGROUND_SERVICE_CONNECTED_DEVICE = "android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE"; // Android 14+
        public static final String INTERNET = Manifest.permission.INTERNET;
        public static final String NFC = Manifest.permission.NFC;
        public static final String NEARBY_WIFI_DEVICES = "android.permission.NEARBY_WIFI_DEVICES"; // Android 13+
        public static final String SCHEDULE_EXACT_ALARM = "android.permission.SCHEDULE_EXACT_ALARM"; // Android 12+
    }
}
