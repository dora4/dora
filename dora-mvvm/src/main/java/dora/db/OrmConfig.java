package dora.db;

import android.text.TextUtils;

public class OrmConfig {

    private String mDatabaseName;
    private int mVersionCode;
    private Class<? extends OrmTable>[] mTables;

    private OrmConfig(Builder builder) {
        mDatabaseName = builder.mDatabaseName;
        mVersionCode = builder.mVersionCode;
        mTables = builder.mTables;
    }

    public String getDatabaseName() {
        return mDatabaseName;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public Class<? extends OrmTable>[] getTables() {
        return mTables;
    }

    public static class Builder {

        private String mDatabaseName;
        private int mVersionCode = 1;
        private Class<? extends OrmTable>[] mTables;

        public Builder database(String name) {
            mDatabaseName = name;
            return this;
        }

        public Builder version(int code) {
            mVersionCode = code;
            return this;
        }

        public Builder tables(Class<? extends OrmTable>... tables) {
            mTables = tables;
            return this;
        }

        public OrmConfig build() {
            if (!TextUtils.isEmpty(mDatabaseName)) {
                return new OrmConfig(this);
            }
            return null;
        }
    }
}
