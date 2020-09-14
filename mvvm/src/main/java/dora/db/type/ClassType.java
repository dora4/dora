package dora.db.type;

public class ClassType extends BaseDataType {

    private static final ClassType mInstance = new ClassType();

    public ClassType() {
        super(SqlType.TEXT);
    }

    public static ClassType getInstance() {
        return mInstance;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{Class.class};
    }
}
