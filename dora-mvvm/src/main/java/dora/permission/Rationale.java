package dora.permission;

import android.content.Context;

@Deprecated
public interface Rationale<T> {

    /**
     * Show rationale to user.
     *
     * @param context  context.
     * @param data     the data.
     * @param executor executor.
     */
    void showRationale(Context context, T data, RequestExecutor executor);
}