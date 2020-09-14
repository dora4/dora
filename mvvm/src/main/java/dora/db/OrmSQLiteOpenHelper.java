package dora.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class OrmSQLiteOpenHelper extends SQLiteOpenHelper {

    private Class<? extends OrmTable>[] mTables;

    public OrmSQLiteOpenHelper(Context context, String name, int version,
                               Class<? extends OrmTable>[] tables) {
        super(context, name, null, version);
        this.mTables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (mTables != null && mTables.length > 0) {
            for (Class<? extends OrmTable> table : mTables) {
                TableManager.getInstance()._createTable(table, db);
            }
        }
    }

    private <T> T newOrmTableInstance(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            c.setAccessible(true);
            Class[] cls = c.getParameterTypes();
            if (cls.length == 0) {
                try {
                    return (T) c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                Object[] objs = new Object[cls.length];
                for (int i = 0; i < cls.length; i++) {
                    objs[i] = getPrimitiveDefaultValue(cls[i]);
                }
                try {
                    return (T) c.newInstance(objs);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Object getPrimitiveDefaultValue(Class clazz) {
        if (clazz.isPrimitive()) {
            return clazz == boolean.class ? false : 0;
        }
        return null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (mTables != null && mTables.length > 0 && newVersion > oldVersion) {
            for (int i = 0; i < mTables.length; i++) {
                Class<? extends OrmTable> table = mTables[i];
                OrmTable ormTable = newOrmTableInstance(mTables[i]);
                boolean isRecreated = ormTable.isUpgradeRecreated();
                if (isRecreated) {
                    TableManager.getInstance()._dropTable(table, db);
                    TableManager.getInstance()._createTable(table, db);
                } else {
                    TableManager.getInstance()._upgradeTable(table, db);
                }
            }
        }
    }
}
