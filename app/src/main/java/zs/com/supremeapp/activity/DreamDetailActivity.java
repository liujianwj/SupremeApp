package zs.com.supremeapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.model.GetoneResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
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
    @BindView(R.id.dreamTitleTv)
    TextView dreamTitleTv;
    @BindView(R.id.dreamContentTv)
    TextView dreamContentTv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.commentLayout)
    LinearLayout commentLayout;

    private String dreamId;
    private EditText commentEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_dream_detail);
        super.onCreate(savedInstanceState);

        backLayout.setOnClickListener(this);
        needTooTv.setOnClickListener(this);
        zanLayout.setOnClickListener(this);
        commentLayout.setOnClickListener(this);

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
                showProcessDialog(false);
                if(getoneResultDO != null && getoneResultDO.getDetail() != null){
                    DreamDO dreamDO = getoneResultDO.getDetail();
                    initView(dreamDO);
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
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(DreamDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(DreamDO dreamDO){
        dreamTitleTv.setText(dreamDO.getDream_title());
        dreamContentTv.setText(dreamDO.getDream_content());
        userNameTv.setText(dreamDO.getUser_name());
    }

    @Override
    public void onClick(View view) {
        if(R.id.backLayout == view.getId()){
            finish();
        }else if(R.id.needTooTv == view.getId()){
            startActivity(new Intent(this, DreamPublishActivity.class));
        }else if(R.id.zanLayout == view.getId()){
            ZanDialog zanDialog = new ZanDialog.Builder(this).create();
            zanDialog.show();
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
        }
    }
}
