package zs.com.supremeapp.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import zs.com.supremeapp.model.CategoryResultDO;
import zs.com.supremeapp.model.DreamsResultDO;
import zs.com.supremeapp.model.GetoneResultDO;
import zs.com.supremeapp.network.HttpClient;
import zs.com.supremeapp.network.INetWorkCallback;

/**
 * Created by liujian on 2018/9/6.
 */

public class DreamApi {

    private IDreamApi dreamApi;

    public DreamApi() {
        this.dreamApi = HttpClient.newInstance().buildRetrofit().create(IDreamApi.class);
    }

    public void getDreamCate(Map<String, String> params, final INetWorkCallback<CategoryResultDO> callback){

        dreamApi.getDreamCate(params).enqueue(new Callback<CategoryResultDO>() {
            @Override
            public void onResponse(Call<CategoryResultDO> call, Response<CategoryResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<CategoryResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getDreams(Map<String, String> params, final INetWorkCallback<DreamsResultDO> callback){

        dreamApi.getDreams(params).enqueue(new Callback<DreamsResultDO>() {
            @Override
            public void onResponse(Call<DreamsResultDO> call, Response<DreamsResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DreamsResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getDreamDetail(Map<String, String> params, final INetWorkCallback<GetoneResultDO> callback){

        dreamApi.getDreamDetail(params).enqueue(new Callback<GetoneResultDO>() {
            @Override
            public void onResponse(Call<GetoneResultDO> call, Response<GetoneResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<GetoneResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });

    }

    public void sendComment(Map<String, String> params, final INetWorkCallback<ResponseBody> callback){

        dreamApi.sendComment(params).enqueue(new Callback<ResponseBody>() {
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


    public interface IDreamApi{

        @FormUrlEncoded
        @POST("dream/cate.api")
        Call<CategoryResultDO> getDreamCate(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dreams.api")
        Call<DreamsResultDO> getDreams(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dreams/getone.api")
        Call<GetoneResultDO> getDreamDetail(@FieldMap Map<String, String> params);


        @FormUrlEncoded
        @POST("dreams/giveto/comment.api")
        Call<ResponseBody> sendComment(@FieldMap Map<String, String> params);
    }
}
