package dora.db.type;

public class ShortType extends BaseDataType {

    private static final ShortType mInstance = new ShortType();

    public ShortType() {
        super(SqlType.INTEGER);
    }

    public static ShortType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{short.class, Short.class};
    }
}
