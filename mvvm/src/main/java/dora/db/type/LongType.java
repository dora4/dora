package dora.db.type;

public class LongType extends BaseDataType {

    private static final LongType mInstance = new LongType();

    public LongType() {
        super(SqlType.INTEGER);
    }

    public static LongType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{long.class, Long.class};
    }
}
