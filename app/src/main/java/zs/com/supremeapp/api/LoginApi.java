package zs.com.supremeapp.api;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import zs.com.supremeapp.model.LoginResultDO;
import zs.com.supremeapp.model.UserResultDO;
import zs.com.supremeapp.network.HttpClient;
import zs.com.supremeapp.network.INetWorkCallback;

/**
 * Created by liujian on 2018/9/3.
 */

public class LoginApi {

    private ILoginApi loginApi;

    public LoginApi() {
        this.loginApi = HttpClient.newInstance().buildRetrofit().create(ILoginApi.class);
    }

    public void getMessageCode(Map<String, String> params, final INetWorkCallback<ResponseBody> callback){
        loginApi.getMessageCode(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void login(Map<String, String> params, final INetWorkCallback<LoginResultDO> callback){
        loginApi.login(params).enqueue(new Callback<LoginResultDO>() {
            @Override
            public void onResponse(Call<LoginResultDO> call, Response<LoginResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<LoginResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getUser(Map<String, String> params, final INetWorkCallback<UserResultDO> callback){
        loginApi.getuser(params).enqueue(new Callback<UserResultDO>() {
            @Override
            public void onResponse(Call<UserResultDO> call, Response<UserResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<UserResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public interface ILoginApi{

        @FormUrlEncoded
        @POST("getcode.api")
        Call<ResponseBody> getMessageCode(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("login.api")
        Call<LoginResultDO> login(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("getuser.api")
        Call<UserResultDO> getuser(@FieldMap Map<String, String> params);
    }
}
