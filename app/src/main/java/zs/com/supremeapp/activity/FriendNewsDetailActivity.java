package zs.com.supremeapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.FriendCommentTextListAdapter;
import zs.com.supremeapp.adapter.FriendStatusImageGridAdapter;
import zs.com.supremeapp.api.ZoneApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.ZoneDO;
import zs.com.supremeapp.model.ZoneDetailResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DateUtils;
import zs.com.supremeapp.utils.DensityUtils;
import zs.com.supremeapp.widget.FriendCommentPopup;
import zs.com.supremeapp.widget.WidgetFlowLayout;

/**
 * Created by liujian on 2018/10/11.
 */

public class FriendNewsDetailActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.headImg)
    SimpleDraweeView headImg;
    @BindView(R.id.commentImg)
    ImageView commentImg;
    @BindView(R.id.zanLayout)
    WidgetFlowLayout zanLayout;
    @BindView(R.id.commentTextListView)
    ListView commentTextListView;
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.contentTv)
    TextView contentTv;
    @BindView(R.id.zanGroup)
    View zanGroup;
    @BindView(R.id.commentGroup)
    View commentGroup;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.sendTv)
    TextView sendTv;
    @BindView(R.id.commentEt)
    EditText commentEt;
    @BindView(R.id.singleImg)
    SimpleDraweeView singleImg;
    @BindView(R.id.imageGridView)
    GridView imageGridView;


    private FriendCommentPopup friendCommentPopup;
    private String zoneId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_friend_news_detail);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            zoneId = bundle.getString("zoneId");
            getZoneDetail();
        }
        sendTv.setOnClickListener(this);
    }

    private void getZoneDetail(){
        Map<String, String> params = new HashMap<>();
        params.put("zoneid", zoneId);
        showProcessDialog(true);
        new ZoneApi().getZoneDetail(params, new INetWorkCallback<ZoneDetailResultDO>() {
            @Override
            public void success(ZoneDetailResultDO zoneDetailResultDO, Object... objects) {
                showProcessDialog(false);
                initZoneDetail(zoneDetailResultDO.getGetone());
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendNewsDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initZoneDetail(ZoneDO item){
        headImg.setImageURI(item.getUser_avatar());
        nameTv.setText(item.getUser_name());
        if(!TextUtils.isEmpty(item.getContent())){
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText(item.getContent());
        }else {
            contentTv.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(item.getVideo())){
            singleImg.setVisibility(View.VISIBLE);
            imageGridView.setVisibility(View.GONE);
            singleImg.setImageURI(item.getVideo());
        }else {
            if(!DataUtils.isListEmpty(item.getAlbum())) {
                singleImg.setVisibility(View.GONE);
                imageGridView.setVisibility(View.VISIBLE);
                FriendStatusImageGridAdapter friendStatusImageGridAdapter = new FriendStatusImageGridAdapter(this, item.getAlbum());
                imageGridView.setAdapter(friendStatusImageGridAdapter);
            }else {
                singleImg.setVisibility(View.GONE);
                imageGridView.setVisibility(View.GONE);
            }
        }

        commentImg.setOnClickListener(this);

        timeTv.setText(DateUtils.getTimeRange(item.getCreatTime()));


        if(!DataUtils.isListEmpty(item.getZhans())){
            zanGroup.setVisibility(View.VISIBLE);
            zanLayout.removeAllViews();
            for (int i = 0; i < item.getZhans().size(); i++) {
                TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.view_friend_zan, zanLayout, false);
                //  textView.setText(position);
                textView.setText(i == item.getZhans().size() - 1 ? item.getZhans().get(i).getUser_name() : item.getZhans().get(i).getUser_name() + "，");
                zanLayout.addView(textView);
            }
        }else {
            zanGroup.setVisibility(View.GONE);
        }
        if(!DataUtils.isListEmpty(item.getCommentslist())){
            commentTextListView.setVisibility(View.VISIBLE);
            FriendCommentTextListAdapter friendCommentTextListAdapter = new FriendCommentTextListAdapter(this, item.getCommentslist());
            commentTextListView.setAdapter(friendCommentTextListAdapter);
        }else {
            commentTextListView.setVisibility(View.GONE);
        }

        if(zanGroup.getVisibility() == View.GONE && commentTextListView.getVisibility() == View.GONE){
            commentGroup.setVisibility(View.GONE);
        }else {
            commentGroup.setVisibility(View.VISIBLE);
        }
    }

    private void postComment(String comment){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("zoneid", zoneId);
        params.put("comment", comment);
        showProcessDialog(true);
        new ZoneApi().postComment(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                getZoneDetail();
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendNewsDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void giveToZhan(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("zoneid", zoneId);
        showProcessDialog(true);
        new ZoneApi().giveToZhan(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                getZoneDetail();
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendNewsDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.commentImg){ //评论
            int offX = DensityUtils.getScreenWidth() - DensityUtils.dip2px(90 + 30 + 125);
            friendCommentPopup = new FriendCommentPopup(this);
            friendCommentPopup.setOnClickListener(this);
            friendCommentPopup.showAsDropDown((View) view.getParent(), offX, -DensityUtils.dip2px(35));
        }else if(viewId == R.id.commentLayout){
            friendCommentPopup.dismiss();
            commentEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null){
                imm.showSoftInput(commentEt, InputMethodManager.SHOW_IMPLICIT);
            }
        }else if(viewId == R.id.zanLayout){ //点赞
            friendCommentPopup.dismiss();
            giveToZhan();
        }else if(viewId == R.id.sendTv){
            if(!TextUtils.isEmpty(commentEt.getText().toString())){
                postComment(commentEt.getText().toString());
            }
        }
    }
}
