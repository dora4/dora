package dora.db.type;

public class StringType extends BaseDataType {

    private static final StringType mInstance = new StringType();

    public StringType() {
        super(SqlType.TEXT);
    }

    public static StringType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{String.class};
    }
}
