package zs.com.supremeapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.FriendStatusListAdapter;
import zs.com.supremeapp.api.ZoneApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.NewmsgsResultDO;
import zs.com.supremeapp.model.ZoneDO;
import zs.com.supremeapp.model.ZoneResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DateUtils;
import zs.com.supremeapp.utils.DensityUtils;
import zs.com.supremeapp.widget.FriendCommentPopup;

/**
 * 朋友圈列表界面
 * Created by liujian on 2018/8/12.
 */

public class FriendStatusListActivity extends BaseActivity implements View.OnClickListener{

    private final static int CODE_REFRESH = 8888;

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.selectImage)
    ImageView selectImage;
    @BindView(R.id.backLayout)
    View backLayout;

    private View newsLayout;
    private TextView newsNumTv;

    private FriendStatusListAdapter friendStatusListAdapter;
    private FriendCommentPopup friendCommentPopup;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ZoneApi zoneApi;
    private List<ZoneDO> zoneDOList = new ArrayList<>();
    private int currentPage = 1;
    private NewmsgsResultDO newmsgsResultDO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_friend_status_list);
        super.onCreate(savedInstanceState);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_friend_status_list_header, listView, false);
        SimpleDraweeView bgImg = headerView.findViewById(R.id.bgImg);
        SimpleDraweeView headImg = headerView.findViewById(R.id.headImg);
        bgImg.setImageURI(Platform.getInstance().getZone_pic());
        headImg.setImageURI(Platform.getInstance().getAvatar());
        newsLayout = headerView.findViewById(R.id.newsLayout);
        newsNumTv = headerView.findViewById(R.id.newsNumTv);
        newsLayout.setOnClickListener(this);
        listView.addHeaderView(headerView);

        friendStatusListAdapter = new FriendStatusListAdapter(this, zoneDOList);
        friendStatusListAdapter.setOnClickListener(this);
        listView.setAdapter(friendStatusListAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        currentPage++;
                        getZoneList();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        selectImage.setOnClickListener(this);
        backLayout.setOnClickListener(this);

        zoneApi = new ZoneApi();
        getZoneList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewmsgs();
    }

    private void getZoneList(){
        Map<String, String> params = new HashMap<>();
        params.put("p", currentPage+"");
      //  params.put("userid", Platform.getInstance().getUsrId());
        showProcessDialog(true);
        zoneApi.getZoneList(params, new INetWorkCallback<ZoneResultDO>() {
            @Override
            public void success(ZoneResultDO responseBody, Object... objects) {
                if(responseBody != null && !DataUtils.isListEmpty(responseBody.getList())){
                    if(currentPage == 1){
                        zoneDOList.clear();
                    }
                    zoneDOList.addAll(responseBody.getList());
                    friendStatusListAdapter.notifyDataSetChanged();
                }
                showProcessDialog(false);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendStatusListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNewmsgs(){
        Map<String, String> params = new HashMap<>();
        zoneApi.getNewmsgs(params, new INetWorkCallback<NewmsgsResultDO>() {
            @Override
            public void success(NewmsgsResultDO resultDO, Object... objects) {
                if(resultDO != null){
                    newmsgsResultDO = resultDO;
                    int count = 0;
                    if(!DataUtils.isListEmpty(newmsgsResultDO.getZhans())){
                        count += newmsgsResultDO.getZhans().size();
                    }
                    if(!DataUtils.isListEmpty(newmsgsResultDO.getMcms_comments())){
                        count += newmsgsResultDO.getMcms_comments().size();
                    }
                    if(count > 0){
                        newsLayout.setVisibility(View.VISIBLE);
                        newsNumTv.setText(String.valueOf(count));
                    }else {
                        newsLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void failure(int errorCode, String message) {

            }
        });
    }

    private void giveToZhan(int position){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("zoneid", zoneDOList.get(position).getId());
        showProcessDialog(true);
        zoneApi.giveToZhan(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendStatusListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postComment(int position, String comment){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("zoneid", zoneDOList.get(position).getId());
        params.put("comment", comment);
        showProcessDialog(true);
        zoneApi.postComment(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendStatusListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.commentImg){ //评论
            Object object = view.getTag();
            if(object instanceof Integer){
                int pos = (Integer) object;
                int offX = DensityUtils.getScreenWidth() - DensityUtils.dip2px(90 + 30 + 125);
                friendCommentPopup = new FriendCommentPopup(this);
                friendCommentPopup.setOnClickListener(this);
                friendCommentPopup.setPosition(pos);
                friendCommentPopup.showAsDropDown((View) view.getParent(), offX, -DensityUtils.dip2px(35));
            }
        }else if(viewId == R.id.commentLayout){
            friendCommentPopup.dismiss();
            final int pos = friendCommentPopup.getPosition();
            final Dialog dialog = new Dialog(FriendStatusListActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
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
            final EditText commentEt = dialog.findViewById(R.id.commentEt);
            dialog.findViewById(R.id.sendTv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(!TextUtils.isEmpty(commentEt.getText().toString())){
                        postComment(pos, commentEt.getText().toString());
                    }
                }
            });

            View commentView = friendStatusListAdapter.getItemView(friendCommentPopup.getPosition());
            final int inputY = getY(commentView);
            final int inputHeight = commentView.getHeight();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int dialogY = getY(dialog.findViewById(R.id.dialog_layout_comment));
                    listView.smoothScrollBy( inputY - ( dialogY - inputHeight), 0);
                }
            }, 300);
        }else if(viewId == R.id.zanLayout){//点赞
            int pos = friendCommentPopup.getPosition();
            friendCommentPopup.dismiss();
            giveToZhan(pos);
        } else if(viewId == R.id.selectImage){ //选择照片或拍照
            startActivityForResult(new Intent(this, FriendStatusPublishActivity.class), CODE_REFRESH);
        } else if(viewId == R.id.backLayout){
            finish();
        } else if(viewId == R.id.newsLayout){ // 查看消息列表
            haveRead();
        }
    }

    private void haveRead(){
        Map<String, String> params = new HashMap<>();
        showProcessDialog(true);
        zoneApi.haveRead(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newmsgsResultDO", newmsgsResultDO);
                Intent intent = new Intent(FriendStatusListActivity.this, FriendNewsListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(FriendStatusListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getY(View view){
        int[] outLocation = new int[2];
        view.getLocationOnScreen(outLocation);
        return outLocation[1];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_REFRESH && resultCode == RESULT_OK){
            currentPage = 1;
            getZoneList();
        }
    }

}
