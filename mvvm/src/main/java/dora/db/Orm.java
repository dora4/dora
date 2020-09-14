package dora.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dora.db.exception.OrmStateException;

public class Orm {

    private static SQLiteDatabase sDatabase;
    private static SQLiteOpenHelper sHelper;
    private static int STATE_DATABASE_NOT_EXISTS = -1;
    private static int STATE_DATABASE_EXISTS = 0;
    private static int STATE_DATABASE_UPDATING = 1;
    private static int sDatabaseState = STATE_DATABASE_NOT_EXISTS;

    public static boolean isPrepared() {
        return sDatabaseState == STATE_DATABASE_EXISTS;
    }

    public static boolean isWaitingUpdate() {
        return sDatabaseState == STATE_DATABASE_UPDATING;
    }

    public static void update() {
        sDatabaseState = STATE_DATABASE_UPDATING;
    }

    public static SQLiteDatabase getDatabase() {
        if (isPrepared()) {
            return sDatabase;
        } else if (isWaitingUpdate()) {
            sDatabase = sHelper.getWritableDatabase();
            if (sDatabase != null) {
                sDatabaseState = STATE_DATABASE_EXISTS;
            }
            return sDatabase;
        } else {
            throw new OrmStateException("Database is not exists.");
        }
    }

    public synchronized static void init(Context context, String databaseName) {
        sHelper = new OrmSQLiteOpenHelper(context, databaseName, 1, null);
        sDatabase = sHelper.getWritableDatabase();
        if (sDatabase != null) {
            sDatabaseState = STATE_DATABASE_EXISTS;
        }
    }

    public synchronized static void init(Context context, OrmConfig config) {
        String name = config.getDatabaseName();
        int versionCode = config.getVersionCode();
        Class<? extends OrmTable>[] tables = config.getTables();
        sHelper = new OrmSQLiteOpenHelper(context, name, versionCode, tables);
        sDatabase = sHelper.getWritableDatabase();
        if (sDatabase != null) {
            sDatabaseState = STATE_DATABASE_EXISTS;
        }
    }
}
