package zs.com.supremeapp.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import zs.com.supremeapp.model.CategoryResultDO;
import zs.com.supremeapp.model.DataDO;
import zs.com.supremeapp.model.DreamCommentResultDO;
import zs.com.supremeapp.model.DreamPayResultDO;
import zs.com.supremeapp.model.DreamsResultDO;
import zs.com.supremeapp.model.GetOneZanResultDO;
import zs.com.supremeapp.model.GetoneResultDO;
import zs.com.supremeapp.model.HaveZansResultDO;
import zs.com.supremeapp.model.RechargeDO;
import zs.com.supremeapp.model.RechargeResultDO;
import zs.com.supremeapp.model.TopicResultDO;
import zs.com.supremeapp.model.ZanPopStatusResultDO;
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

    public void getDreamBanner(Map<String, String> params, final INetWorkCallback<TopicResultDO> callback){

        dreamApi.getDreamBanner(params).enqueue(new Callback<TopicResultDO>() {
            @Override
            public void onResponse(Call<TopicResultDO> call, Response<TopicResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<TopicResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
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

    public void getMyDreams(Map<String, String> params, final INetWorkCallback<DreamsResultDO> callback){

        dreamApi.getMyDreams(params).enqueue(new Callback<DreamsResultDO>() {
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

    public void sendComment(Map<String, String> params, final INetWorkCallback<DataDO> callback){

        dreamApi.sendComment(params).enqueue(new Callback<DataDO>() {
            @Override
            public void onResponse(Call<DataDO> call, Response<DataDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DataDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void createDream(Map<String, String> params, final INetWorkCallback<DataDO> callback){

        dreamApi.createDream(params).enqueue(new Callback<DataDO>() {
            @Override
            public void onResponse(Call<DataDO> call, Response<DataDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DataDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getComments(Map<String, String> params, final INetWorkCallback<DreamCommentResultDO> callback){

        dreamApi.getComments(params).enqueue(new Callback<DreamCommentResultDO>() {
            @Override
            public void onResponse(Call<DreamCommentResultDO> call, Response<DreamCommentResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DreamCommentResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void giveToZan(Map<String, String> params, final INetWorkCallback<DataDO> callback){

        dreamApi.giveToZan(params).enqueue(new Callback<DataDO>() {
            @Override
            public void onResponse(Call<DataDO> call, Response<DataDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DataDO> call, Throwable t) {
                int errorCode = -1;
                try{
                    if(t.getCause() != null){
                        errorCode = Integer.parseInt(t.getCause().getMessage());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                callback.failure(errorCode, t.getMessage());
            }
        });
    }

    public void getZanPopStatus(Map<String, String> params, final INetWorkCallback<ZanPopStatusResultDO> callback){

        dreamApi.getZanPopStatus(params).enqueue(new Callback<ZanPopStatusResultDO>() {
            @Override
            public void onResponse(Call<ZanPopStatusResultDO> call, Response<ZanPopStatusResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<ZanPopStatusResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getFreeZan(Map<String, String> params, final INetWorkCallback<DataDO> callback){

        dreamApi.getFreeZan(params).enqueue(new Callback<DataDO>() {
            @Override
            public void onResponse(Call<DataDO> call, Response<DataDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DataDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void haveZans(Map<String, String> params, final INetWorkCallback<HaveZansResultDO> callback){

        dreamApi.haveZans(params).enqueue(new Callback<HaveZansResultDO>() {
            @Override
            public void onResponse(Call<HaveZansResultDO> call, Response<HaveZansResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<HaveZansResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void getOneZan(Map<String, String> params, final INetWorkCallback<GetOneZanResultDO> callback){

        dreamApi.getOneZan(params).enqueue(new Callback<GetOneZanResultDO>() {
            @Override
            public void onResponse(Call<GetOneZanResultDO> call, Response<GetOneZanResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<GetOneZanResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void recharge(Map<String, String> params, final INetWorkCallback<RechargeResultDO> callback){
        dreamApi.recharge(params).enqueue(new Callback<RechargeResultDO>() {
            @Override
            public void onResponse(Call<RechargeResultDO> call, Response<RechargeResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<RechargeResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void dreamWXPay(Map<String, String> params, final INetWorkCallback<DreamPayResultDO> callback){

        dreamApi.dreamWXPay(params).enqueue(new Callback<DreamPayResultDO>() {
            @Override
            public void onResponse(Call<DreamPayResultDO> call, Response<DreamPayResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<DreamPayResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }


    public interface IDreamApi{

        @FormUrlEncoded
        @POST("dreams/topic.api")
        Call<TopicResultDO> getDreamBanner(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dream/cate.api")
        Call<CategoryResultDO> getDreamCate(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dreams.api")
        Call<DreamsResultDO> getDreams(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("my_dreams.api")
        Call<DreamsResultDO> getMyDreams(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dreams/getone.api")
        Call<GetoneResultDO> getDreamDetail(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dreams/giveto/comment.api")
        Call<DataDO> sendComment(@FieldMap Map<String, String> params);

        //发布Dream
        @FormUrlEncoded
        @POST("dreams/creat.api")
        Call<DataDO> createDream(@FieldMap Map<String, String> params);

        //获取dream评论
        @FormUrlEncoded
        @POST("dreams/getone/comments.api")
        Call<DreamCommentResultDO> getComments(@FieldMap Map<String, String> params);

        //dream点赞
        @FormUrlEncoded
        @POST("dreams/giveto/zhan.api")
        Call<DataDO> giveToZan(@FieldMap Map<String, String> params);

        //获取领取点赞数弹框状态
        @FormUrlEncoded
        @POST("user/get/zhan/pop.api")
        Call<ZanPopStatusResultDO> getZanPopStatus(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("user/getfreezhan.api")
        Call<DataDO> getFreeZan(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("getuser/havezhans.api")
        Call<HaveZansResultDO> haveZans(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dreams/getone/zhan.api")
        Call<GetOneZanResultDO> getOneZan(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("dream/recharge.api")
        Call<RechargeResultDO> recharge(@FieldMap Map<String, String> params);

        @FormUrlEncoded
        @POST("pay/wx/dream_pay.api")
        Call<DreamPayResultDO> dreamWXPay(@FieldMap Map<String, String> params);
    }
}
