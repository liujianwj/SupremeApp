package zs.com.supremeapp.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.UploadImageResultDO;
import zs.com.supremeapp.model.UploadVideoResultDO;
import zs.com.supremeapp.network.HttpClient;
import zs.com.supremeapp.network.INetWorkCallback;

/**
 * Created by liujian on 2018/9/23.
 */

public class UploadApi {

    private IUploadApi uploadApi;

    public UploadApi() {
        this.uploadApi = HttpClient.newInstance().buildRetrofit().create(IUploadApi.class);
    }

    public void uploadVideo(List<File> files, final INetWorkCallback<UploadVideoResultDO> callback){
        long time = new Date().getTime();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("video/mp4"), files.get(0));

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("video", files.get(0).getName(), requestFile);
        HashMap<String, RequestBody> map = new HashMap<>();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain"), Platform.getInstance().getUsrId());
        RequestBody requestBody1 = RequestBody.create(
                MediaType.parse("text/plain"), HttpClient.getSn(time));
        RequestBody requestBody2 = RequestBody.create(
                MediaType.parse("text/plain"), String.valueOf(time));
        map.put("userid", requestBody);
        map.put("sn", requestBody1);
        map.put("time", requestBody2);

        uploadApi.uploadVideo(map, filePart).enqueue(new Callback<UploadVideoResultDO>() {
            @Override
            public void onResponse(Call<UploadVideoResultDO> call, Response<UploadVideoResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<UploadVideoResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    /**
     *
     * @param files
     * @param callback
     */
    public void uploadImages(List<File> files, final INetWorkCallback<UploadImageResultDO> callback){
        List<MultipartBody.Part> parts = new ArrayList<>();
        for(File file : files){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("files[]", file.getName(), requestFile);
            parts.add(filePart);
        }

        HashMap<String, RequestBody> map = new HashMap<>();
        long time = new Date().getTime();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain"), Platform.getInstance().getUsrId());
        RequestBody requestBody1 = RequestBody.create(
                MediaType.parse("text/plain"), HttpClient.getSn(time));
        RequestBody requestBody2 = RequestBody.create(
                MediaType.parse("text/plain"), String.valueOf(time));
        map.put("userid", requestBody);
        map.put("sn", requestBody1);
        map.put("time", requestBody2);

        uploadApi.uploadImages(map, parts).enqueue(new Callback<UploadImageResultDO>() {
            @Override
            public void onResponse(Call<UploadImageResultDO> call, Response<UploadImageResultDO> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<UploadImageResultDO> call, Throwable t) {
                callback.failure(-1, t.getMessage());
            }
        });
    }

    public void uploadImage(List<File> files, final INetWorkCallback<ResponseBody> callback){
        long time = new Date().getTime();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), files.get(0));

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", files.get(0).getName(), requestFile);
        HashMap<String, RequestBody> map = new HashMap<>();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain"), Platform.getInstance().getUsrId());
        RequestBody requestBody1 = RequestBody.create(
                MediaType.parse("text/plain"), HttpClient.getSn(time));
        RequestBody requestBody2 = RequestBody.create(
                MediaType.parse("text/plain"), String.valueOf(time));
        map.put("userid", requestBody);
        map.put("sn", requestBody1);
        map.put("time", requestBody2);

        uploadApi.uploadImage(map, filePart).enqueue(new Callback<ResponseBody>() {
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



    public interface IUploadApi{

        @Multipart
        @POST("upload/video.api")
        Call<UploadVideoResultDO> uploadVideo(@PartMap() Map<String, RequestBody> partMap, @Part  MultipartBody.Part file);

        //multipart/form-data
        //image/png
        @Multipart
        @POST("upload/photos.api")
        Call<UploadImageResultDO> uploadImages(@PartMap() Map<String, RequestBody> partMap, @Part List<MultipartBody.Part> files);

        @Multipart
        @POST("upload/photo.api")
        Call<ResponseBody> uploadImage(@PartMap() Map<String, RequestBody> partMap,  @Part  MultipartBody.Part file);

    }

}
