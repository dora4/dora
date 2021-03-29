package dora.http.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseRetrofitManager {

    Map<Class<?>, String> urlMap;
    Map<Class<?>, Retrofit> retrofitMap;

    protected BaseRetrofitManager() {
        urlMap = new HashMap<>();
        retrofitMap = new HashMap<>();
        initBaseUrl();
    }

    protected abstract void initBaseUrl();

    protected void registerBaseUrl(Class<? extends ApiService> serviceClazz, String baseUrl) {
        urlMap.put(serviceClazz, baseUrl);
    }

    protected abstract OkHttpClient createHttpClient();

    /**
     * 建议在单例的子类写一些静态方法getService，调用此方法。
     * <pre>
     *        public static <T> T getService(Class<T> clazz) {
     *         if (retrofitManager == null) {
     *             synchronized (RetrofitManager.class) {
     *                 if (retrofitManager == null) retrofitManager = new RetrofitManager();
     *             }
     *         }
     *         return retrofitManager._getService(clazz);
     *       }
     * </pre>
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T _getService(Class<T> clazz) {
        Retrofit retrofit;
        if (retrofitMap.containsKey(clazz)) {
            retrofit = retrofitMap.get(clazz);
            return retrofit.create(clazz);
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Objects.requireNonNull(urlMap.get(clazz)))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createHttpClient())
                    .build();
            retrofitMap.put(clazz, retrofit);
        }
        return retrofit.create(clazz);
    }
}
