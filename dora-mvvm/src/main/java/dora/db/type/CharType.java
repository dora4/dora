package dora.db.type;

public class CharType extends BaseDataType {

    private static final CharType mInstance = new CharType();

    public CharType() {
        super(SqlType.TEXT);
    }

    public static CharType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{char.class};
    }
}
