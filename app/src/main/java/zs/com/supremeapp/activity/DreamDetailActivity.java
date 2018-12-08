package zs.com.supremeapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.DreamDetailCommentAdapter;
import zs.com.supremeapp.adapter.DreamImageRecycleAdapter;
import zs.com.supremeapp.adapter.SupportRecyclerAdapter;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.DataDO;
import zs.com.supremeapp.model.DreamCommentDO;
import zs.com.supremeapp.model.DreamCommentResultDO;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.model.GetOneZanResultDO;
import zs.com.supremeapp.model.GetoneResultDO;
import zs.com.supremeapp.model.HaveZansResultDO;
import zs.com.supremeapp.model.RechargeDO;
import zs.com.supremeapp.model.RechargeResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.observer.ObserverKey;
import zs.com.supremeapp.observer.SupplySubject;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DensityUtils;
import zs.com.supremeapp.utils.UMHelpUtils;
import zs.com.supremeapp.widget.CompletedView;
import zs.com.supremeapp.widget.PayForZanDialog;
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
    @BindView(R.id.activeRuleTv)
    View activeRuleTv;
    @BindView(R.id.reportIv)
    View reportIv;
    @BindView(R.id.shareImg)
    View shareImg;

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
    private TextView userCompanyTv;
    private ImageView callIMIv;
    private View playImage;
    private RecyclerView recycleView;
    private View videoLayout;
    private View userLayout;

    private String dreamId;
    private EditText commentEt;

    private DreamDetailCommentAdapter dreamDetailCommentAdapter;
    private List<DreamCommentDO> commentDOList;
    private SupportRecyclerAdapter supportRecyclerAdapter;
    private List<GetOneZanResultDO.SupportDO> supportDOList;
    private DreamDO dreamDO;

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
        userCompanyTv = headerView.findViewById(R.id.userCompanyTv);
        callIMIv = headerView.findViewById(R.id.callIMIv);
        playImage = headerView.findViewById(R.id.playImage);
        recycleView = headerView.findViewById(R.id.recycle_view);
        videoLayout = headerView.findViewById(R.id.videoLayout);
        userLayout = headerView.findViewById(R.id.userLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleView.setLayoutManager(linearLayoutManager);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        supportRecyclerView.setLayoutManager(linearLayoutManager);

        supportDOList = new ArrayList<>();
        supportRecyclerAdapter = new SupportRecyclerAdapter(this, supportDOList);
        supportRecyclerView.setAdapter(supportRecyclerAdapter);
        listView.addHeaderView(headerView);

        backLayout.setOnClickListener(this);
        needTooTv.setOnClickListener(this);
        zanLayout.setOnClickListener(this);
        commentLayout.setOnClickListener(this);
        activeRuleTv.setOnClickListener(this);
        reportIv.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        callIMIv.setOnClickListener(this);

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
                if(dreamCommentResultDO != null && !DataUtils.isListEmpty(dreamCommentResultDO.getList())){
                    commentDOList.clear();
                    commentDOList.addAll(dreamCommentResultDO.getList());
                    dreamDetailCommentAdapter.notifyDataSetChanged();
                    getOneZan(false);
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

    private void getOneZan(boolean isShowProcessDialog){
        Map<String, String> params = new HashMap<>();
        params.put("dreamid", dreamId);
        if(isShowProcessDialog){
            showProcessDialog(true);
        }
        new DreamApi().getOneZan(params, new INetWorkCallback<GetOneZanResultDO>() {
            @Override
            public void success(GetOneZanResultDO getOneZanResultDO, Object... objects) {
                showProcessDialog(false);
                if(getOneZanResultDO != null && !DataUtils.isListEmpty(getOneZanResultDO.getList())){
                    supportDOList.clear();
                    supportDOList.addAll(getOneZanResultDO.getList());
                    supportRecyclerAdapter.notifyDataSetChanged();
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
        new DreamApi().sendComment(params, new INetWorkCallback<DataDO>() {
            @Override
            public void success(DataDO responseBody, Object... objects) {
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
        new DreamApi().giveToZan(params, new INetWorkCallback<DataDO>() {
            @Override
            public void success(DataDO responseBody, Object... objects) {
                showProcessDialog(false);
                getDreamDetail();
                SupplySubject.getInstance().change(null, ObserverKey.DREA_FRAGMENT_UPDATE);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                if(errorCode == 300){
                    PayForZanDialog payForZanDialog =  new PayForZanDialog.Builder(DreamDetailActivity.this)
                            .setOnClickListener(DreamDetailActivity.this)
                            .create();
                    payForZanDialog.show();
                }else {
                    Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                }
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
                    if(haveZansResultDO.getMember_zhan().getZhans() <= 0){
                        PayForZanDialog payForZanDialog =  new PayForZanDialog.Builder(DreamDetailActivity.this)
                                .setOnClickListener(DreamDetailActivity.this)
                                .create();
                        payForZanDialog.show();
                    }else {
                        ZanDialog zanDialog = new ZanDialog.Builder(DreamDetailActivity.this)
                                .setHeadImgUri(dreamDO.getUser_avatar())
                                .setHavaZanNum(haveZansResultDO.getMember_zhan().getZhans())
                                .setOnGetMoreZanListener(new ZanDialog.OnGetMoreZanListener() {
                                    @Override
                                    public void onGetMoreZan() {
//                                        Intent intent = new Intent(DreamDetailActivity.this, WebActivity.class);
//                                        intent.putExtra("url", "http://app.cw2009.com/h5/how_to_get_zhan.html");
//                                        DreamDetailActivity.this.startActivity(intent);
                                        //go pay
                                        PayForZanDialog payForZanDialog =  new PayForZanDialog.Builder(DreamDetailActivity.this)
                                                .setOnClickListener(DreamDetailActivity.this)
                                                .create();
                                        payForZanDialog.show();
                                    }
                                })
                                .setOnClickListener(DreamDetailActivity.this).create();
                        zanDialog.show();
                    }
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(final DreamDO dreamDO){
        if(TextUtils.equals(dreamDO.getDream_status(), "1")){
            userLayout.setVisibility(View.VISIBLE);
        }else {
            userLayout.setVisibility(View.INVISIBLE);
        }
        dreamTitleTv.setText(dreamDO.getDream_title());
        dreamContentTv.setText(dreamDO.getDream_content());
        userNameTv.setText(dreamDO.getUser_name());
        headImg.setImageURI(dreamDO.getUser_avatar());
        userCompanyTv.setText(dreamDO.getUser_company());
        zanTv.setText(getResources().getString(R.string.zan_num, dreamDO.getDream_zhan()));
        zanTargetTv.setText(getResources().getString(R.string.zan_target_num, dreamDO.getDream_target_zhan()));
        if(!TextUtils.isEmpty(dreamDO.getDream_endday())){
           // endDayTv.setText(getResources().getString(R.string.end_day, DateUtils.toDate(Long.parseLong(dreamDO.getDream_endday()), DateUtils.DATE_FORMAT0)));
            endDayTv.setText(getResources().getString(R.string.end_day, dreamDO.getDream_endday()));
        }

        if(!TextUtils.isEmpty(dreamDO.getDream_video())){
            videoLayout.setVisibility(View.VISIBLE);
            recycleView.setVisibility(View.GONE);
            int w = (DensityUtils.getScreenWidth() - DensityUtils.dip2px(50)) / 3;

            videoIv.setImageBitmap(DataUtils.createVideoThumbnail(dreamDO.getDream_video(), w, DensityUtils.dip2px(80)));
            videoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DreamDetailActivity.this, VideoViewActivity.class);
                    intent.putExtra("videoUrl", dreamDO.getDream_video());
                    DreamDetailActivity.this.startActivity(intent);
                }
            });
        }else {
            videoLayout.setVisibility(View.GONE);
            recycleView.setVisibility(View.VISIBLE);
            if(!DataUtils.isListEmpty(dreamDO.getDream_pics())){
                DreamImageRecycleAdapter dreamImageRecycleAdapter = new DreamImageRecycleAdapter(this, dreamDO.getDream_pics());
                dreamImageRecycleAdapter.setOnRecycleItemClickListener(new DreamImageRecycleAdapter.OnRecycleItemClickListener() {
                    @Override
                    public void onRecycleItemClick(View view, int position) {
                        Intent intent = new Intent(DreamDetailActivity.this, ImageCheckActivity.class);
                        intent.putStringArrayListExtra("pics", dreamDO.getDream_pics());
                        intent.putExtra("position", position);
                        DreamDetailActivity.this.startActivity(intent);
                    }
                });
                recycleView.setAdapter(dreamImageRecycleAdapter);
            }
        }
        int process = 0;
        try{
            int zanNum = Integer.valueOf(dreamDO.getDream_zhan());
            int zanTargetNum = Integer.valueOf(dreamDO.getDream_target_zhan());
            if(zanTargetNum != 0){
                process = zanNum * 100 /zanTargetNum;
                process = process > 100 ? 100 : process;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        completedView.setProgress(process);
        processTv.setText(process + "%");
    }

    @Override
    public void onClick(View view) {
        if(R.id.backLayout == view.getId()){
            finish();
        }else if(R.id.needTooTv == view.getId()){
            Intent intent = new Intent(this, DreamPublishActivity.class);
            intent.putExtra("dreamDO", dreamDO);
            startActivity(intent);
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
        }else if(R.id.activeRuleTv == view.getId()){
            Intent intent = new Intent(DreamDetailActivity.this, WebActivity.class);
            intent.putExtra("url", "http://app.cw2009.com/h5/deram/rule.html");
            DreamDetailActivity.this.startActivity(intent);
        }else if(R.id.reportIv == view.getId()){
            Intent intent = new Intent(DreamDetailActivity.this, WebActivity.class);
            intent.putExtra("url", "http://app.cw2009.com/h5/deram/report.html");
            DreamDetailActivity.this.startActivity(intent);
        }else if(R.id.shareImg == view.getId()){
            if(dreamDO != null){
                UMHelpUtils.shareWebToWX(this, "http://app.cw2009.com/app/download.html",
                        dreamDO.getDream_title(), getResources().getString(R.string.dream_share_content), DataUtils.isListEmpty(dreamDO.getDream_pics()) ? null : dreamDO.getDream_pics().get(0));
            }
        }else if(R.id.callIMIv == view.getId()){
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, dreamDO.getUser_id(), dreamDO.getUser_name());
        }else if(R.id.payTv == view.getId()){ //  支付
            Object object = view.getTag();
            if(object instanceof Integer){
                int payNum = (Integer) object;
                createOrder(payNum);
            }
        }
    }

    private void createOrder(final int payNum){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("dreamid", dreamDO.getDream_id());
        params.put("zhan", payNum+"");
        showProcessDialog(true);
        new DreamApi().recharge(params, new INetWorkCallback<RechargeResultDO>() {
            @Override
            public void success(RechargeResultDO rechargeDO, Object... objects) {
                showProcessDialog(false);
                if(rechargeDO !=null && rechargeDO.getWeixin_pay() != null){
                    Intent intent = new Intent(DreamDetailActivity.this, PayActivity.class);
                    intent.putExtra("total", rechargeDO.getWeixin_pay().getTotal_fee());
                    intent.putExtra("orderId", rechargeDO.getWeixin_pay().getDream_order_id());
                    DreamDetailActivity.this.startActivity(intent);
                }

            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
