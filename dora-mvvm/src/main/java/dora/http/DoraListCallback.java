package dora.http;

import dora.db.OrmTable;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public abstract class DoraListCallback<T extends OrmTable> implements Callback<ApiResult<List<T>>> {

    public abstract void onSuccess(List<T> data);

    public abstract void onFailure(int code, String msg);

    protected void onInterceptNetworkData(List<T> data) {
    }

    @Override
    public void onResponse(@NotNull Call<ApiResult<List<T>>> call, Response<ApiResult<List<T>>> response) {
        int code = response.code();
        if (code == 200) {
            ApiResult<List<T>> body = response.body();
            if (body != null) {
                List<T> data = body.getData();
                if (data != null) {
                    onSuccess(data);
                } else {
                    onFailure(1001, "空数据返回");
                }
            } else {
                onFailure(1002, "没有响应体");
            }
        } else {
            onFailure(code, "HTTP状态码："+code);
        }
    }

    @Override
    public void onFailure(@NotNull Call<ApiResult<List<T>>> call, Throwable t) {
        onFailure(-1, t.getMessage());
    }
}
