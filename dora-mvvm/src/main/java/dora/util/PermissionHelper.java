package dora.util;

import android.Manifest;
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

    public enum Permission {

        CAMERA(Manifest.permission.CAMERA),
        RECORD_AUDIO(Manifest.permission.RECORD_AUDIO),
        READ_CONTACTS(Manifest.permission.READ_CONTACTS),
        WRITE_CONTACTS(Manifest.permission.WRITE_CONTACTS),
        GET_ACCOUNTS(Manifest.permission.GET_ACCOUNTS),
        READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),   // Deprecated Android 13+
        WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE), // Deprecated Android 11+
        MANAGE_EXTERNAL_STORAGE(Manifest.permission.MANAGE_EXTERNAL_STORAGE), // Android 11
        READ_MEDIA_IMAGES(Manifest.permission.READ_MEDIA_IMAGES),   // Android 13+
        READ_MEDIA_VIDEO(Manifest.permission.READ_MEDIA_VIDEO),     // Android 13+
        READ_MEDIA_AUDIO(Manifest.permission.READ_MEDIA_AUDIO),     // Android 13+
        READ_MEDIA_VISUAL_USER_SELECTED(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED), // Android 14+
        ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
        ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
        ACCESS_BACKGROUND_LOCATION(Manifest.permission.ACCESS_BACKGROUND_LOCATION), // Android 10+
        READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE),
        CALL_PHONE(Manifest.permission.CALL_PHONE),
        READ_CALL_LOG(Manifest.permission.READ_CALL_LOG),
        WRITE_CALL_LOG(Manifest.permission.WRITE_CALL_LOG),
        ADD_VOICEMAIL(Manifest.permission.ADD_VOICEMAIL),
        USE_SIP(Manifest.permission.USE_SIP),
        PROCESS_OUTGOING_CALLS(Manifest.permission.PROCESS_OUTGOING_CALLS), // Android 9 Deprecated
        SEND_SMS(Manifest.permission.SEND_SMS),
        RECEIVE_SMS(Manifest.permission.RECEIVE_SMS),
        READ_SMS(Manifest.permission.READ_SMS),
        RECEIVE_WAP_PUSH(Manifest.permission.RECEIVE_WAP_PUSH),
        RECEIVE_MMS(Manifest.permission.RECEIVE_MMS),
        BODY_SENSORS(Manifest.permission.BODY_SENSORS),
        BODY_SENSORS_BACKGROUND(Manifest.permission.BODY_SENSORS_BACKGROUND), // Android 14+
        BLUETOOTH(Manifest.permission.BLUETOOTH),               // Before Android 12
        BLUETOOTH_ADMIN(Manifest.permission.BLUETOOTH_ADMIN),   // After Android 12
        BLUETOOTH_SCAN(Manifest.permission.BLUETOOTH_SCAN),     // Android 12+
        BLUETOOTH_CONNECT(Manifest.permission.BLUETOOTH_CONNECT), // Android 12+
        BLUETOOTH_ADVERTISE(Manifest.permission.BLUETOOTH_ADVERTISE), // Android 12+
        POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS), // Android 13+
        FOREGROUND_SERVICE(Manifest.permission.FOREGROUND_SERVICE),
        FOREGROUND_SERVICE_MEDIA_PLAYBACK(Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK), // Android 14+
        FOREGROUND_SERVICE_CAMERA(Manifest.permission.FOREGROUND_SERVICE_CAMERA),                 // Android 14+
        FOREGROUND_SERVICE_CONNECTED_DEVICE(Manifest.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE), // Android 14+
        INTERNET(Manifest.permission.INTERNET),
        NFC(Manifest.permission.NFC),
        NEARBY_WIFI_DEVICES(Manifest.permission.NEARBY_WIFI_DEVICES), // Android 13+
        SCHEDULE_EXACT_ALARM(Manifest.permission.SCHEDULE_EXACT_ALARM), // Android 12+
        ;

        private final String value;

        Permission(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
