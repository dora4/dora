package dora.db.type;

import dora.db.DataMatcher;

public enum DataType {

    STRING(StringType.getInstance()),
    BOOLEAN(BooleanType.getInstance()),
    CHAR(CharType.getInstance()),
    BYTE(ByteType.getInstance()),
    SHORT(ShortType.getInstance()),
    INT(IntType.getInstance()),
    LONG(LongType.getInstance()),
    FLOAT(FloatType.getInstance()),
    DOUBLE(DoubleType.getInstance()),
    OTHER(ByteArrayType.getInstance());

    private final DataMatcher mMatcher;

    /* package */ DataType(DataMatcher matcher) {
        mMatcher = matcher;
    }

    public DataMatcher getMatcher() {
        return mMatcher;
    }
}
