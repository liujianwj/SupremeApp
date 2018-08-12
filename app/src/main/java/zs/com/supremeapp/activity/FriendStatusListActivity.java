package zs.com.supremeapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.FriendStatusListAdapter;

/**
 * 朋友圈列表界面
 * Created by liujian on 2018/8/12.
 */

public class FriendStatusListActivity extends BaseActivity {
    @BindView(R.id.listView)
    ListView listView;

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

        FriendStatusListAdapter friendStatusListAdapter = new FriendStatusListAdapter(this);
        listView.setAdapter(friendStatusListAdapter);

    }


}
