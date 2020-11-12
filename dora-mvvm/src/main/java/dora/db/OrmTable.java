package dora.db;

public interface OrmTable {

    /**
     * Gets the unique identifier's value.
     *
     * @return The primary key value.
     */
    PrimaryKeyEntity getPrimaryKey();

    /**
     * @return If true, it will drop table first and recreate the table when the table is
     * upgraded.Instead,it will expand directly on the previous table.
     */
    boolean isUpgradeRecreated();
}
