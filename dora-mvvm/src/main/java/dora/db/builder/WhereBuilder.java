package dora.db.builder;

import dora.db.Condition;

public class WhereBuilder {

    private static final String WHERE = " WHERE ";

    private static final String EQUAL_HOLDER = "=?";

    private static final String NOT_EQUAL_HOLDER = "!=?";

    private static final String GREATER_THAN_HOLDER = ">?";

    private static final String LESS_THAN_HOLDER = "<?";

    private static final String GREATER_THAN_OR_EQUAL_TO_HOLDER = ">=?";

    private static final String LESS_THAN_OR_EQUAL_TO_HOLDER = "<=?";

    private static final String COMMA_HOLDER = ",?";

    private static final String HOLDER = "?";

    private static final String AND = " AND ";

    private static final String OR = " OR ";

    private static final String NOT = " NOT ";

    private static final String IN = " IN ";

    private static final String PARENTHESES_LEFT = "(";

    private static final String PARENTHESES_RIGHT = ")";

    private static final String SPACE = "";

    private String mWhereClause;

    private Object[] mWhereArgs;

    private WhereBuilder() {
    }

    private WhereBuilder(Condition condition) {
        this.mWhereClause = condition.getSelection();
        this.mWhereArgs = condition.getSelectionArgs();
    }

    private WhereBuilder(String whereClause, String[] whereArgs) {
        this.mWhereClause = whereClause;
        this.mWhereArgs = whereArgs;
    }

    public static WhereBuilder create() {
        return new WhereBuilder();
    }

    public static WhereBuilder create(String whereClause, String[] whereArgs) {
        return new WhereBuilder(whereClause, whereArgs);
    }

    public static WhereBuilder create(Condition condition) {
        return new WhereBuilder(condition);
    }

    public WhereBuilder and() {
        if (mWhereClause != null) {
            mWhereClause += AND;
        }
        return this;
    }

    public WhereBuilder or() {
        if (mWhereClause != null) {
            mWhereClause += OR;
        }
        return this;
    }

    public WhereBuilder not() {
        if (mWhereClause != null) {
            mWhereClause += NOT;
        } else {
            mWhereClause = NOT;
        }
        return this;
    }

    public WhereBuilder and(String whereClause, Object... whereArgs) {
        return append(AND, whereClause, whereArgs);
    }

    public WhereBuilder or(String whereClause, Object... whereArgs) {
        return append(OR, whereClause, whereArgs);
    }

    public WhereBuilder not(String whereClause, Object... whereArgs) {
        return not().parenthesesLeft().append(null, whereClause, whereArgs).parenthesesRight();
    }

    public WhereBuilder andNot(String whereClause, Object... whereArgs) {
        return and(not(whereClause, whereArgs));
    }

    public WhereBuilder orNot(String whereClause, Object... whereArgs) {
        return or(not(whereClause, whereArgs));
    }

    public WhereBuilder and(WhereBuilder builder) {
        String selection = builder.getSelection();
        String[] selectionArgs = builder.getSelectionArgs();
        return and(selection, new Object[]{selectionArgs});
    }

    public WhereBuilder or(WhereBuilder builder) {
        String selection = builder.getSelection();
        String[] selectionArgs = builder.getSelectionArgs();
        return or(selection, new Object[]{selectionArgs});
    }

    public WhereBuilder not(WhereBuilder builder) {
        String selection = builder.getSelection();
        String[] selectionArgs = builder.getSelectionArgs();
        return not(selection, new Object[]{selectionArgs});
    }

    public WhereBuilder andNot(WhereBuilder builder) {
        return and(not(builder));
    }

    public WhereBuilder orNot(WhereBuilder builder) {
        return or(not(builder));
    }

    public WhereBuilder parenthesesLeft() {
        if (mWhereClause != null) {
            mWhereClause += PARENTHESES_LEFT;
        } else {
            mWhereClause = PARENTHESES_LEFT;
        }
        return this;
    }

    public WhereBuilder parenthesesRight() {
        if (mWhereClause != null) {
            mWhereClause += PARENTHESES_RIGHT;
        }
        return this;
    }

    public WhereBuilder addWhereEqualTo(String column, Object value) {
        return append(null, column + EQUAL_HOLDER, value);
    }

    public WhereBuilder addWhereNotEqualTo(String column, Object value) {
        return append(null, column + NOT_EQUAL_HOLDER, value);
    }

    public WhereBuilder addWhereGreaterThan(String column, Object value) {
        return append(null, column + GREATER_THAN_HOLDER, value);
    }

    public WhereBuilder addWhereGreaterThanOrEqualTo(String column, Object value) {
        return append(null, column + GREATER_THAN_OR_EQUAL_TO_HOLDER, value);
    }

