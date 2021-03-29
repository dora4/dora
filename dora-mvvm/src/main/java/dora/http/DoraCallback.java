package dora.http;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DoraCallback<T> implements Callback<ApiResult<T>> {

    public abstract void onSuccess(T data);

    public abstract void onFailure(int code, String msg);

    protected void onInterceptNetworkData(T data) {
    }

    @Override
    public void onResponse(@NotNull Call<ApiResult<T>> call, Response<ApiResult<T>> response) {
        int code = response.code();
        if (code == 200) {
            ApiResult<T> body = response.body();
            if (body != null) {
                T data = body.getData();
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
    public void onFailure(@NotNull Call<ApiResult<T>> call, Throwable t) {
        onFailure(-1, t.getMessage());
    }
}
