package zs.com.supremeapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.DreamDetailActivity;
import zs.com.supremeapp.activity.DreamPublishActivity;
import zs.com.supremeapp.activity.FriendStatusListActivity;
import zs.com.supremeapp.activity.MineDreamActivity;
import zs.com.supremeapp.adapter.DreamRecycleAdapter;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.model.BannerDO;
import zs.com.supremeapp.model.CategoryDO;
import zs.com.supremeapp.model.CategoryResultDO;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.model.DreamsResultDO;
import zs.com.supremeapp.model.TopicResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.observer.Observer;
import zs.com.supremeapp.observer.ObserverKey;
import zs.com.supremeapp.observer.SupplySubject;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.widget.BannerViewPagerAdapter;
import zs.com.supremeapp.widget.WidgetCycleViewPager;
import zs.com.supremeapp.widget.WidgetDragTopLayout;

import static android.app.Activity.RESULT_OK;

/**
 * Dream
 * Created by liujian on 2018/8/4.
 */

public class DreamFragment extends BaseFragment implements View.OnClickListener, Observer{

    @BindView(R.id.myListView)
    ListView myListView;
    @BindView(R.id.titleRv)
    RecyclerView titleRv;
    @BindView(R.id.dragLayout)
    WidgetDragTopLayout dragLayout;
    @BindView(R.id.publishLayout)
    View publishLayout;
    @BindView(R.id.banner_viewpager)
    WidgetCycleViewPager bannerViewpager;
    @BindView(R.id.indicatorLayout)
    LinearLayout indicatorLayout;
    @BindView(R.id.mineLayout)
    LinearLayout mineLayout;
    @BindView(R.id.dreamSquareLayout)
    LinearLayout dreamSquareLayout;

