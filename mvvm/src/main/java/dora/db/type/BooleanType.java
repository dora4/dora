package dora.db.type;

public class BooleanType extends BaseDataType {

    private static final BooleanType mInstance = new BooleanType();

    public BooleanType() {
        super(SqlType.INTEGER);
    }

    public static BooleanType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{boolean.class, Boolean.class};
    }
}
