package dora.db.type;

public class DoubleType extends BaseDataType {

    private static final DoubleType mInstance = new DoubleType();

    public DoubleType() {
        super(SqlType.REAL);
    }

    public static DoubleType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{double.class, Double.class};
    }
}
