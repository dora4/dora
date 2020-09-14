package dora.db.dao;

import android.database.sqlite.SQLiteDatabase;

import dora.db.OrmTable;
import dora.db.builder.QueryBuilder;
import dora.db.builder.WhereBuilder;

import java.util.List;

public interface Dao<T extends OrmTable> {

    boolean insert(T bean);

    boolean insert(List<T> beans);

    boolean insertSafety(T bean, SQLiteDatabase db);

    boolean insertSafety(List<T> beans, SQLiteDatabase db);

    boolean delete(WhereBuilder builder);

    boolean delete(T bean);

    boolean deleteAll();

    boolean deleteSafety(WhereBuilder builder, SQLiteDatabase db);

    boolean deleteAllSafety(SQLiteDatabase db);

    boolean update(WhereBuilder builder, T newBean);

    boolean update(T bean);

    boolean updateAll(T newBean);

    boolean updateSafety(WhereBuilder builder, T newBean, SQLiteDatabase db);

    boolean updateAllSafety(T newBean, SQLiteDatabase db);

    List<T> selectAll();

    List<T> select(QueryBuilder builder);

    T selectOne();

    T selectOne(QueryBuilder builder);

    long selectCount();

    long selectCount(QueryBuilder builder);
}
