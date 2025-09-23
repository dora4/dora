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
    private final Map<String, ActivityResultLauncher<String>> permissionLaunchers = new HashMap<>();

    public static PermissionHelper with(@NonNull Context context) {
        ComponentActivity activity = findActivity(context);
        if (activity == null) {
            throw new IllegalArgumentException("Context must be an instance of ComponentActivity.");
        }
        return new PermissionHelper(activity);
    }

    private static ComponentActivity findActivity(Context context) {
        if (context instanceof ComponentActivity) {
            return (ComponentActivity) context;
        } else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static PermissionHelper with(@NonNull Fragment fragment) {
        assert fragment.getActivity() != null;
        return with(fragment.getActivity());
    }

    private PermissionHelper(ComponentActivity activity) {
        this.activity = activity;
    }

    public void requestPermission(String permission, PermissionCallback callback) {
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

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
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
