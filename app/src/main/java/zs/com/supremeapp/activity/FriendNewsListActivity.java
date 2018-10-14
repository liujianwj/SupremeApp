package zs.com.supremeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.FriendNewsListAdapter;
import zs.com.supremeapp.model.CommentDO;
import zs.com.supremeapp.model.NewmsgsResultDO;
import zs.com.supremeapp.model.ZanDO;
import zs.com.supremeapp.utils.DataUtils;

/**
 * Created by liujian on 2018/10/11.
 */

public class FriendNewsListActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.backLayout)
    View backLayout;
    @BindView(R.id.listView)
    ListView listView;

    private FriendNewsListAdapter friendNewsListAdapter;
    private NewmsgsResultDO newmsgsResultDO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_friend_news_list);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            newmsgsResultDO = (NewmsgsResultDO)bundle.getSerializable("newmsgsResultDO");
            friendNewsListAdapter = new FriendNewsListAdapter(this, newmsgsResultDO);
            listView.setAdapter(friendNewsListAdapter);
        }

        backLayout.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(!DataUtils.isListEmpty(newmsgsResultDO.getZhans()) && position < newmsgsResultDO.getZhans().size()){
                    ZanDO zanDO = newmsgsResultDO.getZhans().get(position);
                    Intent intent = new Intent(FriendNewsListActivity.this, FriendNewsDetailActivity.class);
                    intent.putExtra("zoneId", zanDO.getTarget_id());
                    startActivity(intent);
                }else {
                    int pos = position - (DataUtils.isListEmpty(newmsgsResultDO.getZhans()) ? 0 : newmsgsResultDO.getZhans().size());
                    CommentDO commentDO = newmsgsResultDO.getMcms_comments().get(pos);
                    Intent intent = new Intent(FriendNewsListActivity.this, FriendNewsDetailActivity.class);
                    intent.putExtra("zoneId", commentDO.getTarget_id());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backLayout){
            finish();
        }
    }
}