    private DreamRecycleAdapter dreamRecycleAdapter;
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;
    private List<DreamDO> dreamDOList;
    private List<CategoryDO> categoryDOList;
    private int currentPage = 1;
    private DreamApi dreamApi;
    private boolean isNextStart;
    private boolean isNeedRefresh;
    private boolean isLoadAll = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SupplySubject.getInstance().attach(this);
        if(mContentView == null){
            super.initFragment(R.layout.fragment_dream);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(bannerViewpager != null && isNextStart){
            bannerViewpager.start();
        }
        if(isNeedRefresh){
            currentPage = 1;
            isLoadAll = false;
            isNeedRefresh = false;
            getDreams(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(bannerViewpager != null){
            bannerViewpager.stop();
        }

    }

    @Override
    public void onDestroy() {
        SupplySubject.getInstance().detach(this);
        super.onDestroy();
    }

    @Override
    void initView() {
        dreamDOList = new ArrayList<>();
        dreamRecycleAdapter = new DreamRecycleAdapter(getActivity(), dreamDOList);
        myListView.setAdapter(dreamRecycleAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle params = new Bundle();
                params.putString("dreamId", dreamDOList.get(i).getDream_id());
                Intent intent = new Intent(getActivity(), DreamDetailActivity.class);
                intent.putExtras(params);
                startActivityForResult(intent, 8888);
            }
        });
        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if(isTop(view)){
                        dragLayout.setTouchMode(true);
                        myListView.setSelection(0);
                    }else if(view.getLastVisiblePosition() == view.getCount() - 1){
                        currentPage++;
                        getDreams(true);
                    }
                }else{
                    dragLayout.setTouchMode(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        categoryDOList = new ArrayList<>();
        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(mContext, categoryDOList);
        dreamTitleRecycleAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        titleRv.setLayoutManager(layoutManager);
        titleRv.setAdapter(dreamTitleRecycleAdapter);

        publishLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        dreamSquareLayout.setOnClickListener(this);
    }

    private boolean isTop(AbsListView view){
        View topChildView = view.getChildAt(0);
        return (view.getFirstVisiblePosition() == 0 && topChildView != null && topChildView.getTop() == 0);
    }

    @Override
    void initData() {
        dreamApi = new DreamApi();
        getTopBanner();
    }

    private void getTopBanner(){
        Map<String, String> params = new HashMap<>();
        showProcessDialog(true);
        dreamApi.getDreamBanner(params, new INetWorkCallback<TopicResultDO>() {
            @Override
            public void success(TopicResultDO topicResultDO, Object... objects) {
                if(topicResultDO != null && !DataUtils.isListEmpty(topicResultDO.getList())){
                    initBannerView(topicResultDO.getList());
                    getDreamCategory();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化banner位
    private void initBannerView(List<BannerDO> bannerList){
        if(bannerList == null || bannerList.size() == 0)
            return;
        if(bannerList.size() > 5){
            bannerList = bannerList.subList(0, 5);
        }
        BannerViewPagerAdapter bannerViewPagerAdapter = new BannerViewPagerAdapter(mContext, bannerList);
        bannerViewPagerAdapter.setOnPagerItemClickListener(new BannerViewPagerAdapter.OnPagerItemClickListener() {
            @Override
            public void onPagerItemClick(View view, int position) {
               // BannerDO bannerVo = bannerList.get(position);
              //  goCampaignList(bannerVo);
            }
        });
        bannerViewpager.setAdapter(bannerViewPagerAdapter);
        bannerViewpager.setDataSize(bannerList.size());
        indicatorLayout.removeAllViews();
        bannerViewpager.setIndexIndicatorView(indicatorLayout, false);
        bannerViewpager.setCurrentItem(1);
        bannerViewpager.start();
        isNextStart = true;
    }

    private void getDreamCategory(){
        Map<String, String> params = new HashMap<>();
        dreamApi.getDreamCate(params, new INetWorkCallback<CategoryResultDO>() {

            @Override
            public void success(CategoryResultDO categoryResultDO, Object... objects) {
                if(categoryResultDO != null && !DataUtils.isListEmpty(categoryResultDO.getList())){
                    categoryDOList.clear();
                    categoryDOList.addAll(categoryResultDO.getList());
                    dreamTitleRecycleAdapter.notifyDataSetChanged();
                    getDreams(false);
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDreams(boolean showLoading){
        if(isLoadAll){
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(currentPage));
        params.put("cateid", dreamTitleRecycleAdapter.getSelectId());
      //  params.put("userid", Platform.getInstance().getUsrId());
        if(showLoading){
            showProcessDialog(true);
        }
        dreamApi.getDreams(params, new INetWorkCallback<DreamsResultDO>() {
            @Override
            public void success(DreamsResultDO dreamsResultDO, Object... objects) {
                showProcessDialog(false);
                if(dreamsResultDO != null){
                    if(currentPage == 1){
                        dreamDOList.clear();
                    }
                    if(!DataUtils.isListEmpty(dreamsResultDO.getList())){
                        dreamDOList.addAll(dreamsResultDO.getList());
                    }
                    dreamRecycleAdapter.notifyDataSetChanged();

                    if(dreamsResultDO.getPs() != null && dreamsResultDO.getPs().getThispage() == dreamsResultDO.getPs().getAllpages()){
                        isLoadAll = true;
                    }
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.titleTv){
            Object tag = view.getTag();
            if(tag instanceof Integer){
                int position = (Integer) tag;
                if(dreamTitleRecycleAdapter.getSelectPosition() == position){
                    return;
                }
                dreamTitleRecycleAdapter.setSelectPosition(position);
                dreamTitleRecycleAdapter.notifyDataSetChanged();
                currentPage = 1;
                isLoadAll = false;
                getDreams(true);
            }
        }else if(viewId == R.id.publishLayout){
            startActivityForResult(new Intent(getActivity(), DreamPublishActivity.class), 8888);
        }else if(viewId == R.id.mineLayout){
            startActivity(new Intent(getActivity(), MineDreamActivity.class));
        }else if(viewId == R.id.dreamSquareLayout){
            startActivity(new Intent(getActivity(), FriendStatusListActivity.class));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8888 && resultCode == RESULT_OK){
            currentPage = 1;
            isLoadAll = false;
            getDreams(true);
        }
    }

    @Override
    public void update(Map<String, Object> params, String key) {
        if(ObserverKey.DREA_FRAGMENT_UPDATE.equals(key)){
            isNeedRefresh = true;
        }
    }
}
