package zs.com.supremeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.GridImageAdapter;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.api.UploadApi;
import zs.com.supremeapp.manager.FullyGridLayoutManager;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.UploadImageDO;
import zs.com.supremeapp.model.UploadImageResultDO;
import zs.com.supremeapp.model.UploadVideoResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.DataUtils;

/**
 * dream发布
 * Created by liujian on 2018/8/7.
 */

public class DreamPublishActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.backLayout)
    LinearLayout backLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.publishTv)
    TextView publishTv;
    @BindView(R.id.dreamTitleEt)
    EditText dreamTitleEt;
    @BindView(R.id.moneyEt)
    EditText moneyEt;
    @BindView(R.id.contentEt)
    EditText contentEt;

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<UploadImageDO> uploadImageDOS = new ArrayList<>();
    private String videoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_dream_publish);
        super.onCreate(savedInstanceState);

        backLayout.setOnClickListener(this);
        publishTv.setOnClickListener(this);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(DreamPublishActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(DreamPublishActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        adapter.setOnImageDelListener(new GridImageAdapter.OnImageDelListener() {
            @Override
            public void onImageDel(int position) {
                if(position < uploadImageDOS.size()){
                    uploadImageDOS.remove(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(DreamPublishActivity.this)
                    .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(9)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(true) // 裁剪是否可旋转图片
                    //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    if(!DataUtils.isListEmpty(selectList)){
                        boolean isVideo = PictureMimeType.isVideo(selectList.get(0).getPictureType());
                        if(isVideo){
                            uploadImageDOS.clear();
                            Log.i("视频-----》", selectList.get(0).getPath());
                            uploadVideo(new File(selectList.get(0).getPath()));
                        }else {
                            videoPath = null;
                            List<File> files = new ArrayList<>(selectList.size());
                            for (LocalMedia media : selectList) {
                                Log.i("图片-----》", media.getPath());
                                files.add(new File(media.getPath()));
                            }
                            uploadImages(files);
                        }
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void uploadVideo(File file){
        List<File> files = new ArrayList<>(1);
        files.add(file);
        showProcessDialog(true);
        new UploadApi().uploadVideo(files, new INetWorkCallback<UploadVideoResultDO>() {
            @Override
            public void success(UploadVideoResultDO uploadVideoResultDO, Object... objects) {
                showProcessDialog(false);
                if(uploadVideoResultDO != null){
                    videoPath = uploadVideoResultDO.getUri();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamPublishActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImages(List<File> files){
        showProcessDialog(true);
        new UploadApi().uploadImages(files, new INetWorkCallback<UploadImageResultDO>() {
            @Override
            public void success(UploadImageResultDO responseBody, Object... objects) {
                showProcessDialog(false);
                if(responseBody != null && !DataUtils.isListEmpty(responseBody.getF())){
                    uploadImageDOS = responseBody.getF();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamPublishActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (R.id.backLayout == viewId) {
            finish();
        }else if(R.id.publishTv == viewId){
            if(checkData()){
                publishDream();
            }
        }
    }

    private boolean checkData(){
        if(TextUtils.isEmpty(dreamTitleEt.getText().toString())){
            Toast.makeText(this, getString(R.string.dream_title_input_tip), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(contentEt.getText().toString())){
            Toast.makeText(this, getString(R.string.dream_content_input_tip), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(moneyEt.getText().toString())){
            Toast.makeText(this, getString(R.string.dream_money_input_tip), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void publishDream(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("title", DataUtils.nullToEmpty(dreamTitleEt.getText().toString()));
        params.put("content", DataUtils.nullToEmpty(contentEt.getText().toString()));
        if(!DataUtils.isListEmpty(uploadImageDOS)){
            List<String> pics = new ArrayList<>();
            for(UploadImageDO uploadImageDO : uploadImageDOS){
                pics.add(uploadImageDO.getSource_url());
            }
            params.put("pics", new Gson().toJson(pics));
        }
        params.put("money", DataUtils.nullToEmpty(moneyEt.getText().toString()));
        if(!TextUtils.isEmpty(videoPath)){
            params.put("video", videoPath);
        }
        showProcessDialog(true);
        new DreamApi().createDream(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamPublishActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
