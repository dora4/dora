package dora.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dora.db.Condition;
import dora.db.Orm;
import dora.db.OrmTable;
import dora.db.PrimaryKeyEntity;
import dora.db.TableManager;
import dora.db.builder.QueryBuilder;
import dora.db.builder.WhereBuilder;
import dora.db.constraint.AssignType;
import dora.db.constraint.PrimaryKey;
import dora.db.table.Column;
import dora.db.table.Ignore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class OrmDao<T extends OrmTable> implements Dao<T> {

    private Class<T> mBeanClass;
    private SQLiteDatabase mDatabase;

    /* package */ OrmDao(Class<T> beanClass) {
        this.mBeanClass = beanClass;
        mDatabase = Orm.getDatabase();
    }

    private boolean isAssignableFromBoolean(Class<?> fieldType) {
        return boolean.class.isAssignableFrom(fieldType) ||
                Boolean.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromByte(Class<?> fieldType) {
        return byte.class.isAssignableFrom(fieldType) || Byte.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromShort(Class<?> fieldType) {
        return short.class.isAssignableFrom(fieldType) || Short.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromInteger(Class<?> fieldType) {
        return int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromLong(Class<?> fieldType) {
        return long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromFloat(Class<?> fieldType) {
        return float.class.isAssignableFrom(fieldType) || Float.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromDouble(Class<?> fieldType) {
        return double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromCharacter(Class<?> fieldType) {
        return char.class.isAssignableFrom(fieldType) || Character.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromCharSequence(Class<?> fieldType) {
        return CharSequence.class.isAssignableFrom(fieldType);
    }

    private boolean isAssignableFromClass(Class<?> fieldType) {
        return Class.class.isAssignableFrom(fieldType);
    }

    private ContentValues getContentValues(T bean) {
        ContentValues values = new ContentValues();
        Field[] fields = mBeanClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Ignore ignore = field.getAnnotation(Ignore.class);
            Column column = field.getAnnotation(Column.class);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (ignore != null || (field.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            if (primaryKey != null && primaryKey.value() == AssignType.AUTO_INCREMENT) {
                continue;
            }
            String columnName;
            if (column != null) {
                columnName = column.value();
            } else {
                columnName = TableManager.getInstance().generateColumnName(field.getName());
            }
            Class<?> fieldType = field.getType();
            try {
                if (isAssignableFromCharSequence(fieldType)) {
                    values.put(columnName, String.valueOf(field.get(bean)));
                } else if (isAssignableFromBoolean(fieldType)) {
                    values.put(columnName, field.getBoolean(bean));
                } else if (isAssignableFromByte(fieldType)) {
                    values.put(columnName, field.getByte(bean));
                } else if (isAssignableFromShort(fieldType)) {
                    values.put(columnName, field.getShort(bean));
                } else if (isAssignableFromInteger(fieldType)) {
                    values.put(columnName, field.getInt(bean));
                } else if (isAssignableFromLong(fieldType)) {
                    values.put(columnName, field.getLong(bean));
                } else if (isAssignableFromFloat(fieldType)) {
                    values.put(columnName, field.getFloat(bean));
                } else if (isAssignableFromDouble(fieldType)) {
                    values.put(columnName, field.getDouble(bean));
                } else if (Class.class.isAssignableFrom(fieldType)) {
                    values.put(columnName, ((Class) field.get(bean)).getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    private String getColumnHack() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = mBeanClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Ignore ignore = field.getAnnotation(Ignore.class);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (ignore == null && (primaryKey == null ||
                    (primaryKey != null && primaryKey.value() == AssignType.BY_MYSELF))) {
                String name = field.getName();
                sb.append(name).append(",");
            }
        }
        return sb.substring(0, sb.length() - 2);
    }

    @Override
    public boolean insert(T bean) {
        return insertSafety(bean, mDatabase);
    }

    @Override
    public boolean insert(List<T> beans) {
        return insertSafety(beans, mDatabase);
    }

    @Override
    public boolean insertSafety(List<T> beans, SQLiteDatabase db) {
        int count = 0;
        for (T bean : beans) {
            boolean isOk = insertSafety(bean, db);
            if (isOk) {
                count++;
            }
        }
        return count == beans.size();
    }

    @Override
    public boolean insertSafety(T bean, SQLiteDatabase db) {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        ContentValues contentValues = getContentValues(bean);
        return db.insert(tableName, getColumnHack(), contentValues) > 0;
    }

    @Override
    public boolean delete(WhereBuilder builder) {
        return deleteSafety(builder, mDatabase);
    }

    @Override
    public boolean delete(T bean) {
        PrimaryKeyEntity primaryKey = bean.getPrimaryKey();
        String name = primaryKey.getName();
        String value = primaryKey.getValue();
        return deleteSafety(WhereBuilder.create(new Condition(name + "=?", new String[]{value})), mDatabase);
    }

    @Override
    public boolean deleteAll() {
        return deleteAllSafety(mDatabase);
    }

    @Override
    public boolean deleteAllSafety(SQLiteDatabase db) {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        return db.delete(tableName, null, null) > 0;
    }

    @Override
    public boolean deleteSafety(WhereBuilder builder, SQLiteDatabase db) {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        return db.delete(tableName, builder.getSelection(), builder.getSelectionArgs()) > 0;
    }

    @Override
    public boolean update(WhereBuilder builder, final T newBean) {
        return updateSafety(builder, newBean, mDatabase);
    }

    @Override
    public boolean update(T bean) {
        PrimaryKeyEntity primaryKey = bean.getPrimaryKey();
        String name = primaryKey.getName();
        String value = primaryKey.getValue();
        return updateSafety(WhereBuilder.create(new Condition(name + "=?", new String[]{value})),
                bean, mDatabase);
    }

    @Override
    public boolean updateAll(T newBean) {
        return updateAllSafety(newBean, mDatabase);
    }

    @Override
    public boolean updateAllSafety(T newBean, SQLiteDatabase db) {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        ContentValues contentValues = getContentValues(newBean);
        return db.update(tableName, contentValues, null, null) > 0;
    }

    @Override
    public boolean updateSafety(WhereBuilder builder, T newBean, SQLiteDatabase db) {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        ContentValues contentValues = getContentValues(newBean);
        return db.update(tableName, contentValues, builder.getSelection(), builder.getSelectionArgs()) > 0;
    }

    @Override
    public List<T> selectAll() {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        Cursor cursor = mDatabase.query(tableName, null, null, null, null, null, null);
        return getResult(cursor);
    }

    @Override
    public List<T> select(QueryBuilder builder) {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        String[] columns = builder.getColumns();
        String group = builder.getGroup();
        String having = builder.getHaving();
        String order = builder.getOrder();
        String limit = builder.getLimit();
        WhereBuilder where = builder.getWhereBuilder();
        String selection = where.getSelection();
        String[] selectionArgs = where.getSelectionArgs();
        Cursor cursor = mDatabase.query(tableName, columns, selection, selectionArgs, group, having, order, limit);
        return getResult(cursor);
    }

    @Override
    public T selectOne() {
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        Cursor cursor = mDatabase.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            try {
                T bean = createResult(cursor);
                return bean;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public T selectOne(QueryBuilder builder) {
        List<T> beans = select(builder);
        if (beans.size() > 0) {
            return beans.get(0);
        }
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        String[] columns = builder.getColumns();
        String group = builder.getGroup();
        String having = builder.getHaving();
        String order = builder.getOrder();
        String limit = builder.getLimit();
        WhereBuilder where = builder.getWhereBuilder();
        String selection = where.getSelection();
        String[] selectionArgs = where.getSelectionArgs();
        Cursor cursor = mDatabase.query(tableName, columns, selection, selectionArgs, group, having, order, limit);
        if (cursor.moveToFirst()) {
            try {
                T bean = createResult(cursor);
                return bean;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long selectCount() {
        long count = 0;
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getLong(0);
            cursor.close();
        }
        return count;
    }

    @Override
    public long selectCount(QueryBuilder builder) {
        long count = 0;
        TableManager manager = TableManager.getInstance();
        String tableName = manager.getTableName(mBeanClass);
        String sql = builder.build();
        Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM " + tableName + sql,
                builder.getWhereBuilder().getSelectionArgs());
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getLong(0);
            cursor.close();
        }
        return count;
    }

    private List<T> getResult(Cursor cursor) {
        List<T> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                T bean = createResult(cursor);
                result.add(bean);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private <T extends OrmTable> T newOrmTableInstance(Class<T> clazz) {
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

    private T createResult(Cursor cursor) throws IllegalAccessException,
            InstantiationException, InvocationTargetException {
        T bean = newOrmTableInstance(mBeanClass);
        Field[] fields = mBeanClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String columnName;
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columnName = column.value();
            } else {
                columnName = TableManager.getInstance().generateColumnName(field.getName());
            }
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex != -1) {
                Class<?> fieldType = field.getType();
                if (isAssignableFromCharSequence(fieldType)) {
                    field.set(bean, cursor.getString(columnIndex));
                } else if (isAssignableFromBoolean(fieldType)) {
                    int value = cursor.getInt(columnIndex);
                    field.set(bean, value == 1 ? true : false);
                } else if (isAssignableFromLong(fieldType)) {
                    field.set(bean, cursor.getLong(columnIndex));
                } else if (isAssignableFromInteger(fieldType)) {
                    field.set(bean, cursor.getInt(columnIndex));
                } else if (isAssignableFromShort(fieldType)
                        || isAssignableFromByte(fieldType)) {
                    field.set(bean, cursor.getShort(columnIndex));
                } else if (isAssignableFromDouble(fieldType)) {
                    field.set(bean, cursor.getDouble(columnIndex));
                } else if (isAssignableFromFloat(fieldType)) {
                    field.set(bean, cursor.getFloat(columnIndex));
                } else if (isAssignableFromCharacter(fieldType)) {
                    field.set(bean, cursor.getString(columnIndex));
                } else if (isAssignableFromClass(fieldType)) {
                    try {
                        field.set(bean, Class.forName(cursor.getString(columnIndex)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    field.set(bean, cursor.getBlob(columnIndex));
                }
            }
        }
        return bean;
    }
}
