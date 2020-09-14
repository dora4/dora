package dora.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

class ContactsReadTest implements PermissionTest {

    private ContentResolver mResolver;

    ContactsReadTest(Context context) {
        mResolver = context.getContentResolver();
    }

    @Override
    public boolean test() throws Throwable {
        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DATA1};
        Cursor cursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }
}