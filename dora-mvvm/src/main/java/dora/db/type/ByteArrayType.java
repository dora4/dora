package dora.db.type;

public class ByteArrayType extends BaseDataType {

    private static final ByteArrayType mInstance = new ByteArrayType();

    public ByteArrayType() {
        super(SqlType.BLOB);
    }

    public static ByteArrayType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{byte[].class};
    }
}
