package zs.com.supremeapp.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import zs.com.supremeapp.model.NewmsgsResultDO;
import zs.com.supremeapp.model.ZoneDetailResultDO;
import zs.com.supremeapp.model.ZoneResultDO;
import zs.com.supremeapp.network.HttpClient;
import zs.com.supremeapp.network.INetWorkCallback;

/**
 * Created by liujian on 2018/9/23.
 */

public class ZoneApi {

    private IZoneApi zoneApi;

    public ZoneApi() {
        this.zoneApi = HttpClient.newInstance().buildRetrofit().create(IZoneApi.class);
    }

    public void getZoneList(Map<String, String> params, final INetWorkCallback<ZoneResultDO> callback){

        zoneApi.getZoneList(params).enqueue(new Callback<ZoneResultDO>() {
            @Override
            public void onResponse(Call<ZoneResultDO> call, Response<ZoneResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<ZoneResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getZoneDetail(Map<String, String> params, final INetWorkCallback<ZoneDetailResultDO> callback){
        zoneApi.getZoneDetail(params).enqueue(new Callback<ZoneDetailResultDO>() {
            @Override
            public void onResponse(Call<ZoneDetailResultDO> call, Response<ZoneDetailResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<ZoneDetailResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void giveToZhan(Map<String, String> params, final INetWorkCallback<ResponseBody> callback){

        zoneApi.giveToZhan(params).enqueue(new Callback<ResponseBody>() {
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

    public void postComment(Map<String, String> params, final INetWorkCallback<ResponseBody> callback){

        zoneApi.postComment(params).enqueue(new Callback<ResponseBody>() {
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

    public void createZone(Map<String, String> params, final INetWorkCallback<ResponseBody> callback){
        zoneApi.createZone(params).enqueue(new Callback<ResponseBody>() {
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

    public void getNewmsgs(Map<String, String> params, final INetWorkCallback<NewmsgsResultDO> callback){
        zoneApi.getNewmsgs(params).enqueue(new Callback<NewmsgsResultDO>() {
            @Override
            public void onResponse(Call<NewmsgsResultDO> call, Response<NewmsgsResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<NewmsgsResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public interface IZoneApi{

        //获取朋友圈列表
        @FormUrlEncoded
        @POST("zone.api")
        Call<ZoneResultDO> getZoneList(@FieldMap Map<String, String> params);

        //获取指定id的朋友圈详情
        @FormUrlEncoded
        @POST("zone/getone.api")
        Call<ZoneDetailResultDO> getZoneDetail(@FieldMap Map<String, String> params);

        //获取未读评论信息及点赞数量及列表
        @FormUrlEncoded
        @POST("zone/newmsgs.api")
        Call<NewmsgsResultDO> getNewmsgs(@FieldMap Map<String, String> params);

        //上报新评论信息的阅读状态
        @FormUrlEncoded
        @POST("zone/newmsgs/haveread.api")
        Call<ResponseBody> haveRead(@FieldMap Map<String, String> params);

        //朋友圈点赞
        @FormUrlEncoded
        @POST("zone/givetozhan.api")
        Call<ResponseBody> giveToZhan(@FieldMap Map<String, String> params);

        //发表朋友圈
        @FormUrlEncoded
        @POST("zone/creat.api")
        Call<ResponseBody> createZone(@FieldMap Map<String, String> params);

        //朋友圈发表评论
        @FormUrlEncoded
        @POST("zone/postacomment.api")
        Call<ResponseBody> postComment(@FieldMap Map<String, String> params);
    }
}
