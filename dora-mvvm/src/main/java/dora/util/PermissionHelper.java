package dora.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class PermissionHelper {

    private final ComponentActivity activity;
    private final Fragment fragment;
    private final Map<String, ActivityResultLauncher<String>> singleLaunchers = new HashMap<>();
    private ActivityResultLauncher<String[]> multiLauncher;
    private final Map<String, PermissionCallback> callbackMap = new HashMap<>();
    private String[] pendingPermissions;
    public static final int REQUEST_CODE_REQUEST_PERMISSION = 10001;

    private PermissionHelper(ComponentActivity activity) {
        this.activity = activity;
        this.fragment = null;
    }

    private PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.requireActivity();
    }

    public static PermissionHelper with(@NonNull Context context) {
        ComponentActivity act = findActivity(context);
        if (act == null) throw new IllegalArgumentException("Context must be a ComponentActivity");
        return new PermissionHelper(act);
    }

    public static PermissionHelper with(@NonNull Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    private static ComponentActivity findActivity(Context context) {
        if (context instanceof ComponentActivity) return (ComponentActivity) context;
        if (context instanceof ContextWrapper) return findActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    public PermissionHelper prepare(String... allPermissions) {
        if (activity == null && fragment == null) {
            throw new IllegalStateException("PermissionHelper not initialized. Call with(Context) or with(Fragment) first.");
        }
        if (multiLauncher == null) {
            multiLauncher = fragment != null
                    ? fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::onMultiResult)
                    : activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::onMultiResult);
        }
        for (String perm : allPermissions) {
            if (!singleLaunchers.containsKey(perm)) {
                ActivityResultLauncher<String> launcher = fragment != null
                        ? fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> onSingleResult(perm, granted))
                        : activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> onSingleResult(perm, granted));
                singleLaunchers.put(perm, launcher);
            }
        }
        return this;
    }

    public PermissionHelper permissions(String... permissions) {
        this.pendingPermissions = permissions;
        return this;
    }

    public static boolean hasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void handlePermanentlyDenied() {
        if (activity != null) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        } else if (fragment != null) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + fragment.requireContext().getPackageName()));
            fragment.startActivity(intent);
        }
    }

    public void request(PermissionCallback callback) {
        if (pendingPermissions == null || pendingPermissions.length == 0) return;
        boolean allGranted = true;
        for (String perm : pendingPermissions) {
            if (!hasPermission(activity, perm)) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            callback.onResult(true);
            return;
        }
        for (String perm : pendingPermissions) {
            if (isPermissionPermanentlyDenied(perm)) {
                handlePermanentlyDenied();
                return;
            }
        }
        if (pendingPermissions.length == 1) {
            ActivityResultLauncher<String> launcher = singleLaunchers.get(pendingPermissions[0]);
            if (launcher == null) throw new IllegalStateException("Launcher not registered for " + pendingPermissions[0]);
            callbackMap.put(pendingPermissions[0], callback);
            launcher.launch(pendingPermissions[0]);
        } else {
            if (multiLauncher == null) throw new IllegalStateException("Multi-permission launcher not registered");
            callbackMap.put("MULTI", callback);
            multiLauncher.launch(pendingPermissions);
        }
//        if (fragment != null) {
//            fragment.requestPermissions(pendingPermissions, REQUEST_CODE_REQUEST_PERMISSION);
//        } else if (activity != null) {
//            ActivityCompat.requestPermissions(activity, pendingPermissions, REQUEST_CODE_REQUEST_PERMISSION);
//        }
    }

    public boolean isPermissionPermanentlyDenied(String permission) {
        if (fragment != null) {
            return !fragment.shouldShowRequestPermissionRationale(permission)
                    && !hasPermission(fragment.getActivity(), permission);
        } else {
            return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                    && !hasPermission(activity, permission);
        }
    }

    private void onSingleResult(String permission, boolean granted) {
        PermissionCallback cb = callbackMap.remove(permission);
        if (cb != null) cb.onResult(granted);
    }

    private void onMultiResult(Map<String, Boolean> results) {
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

    public interface PermissionCallback {
        void onResult(boolean granted);
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
        public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS; // Deprecated Android 9+
        public static final String SEND_SMS = Manifest.permission.SEND_SMS;
        public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
        public static final String READ_SMS = Manifest.permission.READ_SMS;
        public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
        public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
        public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
        public static final String BODY_SENSORS_BACKGROUND = "android.permission.BODY_SENSORS_BACKGROUND"; // Android 14+
        public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;               // Before Android 12
        public static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;   // Android 12+
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
