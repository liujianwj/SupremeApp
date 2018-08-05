package zs.com.supremeapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import zs.com.supremeapp.model.ContributorDO;

/**
 * 主页接口
 * Created by liujian on 2018/8/4.
 */

public class HomeApi{
    private  IHomeApi homeApi;

    public HomeApi() {
        this.homeApi = HttpClient.newInstance().buildRetrofit().create(IHomeApi.class);
    }

    public void getContributor(final INetWorkCallback<List<ContributorDO>> callback){
        homeApi.getContributor().enqueue(new Callback<List<ContributorDO>>() {
            @Override
            public void onResponse(Call<List<ContributorDO>> call, Response<List<ContributorDO>> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<ContributorDO>> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public interface IHomeApi{
        @GET("repos/square/retrofit/contributors")
        Call<List<ContributorDO>> getContributor();
    }

}
