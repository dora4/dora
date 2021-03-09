package dora.cache.data.visitor;

import java.util.List;

import dora.cache.data.page.IDataPager;

/**
 * 分页数据的访问者。
 *
 * @param <T>
 */
public interface IPageDataVisitor<T> {

    /**
     * 访问数据分页器。
     *
     * @param pager
     */
    void visitDataPager(IDataPager pager);

    /**
     * 过滤出符合要求的一页数据。
     *
     * @param data        样本数据
     * @param totalCount  数据总条数
     * @param currentPage 当前第几页
     * @param pageSize    每页数据条数
     * @return 该页的数据
     */
    List<T> getResult(List<T> data, int totalCount, int currentPage, int pageSize);
}
