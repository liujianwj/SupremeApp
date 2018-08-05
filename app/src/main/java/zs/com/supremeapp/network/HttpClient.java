package zs.com.supremeapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zs.com.supremeapp.BuildConfig;

/**
 * 网络请求
 * Created by liujian on 2018/8/4.
 */

public class HttpClient {
    private static HttpClient mHttpClient = null;

    private static final String BASE_URL = "https://api.github.com/";

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024; //缓存文件大小

    private Retrofit mRetrofit = null;

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private HttpClient() {
    }

    public static HttpClient newInstance(){
        if(mHttpClient == null){
            synchronized (HttpClient.class){
                if(mHttpClient == null){
                    mHttpClient = new HttpClient();
                }
            }
        }
        return mHttpClient;
    }

    public Retrofit buildRetrofit(){
        if(mRetrofit == null){
            mRetrofit = getRetrofitBuilder().build();
        }
        return mRetrofit;
    }

    private Retrofit.Builder getRetrofitBuilder(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        //设置缓存

        //设置请求头
       // HeaderInterceptor headerInterceptor = new HeaderInterceptor();
       // okHttpClientBuilder.addInterceptor(headerInterceptor);

        //公共参数
        AddQueryParameterInterceptor addQueryParameterInterceptor = new AddQueryParameterInterceptor();
        okHttpClientBuilder.addInterceptor(addQueryParameterInterceptor);

        //Log信息拦截器
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        //设置cookie
        okHttpClientBuilder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });

        //设置超时和重连
        okHttpClientBuilder.retryOnConnectionFailure(true);
        okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.connectTimeout(24, TimeUnit.SECONDS);



        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);
        return builder;
    }

    private static class AddQueryParameterInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("platform", "android")
                    .addQueryParameter("version", "1.0.0")
                    .build();
            Request request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        }
    }

    private static class HeaderInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request = originalRequest.newBuilder()
                    .header("Accept", "application/json")
                    .header("Token", "liujian")
                    .build();
            return chain.proceed(request);
        }
    }

    private static class CacheInterceptor implements Interceptor{
        private Context context;

        public CacheInterceptor(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Cache cache = cache(context);
            Request request = chain.request();
            if(!NetWorkUtils.isNetworkActive(context)){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetworkActive(context)) {
                int maxAge = 60;                  //在线缓存一分钟
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();

            } else {
                int maxStale = 60 * 60 * 24 * 4 * 7;     //离线缓存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    }



    private static Cache cache(Context context) {
        //设置缓存路径
        final File baseDir = getDiskCacheDir(context);
        final File cacheDir = new File(baseDir, "HttpResponseCache");
        //设置缓存 10M
        return new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }

    //获取缓存文件
    private static File getDiskCacheDir(Context context) {
        File cacheFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheFile = context.getExternalCacheDir();
        } else {
            //  /data/data/<application package>/cache
            cacheFile = context.getCacheDir();
        }
        return cacheFile;
    }


    public static class AddCookiesInterceptor implements Interceptor {
        private Context context;
        private String lang;

        public AddCookiesInterceptor(Context context, String lang) {
            super();
            this.context = context;
            this.lang = lang;

        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder builder = chain.request().newBuilder();
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
//            Observable.just(sharedPreferences.getString("cookie", ""))
//                    .subscribe(new Action1<String>() {
//                        @Override
//                        public void call(String cookie) {
//                            if (cookie.contains("lang=ch")){
//                                cookie = cookie.replace("lang=ch","lang="+lang);
//                            }
//                            if (cookie.contains("lang=en")){
//                                cookie = cookie.replace("lang=en","lang="+lang);
//                            }
//                            //添加cookie
////                        Log.d("http", "AddCookiesInterceptor"+cookie);
//                            builder.addHeader("cookie", cookie);
//                        }
//                    });
            return chain.proceed(builder.build());
        }
    }

    public class ReceivedCookiesInterceptor implements Interceptor {
        private Context context;
        SharedPreferences sharedPreferences;

        public ReceivedCookiesInterceptor(Context context) {
            super();
            this.context = context;
            sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            Log.d("http", "originalResponse" + originalResponse.toString());
            if (!originalResponse.headers("set-cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
//                Observable.from(originalResponse.headers("set-cookie"))
//                        .map(new Func1<String, String>() {
//                            @Override
//                            public String call(String s) {
//                                String[] cookieArray = s.split(";");
//                                return cookieArray[0];
//                            }
//                        })
//                        .subscribe(new Action1<String>() {
//                            @Override
//                            public void call(String cookie) {
//                                cookieBuffer.append(cookie).append(";");
//                            }
//                        });
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookie", cookieBuffer.toString());
                Log.d("http", "ReceivedCookiesInterceptor" + cookieBuffer.toString());
                editor.apply();
            }

            return originalResponse;
        }
    }
}
