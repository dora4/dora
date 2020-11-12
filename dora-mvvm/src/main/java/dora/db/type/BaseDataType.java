package dora.db.type;

import dora.db.DataMatcher;

import java.lang.reflect.Field;

public abstract class BaseDataType implements DataMatcher {

    private final SqlType mSqlType;

    public BaseDataType(SqlType sqlType) {
        this.mSqlType = sqlType;
    }

    public SqlType getSqlType() {
        return mSqlType;
    }

    public boolean matches(Field field) {
        Class<?>[] types = getTypes();
        for (Class<?> type : types) {
            if (type.isAssignableFrom(field.getType())) {
                return true;
            }
        }
        return false;
    }
}
