package dora.db;

public class PrimaryKeyId extends PrimaryKeyEntity {

    public PrimaryKeyId(long id) {
        super("_id", id);
    }
}
