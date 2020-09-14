package dora.db.type;

/**
 * SQL of the SQLite database is enumerated, and the data types of all other database SQL statements
 * will eventually become the following five.
 */
public enum SqlType {

    /**
     * The value is the null value.
     */
    NULL,

    /**
     * The value is a signed integer that is stored at 1, 2, 3, 4, 6, or 8 bytes depending on the
     * size of the value.
     */
    INTEGER,

    /**
     * Values are floating-point Numbers, stored in 8-byte IEEE floating point Numbers.
     */
    REAL,

    /**
     * The value is a text string and is stored using database encoding
     * (utf-8, utf-16be or utf-16le).
     */
    TEXT,

    /**
     * A value is a block of data that is stored as it is typed.
     */
    BLOB
}
