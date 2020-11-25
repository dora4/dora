package dora;

import androidx.annotation.NonNull;

import dora.util.NetworkUtils;

/**
 * 数据仓库。
 */
public abstract class BaseRepository {

    protected void selectData(@NonNull DataSource ds) {
        if (useCache()) {
            ds.loadFromDb();
        } else {
            //加载成功后，更新数据库
            ds.loadFromNet();
        }
    }

    protected boolean useCache() {
        return !NetworkUtils.checkNetwork();
    }

    public interface DataSource {
        void loadFromDb();
        void loadFromNet();
    }
}
