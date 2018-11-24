package zs.com.supremeapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.rxbus2.Subscribe;
import com.luck.picture.lib.rxbus2.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.rong.eventbus.EventBus;
import io.rong.imlib.RongIMClient;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.event.NavigationControlEvent;
import zs.com.supremeapp.fragment.ChatFragment;
import zs.com.supremeapp.fragment.DreamFragment;
import zs.com.supremeapp.fragment.WebFragment;
import zs.com.supremeapp.manager.ActivityStackManager;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.manager.TabManager;
import zs.com.supremeapp.model.ZanPopStatusResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.DensityUtils;
import zs.com.supremeapp.utils.ShareUtils;
import zs.com.supremeapp.widget.GetZanDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @BindView(android.R.id.tabhost)
    TabHost mTabHost;
    @BindView(android.R.id.tabs)
    TabWidget mTabWidget;
    @BindView(android.R.id.tabcontent)
    FrameLayout tabcontent;

    private TabManager mTabManager;
    private List<Pair<TextView, ImageView>> tabList = new ArrayList<>();
    private int currentPosition;
    private GetZanDialog getZanDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mTabHost.setup();
        mTabManager = new TabManager(this, mTabHost, android.R.id.tabcontent);
        initTab();
//        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
//            @Override
//            public void onChanged(ConnectionStatus status) {
//                if (status == ConnectionStatus.TOKEN_INCORRECT) {
//                    jianConnect();
//                }
//            }
//        });
    //    jianConnect();
        getZanPopStatus();
    }

    public void onEvent(NavigationControlEvent messageEvent) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tabcontent.getLayoutParams();
        if(messageEvent.isShow()){
            layoutParams.bottomMargin = DensityUtils.dip2px(60);
            tabcontent.setLayoutParams(layoutParams);
            mTabWidget.setVisibility(View.VISIBLE);
        }else {
            layoutParams.bottomMargin = DensityUtils.dip2px(0);
            tabcontent.setLayoutParams(layoutParams);
            mTabWidget.setVisibility(View.GONE);
        }
    }


    private void getZanPopStatus(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        new DreamApi().getZanPopStatus(params, new INetWorkCallback<ZanPopStatusResultDO>() {
            @Override
            public void success(ZanPopStatusResultDO zanPopStatusResultDO, Object... objects) {
                if(zanPopStatusResultDO != null && zanPopStatusResultDO.getMember_zhan_pop() != null){
                    if(zanPopStatusResultDO.getMember_zhan_pop().getCanpop() == 1){
                        getZanDialog = new GetZanDialog.Builder(MainActivity.this)
                                .setOnClickListener(MainActivity.this)
                                .create();
                        getZanDialog.show();
                    }
                }
            }

            @Override
            public void failure(int errorCode, String message) {

            }
        });
    }

    private void initTab(){
        tabList.clear();
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://app.cw2009.com/");
        mTabManager.addTab(getTabSpecView("home", R.layout.tab_item_home), WebFragment.class, bundle);
        mTabManager.addTab(getTabSpecView("chat", R.layout.tab_item_chat), ChatFragment.class, null);
        bundle = new Bundle();
        bundle.putString("url", "http://app.cw2009.com/finder.html");
        //private final String url = "file:///android_asset/jstest.html";
       // bundle.putString("url", "file:///android_asset/jstest.html");
        mTabManager.addTab(getTabSpecView("find", R.layout.tab_item_find), WebFragment.class, bundle);
        mTabManager.addTab(getTabSpecView("dream", R.layout.tab_item_dream), DreamFragment.class, null);
        bundle = new Bundle();
        bundle.putString("url", "http://app.cw2009.com/choosemyidentity.html");
        mTabManager.addTab(getTabSpecView("mine", R.layout.tab_item_mine), WebFragment.class, bundle);
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void getFreeZan(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        showProcessDialog(true);
        new DreamApi().getFreeZan(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                getZanDialog.dismiss();
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.getZanTv){ //获取点赞数
            getFreeZan();
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivityStackManager.getInstance().finishAllActivity();
            RongIMClient.getInstance().logout();
            System.exit(0);
            super.onBackPressed();
        }
    }
}
