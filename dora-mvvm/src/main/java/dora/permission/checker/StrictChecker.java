package dora.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import dora.permission.runtime.Permission;

import java.util.List;

@Deprecated
public final class StrictChecker implements PermissionChecker {

    public StrictChecker() {
    }

    @Override
    public boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }

        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasPermission(Context context, List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPermission(Context context, String permission) {
        try {
            switch (permission) {
                case Permission.READ_CALENDAR:
                    return checkReadCalendar(context);
                case Permission.WRITE_CALENDAR:
                    return checkWriteCalendar(context);
                case Permission.CAMERA:
                    return checkCamera(context);
                case Permission.READ_CONTACTS:
                    return checkReadContacts(context);
                case Permission.WRITE_CONTACTS:
                    return checkWriteContacts(context);
                case Permission.GET_ACCOUNTS:
                    return true;
                case Permission.ACCESS_COARSE_LOCATION:
                    return checkCoarseLocation(context);
                case Permission.ACCESS_FINE_LOCATION:
                    return checkFineLocation(context);
                case Permission.RECORD_AUDIO:
                    return checkRecordAudio(context);
                case Permission.READ_PHONE_STATE:
                    return checkReadPhoneState(context);
                case Permission.CALL_PHONE:
                    return true;
                case Permission.READ_CALL_LOG:
                    return checkReadCallLog(context);
                case Permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(context);
                case Permission.ADD_VOICEMAIL:
                    return checkAddVoicemail(context);
                case Permission.USE_SIP:
                    return checkSip(context);
                case Permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Permission.BODY_SENSORS:
                    return checkSensors(context);
                case Permission.SEND_SMS:
                case Permission.RECEIVE_MMS:
                    return true;
                case Permission.READ_SMS:
                    return checkReadSms(context);
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_SMS:
                    return true;
                case Permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage(context);
                case Permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage(context);
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    private static boolean checkReadCalendar(Context context) throws Throwable {
        PermissionTest test = new CalendarReadTest(context);
        return test.test();
    }

    private static boolean checkWriteCalendar(Context context) throws Throwable {
        PermissionTest test = new CalendarWriteTest(context);
        return test.test();
    }

    private static boolean checkCamera(Context context) throws Throwable {
        PermissionTest test = new CameraTest(context);
        return test.test();
    }

    private static boolean checkReadContacts(Context context) throws Throwable {
        PermissionTest test = new ContactsReadTest(context);
        return test.test();
    }

    private static boolean checkWriteContacts(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        PermissionTest test = new ContactsWriteTest(resolver);
        return test.test();
    }

    private static boolean checkCoarseLocation(Context context) throws Throwable {
        PermissionTest test = new LocationCoarseTest(context);
        return test.test();
    }

    private static boolean checkFineLocation(Context context) throws Throwable {
        PermissionTest test = new LocationFineTest(context);
        return test.test();
    }

    private static boolean checkRecordAudio(Context context) throws Throwable {
        PermissionTest test = new RecordAudioTest(context);
        return test.test();
    }

    private static boolean checkReadPhoneState(Context context) throws Throwable {
        PermissionTest test = new PhoneStateReadTest(context);
        return test.test();
    }

    private static boolean checkReadCallLog(Context context) throws Throwable {
        PermissionTest test = new CallLogReadTest(context);
        return test.test();
    }

    private static boolean checkWriteCallLog(Context context) throws Throwable {
        PermissionTest test = new CallLogWriteTest(context);
        return test.test();
    }

    private static boolean checkAddVoicemail(Context context) throws Throwable {
        PermissionTest test = new AddVoiceMailTest(context);
        return test.test();
    }

    private static boolean checkSip(Context context) throws Throwable {
        PermissionTest test = new SipTest(context);
        return test.test();
    }

    private static boolean checkSensors(Context context) throws Throwable {
        PermissionTest test = new SensorsTest(context);
        return test.test();
    }

    private static boolean checkReadSms(Context context) throws Throwable {
        PermissionTest test = new SmsReadTest(context);
        return test.test();
    }

    private static boolean checkReadStorage(Context context) throws Throwable {
        return ActivityCompat.checkSelfPermission(context, Permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkWriteStorage(Context context) throws Throwable {
        return ActivityCompat.checkSelfPermission(context, Permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}