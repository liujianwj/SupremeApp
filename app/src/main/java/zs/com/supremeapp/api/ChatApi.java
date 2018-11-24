package zs.com.supremeapp.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import zs.com.supremeapp.model.ContrastResultDO;
import zs.com.supremeapp.model.DynamicsResultDO;
import zs.com.supremeapp.model.NearbyResultDO;
import zs.com.supremeapp.network.HttpClient;
import zs.com.supremeapp.network.INetWorkCallback;

/**
 * Created by liujian on 2018/10/9.
 */

public class ChatApi {

    private IChatApi chatApi;

    public ChatApi(){
        chatApi = HttpClient.newInstance().buildRetrofit().create(IChatApi.class);
    }

    public void getContrast(Map<String, String> params, final INetWorkCallback<ContrastResultDO> callback){
        chatApi.getContrast(params).enqueue(new Callback<ContrastResultDO>() {
            @Override
            public void onResponse(Call<ContrastResultDO> call, Response<ContrastResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<ContrastResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getNearby(Map<String, String> params, final INetWorkCallback<NearbyResultDO> callback){
        chatApi.getNearby(params).enqueue(new Callback<NearbyResultDO>() {
            @Override
            public void onResponse(Call<NearbyResultDO> call, Response<NearbyResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<NearbyResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getDynamics(Map<String, String> params, final INetWorkCallback<DynamicsResultDO> callback){
        chatApi.getDynamics(params).enqueue(new Callback<DynamicsResultDO>() {
            @Override
            public void onResponse(Call<DynamicsResultDO> call, Response<DynamicsResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DynamicsResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public interface IChatApi{

        @FormUrlEncoded
        @POST("ebook/contrast.api")
        Call<ContrastResultDO> getContrast(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("user/nearby.api")
        Call<NearbyResultDO> getNearby(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("im/getdynamics.api")
        Call<DynamicsResultDO> getDynamics(@FieldMap Map<String, String> params);

    }
}
