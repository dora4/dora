package dora.db.type;

public class FloatType extends BaseDataType {

    private static final FloatType mInstance = new FloatType();

    public FloatType() {
        super(SqlType.REAL);
    }

    public static FloatType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{float.class, Float.class};
    }
}
