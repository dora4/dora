package dora.permission.runtime;

import android.content.Context;
import android.os.Build;
import dora.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Permissions.</p>
 */
@Deprecated
public class Permission {

    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    public static final String CAMERA = "android.permission.CAMERA";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    public static final String ADD_VOICEMAIL_MANIFEST = "android.permission.ADD_VOICEMAIL";
    public static final String USE_SIP = "android.permission.USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    public static final String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";

    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";

    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final class Group {

        public static final String[] CALENDAR = new String[]{Permission.READ_CALENDAR, Permission.WRITE_CALENDAR};

        public static final String[] CAMERA = new String[]{Permission.CAMERA};

        public static final String[] CONTACTS = new String[]{Permission.READ_CONTACTS, Permission.WRITE_CONTACTS,
                Permission.GET_ACCOUNTS};

        public static final String[] LOCATION = new String[]{Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION};

        public static final String[] MICROPHONE = new String[]{Permission.RECORD_AUDIO};

        public static final String[] PHONE;

        static {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PHONE = new String[]{Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.READ_CALL_LOG,
                        Permission.WRITE_CALL_LOG, Permission.ADD_VOICEMAIL, Permission.USE_SIP,
                        Permission.PROCESS_OUTGOING_CALLS, Permission.READ_PHONE_NUMBERS, Permission.ANSWER_PHONE_CALLS};
            } else {
                PHONE = new String[]{Permission.READ_PHONE_STATE, Permission.CALL_PHONE, Permission.READ_CALL_LOG,
                        Permission.WRITE_CALL_LOG, Permission.ADD_VOICEMAIL, Permission.USE_SIP,
                        Permission.PROCESS_OUTGOING_CALLS};
            }
        }

        public static final String[] SENSORS = new String[]{Permission.BODY_SENSORS};

        public static final String[] SMS = new String[]{Permission.SEND_SMS, Permission.RECEIVE_SMS,
                Permission.READ_SMS, Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS};

        public static final String[] STORAGE = new String[]{Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String... permissions) {
        return transformText(context, Arrays.asList(permissions));
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        return transformText(context, permissionList);
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, List<String> permissions) {
        List<String> textList = new ArrayList<>();
        for (String permission : permissions) {
            switch (permission) {
                case Permission.READ_CALENDAR:
                case Permission.WRITE_CALENDAR: {
                    String message = context.getString(R.string.permission_name_calendar);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }

                case Permission.CAMERA: {
                    String message = context.getString(R.string.permission_name_camera);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.READ_CONTACTS:
                case Permission.WRITE_CONTACTS: {
                    String message = context.getString(R.string.permission_name_contacts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.GET_ACCOUNTS: {
                    String message = context.getString(R.string.permission_name_accounts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.ACCESS_FINE_LOCATION:
                case Permission.ACCESS_COARSE_LOCATION: {
                    String message = context.getString(R.string.permission_name_location);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.RECORD_AUDIO: {
                    String message = context.getString(R.string.permission_name_microphone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.READ_PHONE_STATE:
                case Permission.CALL_PHONE:
                case Permission.READ_CALL_LOG:
                case Permission.WRITE_CALL_LOG:
                case Permission.ADD_VOICEMAIL:
                case Permission.ADD_VOICEMAIL_MANIFEST:
                case Permission.USE_SIP:
                case Permission.PROCESS_OUTGOING_CALLS:
                case Permission.READ_PHONE_NUMBERS:
                case Permission.ANSWER_PHONE_CALLS: {
                    String message = context.getString(R.string.permission_name_phone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.BODY_SENSORS: {
                    String message = context.getString(R.string.permission_name_sensors);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.SEND_SMS:
                case Permission.RECEIVE_SMS:
                case Permission.READ_SMS:
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_MMS: {
                    String message = context.getString(R.string.permission_name_sms);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case Permission.READ_EXTERNAL_STORAGE:
                case Permission.WRITE_EXTERNAL_STORAGE: {
                    String message = context.getString(R.string.permission_name_storage);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
            }
        }
        return textList;
    }

}