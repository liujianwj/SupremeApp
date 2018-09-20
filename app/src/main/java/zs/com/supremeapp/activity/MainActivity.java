package zs.com.supremeapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import zs.com.supremeapp.R;
import zs.com.supremeapp.fragment.ChatFragment;
import zs.com.supremeapp.fragment.DreamFragment;
import zs.com.supremeapp.fragment.FindFragment;
import zs.com.supremeapp.fragment.HomeFragment;
import zs.com.supremeapp.fragment.MineFragment;
import zs.com.supremeapp.manager.TabManager;

public class MainActivity extends BaseActivity {

    @BindView(android.R.id.tabhost)
    TabHost mTabHost;

    private TabManager mTabManager;
    private List<Pair<TextView, ImageView>> tabList = new ArrayList<>();
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        mTabHost.setup();
        mTabManager = new TabManager(this, mTabHost, android.R.id.tabcontent);
        initTab();
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus status) {
                if (status == ConnectionStatus.TOKEN_INCORRECT) {
                    jianConnect();
                }
            }
        });
        jianConnect();
    }

    public void jianConnect(){
        String token = "PjsSLMYPo9cyCjtMilWw3aYSYOP9+As0ItQwtH16mltTrcQONwKz8kvo1GGyckS4fxfOXT0xUc71W9/69JF+9Q==";//jian 654321

//        String token = "nolHg4ofNRUZpLHGUtKKEwkXinEBicBkpqMVOQWNI9jJJ+nUl6RvPtih6eBEhM+Yqb3eQVHXIEdZOl8pw+fKIg=="; //liujian 123456
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Toast.makeText(MainActivity.this, "Token 错误", Toast.LENGTH_SHORT).show();
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Toast.makeText(MainActivity.this, userid + "连接成功", Toast.LENGTH_SHORT).show();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(MainActivity.this, "连接失败" + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTab(){
        tabList.clear();
        mTabManager.addTab(getTabSpecView("home", R.layout.tab_item_home), HomeFragment.class, null);
        mTabManager.addTab(getTabSpecView("chat", R.layout.tab_item_chat), ChatFragment.class, null);
        mTabManager.addTab(getTabSpecView("find", R.layout.tab_item_find), FindFragment.class, null);
        mTabManager.addTab(getTabSpecView("dream", R.layout.tab_item_dream), DreamFragment.class, null);
        mTabManager.addTab(getTabSpecView("mine", R.layout.tab_item_mine), MineFragment.class, null);
        mTabManager.setOnTabChangListener(new TabManager.OnTabChangListener() {
            @Override
            public boolean change(String tabId, TabManager.TabInfo tabInfo) {
                initTabColor(currentPosition, false);
                currentPosition = tabInfo.position;
                initTabColor(currentPosition, true);
                return false;
            }
        });
        mTabHost.getTabWidget().setDividerDrawable(R.color.grey_90);

        currentPosition = 0;
        initTabColor(currentPosition, true);

    }

    public TabHost.TabSpec getTabSpecView(String tag, int layoutId) {
        return mTabHost.newTabSpec(tag).setIndicator(getIndicatorView(layoutId));
    }

    private View getIndicatorView(int layout) {
        View tabLayout = getLayoutInflater().inflate(layout, null);
        Pair<TextView, ImageView> pair = new Pair<>((TextView) tabLayout.findViewById(R.id.tabText), (ImageView) tabLayout.findViewById(R.id.tabIcon));
        tabList.add(pair);
        return tabLayout;
    }

    private void initTabColor(int currentPosition, boolean selected){
        Pair<TextView, ImageView> currentTab = tabList.get(currentPosition);
        currentTab.first.setTextColor(getResources().getColor(selected ? R.color.common_red : R.color.grey_43));
        if(currentPosition == 0){
            currentTab.second.setImageResource(selected ? R.drawable.nav_home_fill : R.drawable.nav_home);
        }else if(currentPosition == 1){
            currentTab.second.setImageResource(selected ? R.drawable.nav_im_fill : R.drawable.nav_im);
        }else if(currentPosition == 2){
            currentTab.second.setImageResource(selected ? R.drawable.nav_finder_fill : R.drawable.nav_finder);
        }else if(currentPosition == 3){
            currentTab.second.setImageResource(selected ? R.drawable.nav_dream_fill : R.drawable.nav_dream);
        }else if(currentPosition == 4){
            currentTab.second.setImageResource(selected ? R.drawable.nav_mine_fill : R.drawable.nav_mine);
        }

    }

    @Override
    protected void onDestroy() {

        if (mTabManager != null) {
            mTabManager.onDestroy();
            mTabManager = null;
        }
        super.onDestroy();
    }
}
