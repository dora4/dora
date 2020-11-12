package dora.db.type;

public class IntType extends BaseDataType {

    private static final IntType mInstance = new IntType();

    public IntType() {
        super(SqlType.INTEGER);
    }

    public static IntType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{int.class, Integer.class};
    }
}
