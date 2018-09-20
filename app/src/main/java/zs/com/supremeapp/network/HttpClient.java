package zs.com.supremeapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zs.com.supremeapp.BuildConfig;

/**
 * 网络请求
 * Created by liujian on 2018/8/4.
 */

public class HttpClient {
    private static HttpClient mHttpClient = null;

    private static final String BASE_URL = "http://app.cw2009.com/api/";

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
//        AddQueryParameterInterceptor addQueryParameterInterceptor = new AddQueryParameterInterceptor();
//        okHttpClientBuilder.addInterceptor(addQueryParameterInterceptor);
        //公共参数
        long time = new Date().getTime();
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                //.addParam("sn", getSn(time))
                .addParam("sn", "6d78616f74c40665264982dc634614bf714a2b24")
              //  .addParam("time", String.valueOf(time))
                .addParam("time", "123456")
                .build();
        okHttpClientBuilder.addInterceptor(basicParamsInterceptor);

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
                .addConverterFactory(MyGsonConverterFactory.create())
                .client(okHttpClient);
        return builder;
    }

    public static class BasicParamsInterceptor implements Interceptor {

        Map<String, String> queryParamsMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headerParamsMap = new HashMap<>();
        List<String> headerLinesList = new ArrayList<>();

        private BasicParamsInterceptor() {

        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder();

            // process header params inject
            Headers.Builder headerBuilder = request.headers().newBuilder();
            if (headerParamsMap.size() > 0) {
                Iterator iterator = headerParamsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
                }
            }

            if (headerLinesList.size() > 0) {
                for (String line: headerLinesList) {
                    headerBuilder.add(line);
                }
            }

            requestBuilder.headers(headerBuilder.build());
            // process header params end




            // process queryParams inject whatever it's GET or POST
            if (queryParamsMap.size() > 0) {
                injectParamsIntoUrl(request, requestBuilder, queryParamsMap);
            }
            // process header params end




            // process post body inject
            if (request.method().equals("POST") && request.body().contentType().subtype().equals("x-www-form-urlencoded")) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                if (paramsMap.size() > 0) {
                    Iterator iterator = paramsMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        formBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                RequestBody formBody = formBodyBuilder.build();
                String postBodyString = bodyToString(request.body());
                postBodyString += ((postBodyString.length() > 0) ? "&" : "") +  bodyToString(formBody);
                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
            } else {    // can't inject into body, then inject into url
                injectParamsIntoUrl(request, requestBuilder, paramsMap);
            }

            request = requestBuilder.build();
            return chain.proceed(request);
        }

        // func to inject params into url
        private void injectParamsIntoUrl(Request request, Request.Builder requestBuilder, Map<String, String> paramsMap) {
            HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
            if (paramsMap.size() > 0) {
                Iterator iterator = paramsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
                }
            }

            requestBuilder.url(httpUrlBuilder.build());
        }

        private static String bodyToString(final RequestBody request){
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                if(copy != null)
                    copy.writeTo(buffer);
                else
                    return "";
                return buffer.readUtf8();
            }
            catch (final IOException e) {
                return "did not work";
            }
        }

        public static class Builder {

            BasicParamsInterceptor interceptor;

            public Builder() {
                interceptor = new BasicParamsInterceptor();
            }

            public Builder addParam(String key, String value) {
                interceptor.paramsMap.put(key, value);
                return this;
            }

            public Builder addParamsMap(Map<String, String> paramsMap) {
                interceptor.paramsMap.putAll(paramsMap);
                return this;
            }

            public Builder addHeaderParam(String key, String value) {
                interceptor.headerParamsMap.put(key, value);
                return this;
            }

            public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
                interceptor.headerParamsMap.putAll(headerParamsMap);
                return this;
            }

            public Builder addHeaderLine(String headerLine) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.headerLinesList.add(headerLine);
                return this;
            }

            public Builder addHeaderLinesList(List<String> headerLinesList) {
                for (String headerLine: headerLinesList) {
                    int index = headerLine.indexOf(":");
                    if (index == -1) {
                        throw new IllegalArgumentException("Unexpected header: " + headerLine);
                    }
                    interceptor.headerLinesList.add(headerLine);
                }
                return this;
            }

            public Builder addQueryParam(String key, String value) {
                interceptor.queryParamsMap.put(key, value);
                return this;
            }

            public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
                interceptor.queryParamsMap.putAll(queryParamsMap);
                return this;
            }

            public BasicParamsInterceptor build() {
                return interceptor;
            }

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

    //sha1(md5('cw2009com'.'E350D68BCD46861FEEFB3EFEEAE9F936'.time()));
    public static String getSn(long time){
        String snStr = "cw2009com" + "E350D68BCD46861FEEFB3EFEEAE9F936" + time;
        String md5Sn = md5(snStr);
        return encryptToSHA(md5Sn);
    }

    public static String md5(String string) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = string.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String rs = byte2hex(digesta);
        return rs;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }
}
