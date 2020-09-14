package dora.db.type;

public class ByteType extends BaseDataType {

    private static final ByteType mInstance = new ByteType();

    public ByteType() {
        super(SqlType.INTEGER);
    }

    public static ByteType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{byte.class, Byte.class};
    }
}
