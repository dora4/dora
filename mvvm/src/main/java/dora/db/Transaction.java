package dora.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public final class Transaction {

    private Transaction() {
    }

    public interface Worker {
        boolean doTransition(SQLiteDatabase db);
    }

    public static boolean execute(Worker worker) {
        SQLiteDatabase db = Orm.getDatabase();
        db.beginTransaction();
        try {
            boolean isOk = worker.doTransition(db);
            if (isOk) {
                db.setTransactionSuccessful();
            }
            return isOk;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return false;
    }
}
