package dora.db.builder;

import dora.db.Condition;

public class QueryBuilder {

    private static final String GROUP_BY = " GROUP BY ";

    private static final String HAVING = " HAVING ";

    private static final String ORDER_BY = " ORDER BY ";

    private static final String LIMIT = " LIMIT ";

    private static final String COMMA = ",";

    private static final String SPACE = "";

    private String[] mColumns;

    private String mGroup;

    private String mHaving;

    private String mOrder;

    private String mLimit;

    private WhereBuilder mWhereBuilder;

    private QueryBuilder() {
        mWhereBuilder = WhereBuilder.create();
    }

    public QueryBuilder where(WhereBuilder builder) {
        mWhereBuilder = builder;
        return this;
    }

    public QueryBuilder where(Condition condition) {
        mWhereBuilder = WhereBuilder.create();
        mWhereBuilder.where(condition);
        return this;
    }

    public static QueryBuilder create() {
        return new QueryBuilder();
    }

    public QueryBuilder columns(String[] columns) {
        mColumns = columns;
        return this;
    }

    public QueryBuilder having(String having) {
        mHaving = HAVING + having;
        return this;
    }

    public QueryBuilder orderBy(String order) {
        mOrder = ORDER_BY + order;
        return this;
    }

    public QueryBuilder groupBy(String group) {
        mGroup = GROUP_BY + group;
        return this;
    }

    public QueryBuilder limit(int limit) {
        mLimit = LIMIT + limit;
        return this;
    }

    public QueryBuilder limit(int start, int end) {
        mLimit = LIMIT + start + COMMA + end;
        return this;
    }

    public String build() {
        return mWhereBuilder.build() + (mGroup != null ? mGroup : SPACE) + (mHaving != null ? mHaving : SPACE)
                + (mOrder != null ? mOrder : SPACE) + (mLimit != null ? mLimit : SPACE);
    }

    public WhereBuilder getWhereBuilder() {
        return mWhereBuilder;
    }

    public String getHaving() {
        return mHaving != null ? new StringBuilder(mHaving).delete(0, HAVING.length()).toString() : SPACE;
    }

    public String getOrder() {
        return mOrder != null ? new StringBuilder(mOrder).delete(0, ORDER_BY.length()).toString() : SPACE;
    }

    public String getGroup() {
        return mGroup != null ? new StringBuilder(mGroup).delete(0, GROUP_BY.length()).toString() : SPACE;
    }

    public String getLimit() {
        return mLimit != null ? new StringBuilder(mLimit).delete(0, LIMIT.length()).toString() : SPACE;
    }

    public String[] getColumns() {
        return mColumns;
    }
}
