package zs.com.supremeapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.FriendStatusListAdapter;
import zs.com.supremeapp.utils.DensityUtils;
import zs.com.supremeapp.widget.FriendCommentPopup;

/**
 * 朋友圈列表界面
 * Created by liujian on 2018/8/12.
 */

public class FriendStatusListActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.selectImage)
    ImageView selectImage;

    private FriendStatusListAdapter friendStatusListAdapter;
    private FriendCommentPopup friendCommentPopup;

    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_friend_status_list);
        super.onCreate(savedInstanceState);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_friend_status_list_header, listView, false);
        SimpleDraweeView bgImg = headerView.findViewById(R.id.bgImg);
        SimpleDraweeView headImg = headerView.findViewById(R.id.headImg);
        bgImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
        headImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
        listView.addHeaderView(headerView);

        friendStatusListAdapter = new FriendStatusListAdapter(this);
        friendStatusListAdapter.setOnClickListener(this);
        listView.setAdapter(friendStatusListAdapter);

        selectImage.setOnClickListener(this);
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
        }else if(viewId == R.id.selectImage){ //选择照片或拍照
            startActivity(new Intent(this, FriendStatusPublishActivity.class));
        }
    }

    private int getY(View view){
        int[] outLocation = new int[2];
        view.getLocationOnScreen(outLocation);
        return outLocation[1];
    }

}