    public WhereBuilder addWhereLessThan(String column, Object value) {
        return append(null, column + LESS_THAN_HOLDER, value);
    }

    public WhereBuilder addWhereLessThanOrEqualTo(String column, Object value) {
        return append(null, column + LESS_THAN_OR_EQUAL_TO_HOLDER, value);
    }

    public WhereBuilder addWhereIn(String column, String... values) {
        return appendWhereIn(null, column, values);
    }

    public WhereBuilder andWhereEqualTo(String column, Object value) {
        return append(AND, column + EQUAL_HOLDER, value);
    }

    public WhereBuilder andWhereNotEqualTo(String column, Object value) {
        return append(AND, column + NOT_EQUAL_HOLDER, value);
    }

    public WhereBuilder andWhereGreatorThan(String column, Object value) {
        return append(AND, column + GREATER_THAN_HOLDER, value);
    }

    public WhereBuilder andWhereGreatorThanOrEqualTo(String column, Object value) {
        return append(AND, column + GREATER_THAN_OR_EQUAL_TO_HOLDER, value);
    }

    public WhereBuilder andWhereLessThan(String column, Object value) {
        return append(AND, column + LESS_THAN_HOLDER, value);
    }

    public WhereBuilder andWhereLessThanOrEqualTo(String column, Object value) {
        return append(AND, column + LESS_THAN_OR_EQUAL_TO_HOLDER, value);
    }

    public WhereBuilder andWhereIn(String column, String... values) {
        return appendWhereIn(AND, column, values);
    }

    public WhereBuilder orWhereEqualTo(String column, Object value) {
        return append(OR, column + EQUAL_HOLDER, value);
    }

    public WhereBuilder orWhereNotEqualTo(String column, Object value) {
        return append(OR, column + NOT_EQUAL_HOLDER, value);
    }

    public WhereBuilder orWhereGreatorThan(String column, Object value) {
        return append(OR, column + GREATER_THAN_HOLDER, value);
    }

    public WhereBuilder orWhereGreatorThanOrEqualTo(String column, Object value) {
        return append(OR, column + GREATER_THAN_OR_EQUAL_TO_HOLDER, value);
    }

    public WhereBuilder orWhereLessThan(String column, Object value) {
        return append(OR, column + LESS_THAN_HOLDER, value);
    }

    public WhereBuilder orWhereLessThanOrEqualTo(String column, Object value) {
        return append(OR, column + LESS_THAN_OR_EQUAL_TO_HOLDER, value);
    }

    public WhereBuilder orWhereIn(String column, String... values) {
        return appendWhereIn(OR, column, values);
    }

    public String[] toStringArgs(Object[] objArgs) {
        String[] tempValues = new String[objArgs.length];
        for (int i = 0; i < tempValues.length; i++) {
            tempValues[i] = String.valueOf(objArgs[i]);
        }
        return tempValues;
    }

    private WhereBuilder append(String connect, String whereClause, Object... whereArgs) {
        if (mWhereClause == null) {
            mWhereClause = whereClause;
            mWhereArgs = whereArgs;
        } else {
            if (connect != null) {
                mWhereClause += connect;
            }
            this.mWhereClause += whereClause;
            if (mWhereArgs == null) {
                mWhereArgs = whereArgs;
            } else {
                Object[] tempArgs = new Object[mWhereArgs.length + whereArgs.length];
                System.arraycopy(mWhereArgs, 0, tempArgs, 0, mWhereArgs.length);
                System.arraycopy(whereArgs, 0, tempArgs, mWhereArgs.length, whereArgs.length);
                mWhereArgs = tempArgs;
            }
        }
        return this;
    }

    private WhereBuilder appendWhereIn(String connect, String column, String[] values) {
        String whereIn = buildWhereIn(column, values.length);
        return append(connect, whereIn, new Object[]{values});
    }

    private String buildWhereIn(String column, int num) {
        StringBuilder sb = new StringBuilder(column).append(SPACE).append(IN).append(PARENTHESES_LEFT)
                .append(HOLDER);
        for (int i = 0; i < num - 1; i++) {
            sb.append(COMMA_HOLDER);
        }
        return sb.append(PARENTHESES_RIGHT).toString();
    }

    public String build() {
        return mWhereClause != null ? WHERE + mWhereClause : SPACE;
    }

    public String getSelection() {
        return mWhereClause;
    }

    public String[] getSelectionArgs() {
        return mWhereArgs != null ? toStringArgs(mWhereArgs) : null;
    }

    public WhereBuilder where(Condition condition) {
        mWhereClause = condition.getSelection();
        mWhereArgs = condition.getSelectionArgs();
        return this;
    }
}
