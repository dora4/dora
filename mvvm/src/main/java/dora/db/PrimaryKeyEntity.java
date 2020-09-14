package dora.db;

public class PrimaryKeyEntity {

    private String name;
    private String value;

    public PrimaryKeyEntity(String primaryKeyName, Number primaryKeyValue) {
        this.name = primaryKeyName;
        this.value = String.valueOf(primaryKeyValue);
    }

    public PrimaryKeyEntity(String primaryKeyName, String primaryKeyValue) {
        this.name = primaryKeyName;
        this.value = primaryKeyValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
