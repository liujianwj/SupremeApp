package zs.com.supremeapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.DreamDetailCommentAdapter;
import zs.com.supremeapp.adapter.SupportRecyclerAdapter;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.DreamCommentDO;
import zs.com.supremeapp.model.DreamCommentResultDO;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.model.GetoneResultDO;
import zs.com.supremeapp.model.HaveZansResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.observer.ObserverKey;
import zs.com.supremeapp.observer.SupplySubject;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DateUtils;
import zs.com.supremeapp.widget.CompletedView;
import zs.com.supremeapp.widget.ZanDialog;

/**
 * dream详情页面
 * Created by liujian on 2018/8/5.
 */

public class DreamDetailActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.backLayout)
    LinearLayout backLayout;
    @BindView(R.id.needTooTv)
    TextView needTooTv;
    @BindView(R.id.zanLayout)
    LinearLayout zanLayout;
    @BindView(R.id.listView)
    ListView listView;

    private TextView dreamContentTv;
    private TextView userNameTv;
    private LinearLayout commentLayout;
    private SimpleDraweeView videoIv;
    private SimpleDraweeView headImg;
    private TextView dreamTitleTv;
    private CompletedView completedView;
    private TextView processTv;
    private TextView zanTv;
    private TextView zanTargetTv;
    private TextView endDayTv;
    private RecyclerView supportRecyclerView;

    private String dreamId;
    private EditText commentEt;
    private MyHandler myHandler = new MyHandler(this);

    private DreamDetailCommentAdapter dreamDetailCommentAdapter;
    private List<DreamCommentDO> commentDOList;
    private SupportRecyclerAdapter supportRecyclerAdapter;
    private DreamDO dreamDO;;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_dream_detail);
        super.onCreate(savedInstanceState);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_dream_detail_header, null);
        dreamContentTv = headerView.findViewById(R.id.dreamContentTv);
        userNameTv = headerView.findViewById(R.id.userNameTv);
        commentLayout = headerView.findViewById(R.id.commentLayout);
        videoIv = headerView.findViewById(R.id.videoIv);
        headImg = headerView.findViewById(R.id.headImg);
        dreamTitleTv = headerView.findViewById(R.id.dreamTitleTv);
        completedView = headerView.findViewById(R.id.completedView);
        processTv = headerView.findViewById(R.id.processTv);
        zanTv = headerView.findViewById(R.id.zanTv);
        zanTargetTv = headerView.findViewById(R.id.zanTargetTv);
        endDayTv = headerView.findViewById(R.id.endDayTv);
        supportRecyclerView = headerView.findViewById(R.id.supportRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        supportRecyclerView.setLayoutManager(linearLayoutManager);
        supportRecyclerAdapter = new SupportRecyclerAdapter(this);
        supportRecyclerView.setAdapter(supportRecyclerAdapter);
        listView.addHeaderView(headerView);

        backLayout.setOnClickListener(this);
        needTooTv.setOnClickListener(this);
        zanLayout.setOnClickListener(this);
        commentLayout.setOnClickListener(this);

        commentDOList = new ArrayList<>();
        dreamDetailCommentAdapter = new DreamDetailCommentAdapter(this, commentDOList);
        listView.setAdapter(dreamDetailCommentAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            dreamId = bundle.getString("dreamId");
            getDreamDetail();
        }
    }



    private void getDreamDetail(){
        Map<String, String> params = new HashMap<>();
        params.put("dreamid", dreamId);
        showProcessDialog(true);
        new DreamApi().getDreamDetail(params, new INetWorkCallback<GetoneResultDO>() {
            @Override
            public void success(GetoneResultDO getoneResultDO, Object... objects) {
                if(getoneResultDO != null && getoneResultDO.getDetail() != null){
                    dreamDO = getoneResultDO.getDetail();
                    initView(dreamDO);
                    getComments(false);
                }else {
                    showProcessDialog(false);
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getComments(boolean isShowProcessDialog){
        Map<String, String> params = new HashMap<>();
        params.put("dreamid", dreamId);
        if(isShowProcessDialog){
            showProcessDialog(true);
        }
        new DreamApi().getComments(params, new INetWorkCallback<DreamCommentResultDO>() {
            @Override
            public void success(DreamCommentResultDO dreamCommentResultDO, Object... objects) {
                showProcessDialog(false);
                if(dreamCommentResultDO != null && !DataUtils.isListEmpty(dreamCommentResultDO.getList())){
                    commentDOList.clear();
                    commentDOList.addAll(dreamCommentResultDO.getList());
                    dreamDetailCommentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //发表评论
    private void sendComment(String commentStr){
        Map<String, String> params = new HashMap<>();
        params.put("dreamid", dreamId);
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("comment", commentStr);
        showProcessDialog(true);
        new DreamApi().sendComment(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                getComments(true);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //点赞
    private void giveToZan(int zanNum){
        Map<String, String> params = new HashMap<>();
        params.put("dreamid", dreamId);
        params.put("zhan", String.valueOf(zanNum));
        params.put("userid", Platform.getInstance().getUsrId());
        showProcessDialog(true);
        new DreamApi().giveToZan(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                getDreamDetail();
                SupplySubject.getInstance().change(null, ObserverKey.DREA_FRAGMENT_UPDATE);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void haveZans(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        showProcessDialog(true);
        new DreamApi().haveZans(params, new INetWorkCallback<HaveZansResultDO>() {
            @Override
            public void success(HaveZansResultDO haveZansResultDO, Object... objects) {
                showProcessDialog(false);
                if(haveZansResultDO != null && haveZansResultDO.getMember_zhan() != null){
                    if(dreamDO == null){
                        return;
                    }
                    ZanDialog zanDialog = new ZanDialog.Builder(DreamDetailActivity.this)
                            .setHeadImgUri(dreamDO.getUser_avatar())
                            .setHavaZanNum(haveZansResultDO.getMember_zhan().getZhans())
                            .setOnClickListener(DreamDetailActivity.this).create();
                    zanDialog.show();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap bitmap = null;

    private void loadVideo(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();

                try {
                    //这里要用FileProvider获取的Uri
                    if (url.contains("http")) {
                        retriever.setDataSource(url, new HashMap<String, String>());
                    } else {
                        retriever.setDataSource(url);
                    }
                    bitmap = retriever.getFrameAtTime();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException ex) {
                        ex.printStackTrace();
                    }
                }
                myHandler.sendEmptyMessage(1);
              //  showImageMessage(bitmap, positionTag, vv);
            }
        }).start();
    }

    private void initView(DreamDO dreamDO){
        dreamTitleTv.setText(dreamDO.getDream_title());
        dreamContentTv.setText(dreamDO.getDream_content());
        userNameTv.setText(dreamDO.getUser_name());
        headImg.setImageURI(dreamDO.getUser_avatar());
        zanTv.setText(getResources().getString(R.string.zan_num, dreamDO.getDream_zhan()));
        zanTargetTv.setText(getResources().getString(R.string.zan_target_num, dreamDO.getDream_target_zhan()));
        endDayTv.setText(getResources().getString(R.string.end_day, DateUtils.toDate(dreamDO.getDream_endday(), DateUtils.DATE_FORMAT0)));
        if(!TextUtils.isEmpty(dreamDO.getDream_status())){
            completedView.setProgress(Integer.valueOf(dreamDO.getDream_status()));
            processTv.setText(Integer.valueOf(dreamDO.getDream_status())/100 + "%");
        }
    }

    @Override
    public void onClick(View view) {
        if(R.id.backLayout == view.getId()){
            finish();
        }else if(R.id.needTooTv == view.getId()){
            startActivity(new Intent(this, DreamPublishActivity.class));
        }else if(R.id.zanLayout == view.getId()){
            haveZans();
        }else if(R.id.commentLayout == view.getId()){ //评论
            final Dialog dialog = new Dialog(DreamDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.dialog_comment);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            dialog.findViewById(R.id.dialogScrollView).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    dialog.dismiss();
                    return false;
                }
            });
            commentEt = dialog.findViewById(R.id.commentEt);
            dialog.findViewById(R.id.sendTv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(commentEt.getText().toString())){
                        sendComment(commentEt.getText().toString());
                    }
                    dialog.dismiss();
                }
            });
        }else if(R.id.zanTv == view.getId()){
            int num = (Integer) view.getTag();
            giveToZan(num);
        }
    }

    public void showVideoImage(){
        if(bitmap != null){
            videoIv.setImageBitmap(bitmap);
        }
    }

    static class MyHandler extends Handler{
        WeakReference<DreamDetailActivity> mWeakReference;
        public MyHandler(DreamDetailActivity activity) {
            mWeakReference=new WeakReference<DreamDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final DreamDetailActivity activity = mWeakReference.get();
            if(activity!=null) {
                if (msg.what == 1) {
                   // noteBookAdapter.notifyDataSetChanged();
                    activity.showVideoImage();
                }
            }
        }
    }
}
