package dora.cache.data.visitor;

import java.util.ArrayList;
import java.util.List;

public class DefaultPageDataVisitor<T> extends BasePageDataVisitor<T> {

    @Override
    public List<T> getResult(List<T> data, int totalCount, int currentPage, int pageSize) {
        List<T> result = new ArrayList<>();
        int pageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        for (int i = 0; i < pageCount; i++) {
            result.add(data.get(currentPage * pageSize + i));
        }
        return result;
    }
}
