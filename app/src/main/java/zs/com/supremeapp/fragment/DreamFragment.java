package zs.com.supremeapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.DreamDetailActivity;
import zs.com.supremeapp.activity.DreamPublishActivity;
import zs.com.supremeapp.activity.FriendStatusListActivity;
import zs.com.supremeapp.activity.LoginActivity;
import zs.com.supremeapp.activity.MineDreamActivity;
import zs.com.supremeapp.adapter.DreamRecycleAdapter;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.manager.Platform;
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
import zs.com.supremeapp.utils.TDFPermissionUtils;
import zs.com.supremeapp.widget.BannerViewPagerAdapter;
import zs.com.supremeapp.widget.WidgetCycleViewPager;
import zs.com.supremeapp.widget.WidgetDragTopLayout;

import static android.app.Activity.RESULT_OK;

/**
 * Dream
 * Created by liujian on 2018/8/4.
 */

public class DreamFragment extends BaseFragment implements View.OnClickListener, Observer{

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
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


    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (recycleView.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) recycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (recycleView.getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) recycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (recycleView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recycleView.getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = recycleView.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    @Override
    void initView() {
        dreamDOList = new ArrayList<>();
        dreamRecycleAdapter = new DreamRecycleAdapter(mContext, dreamDOList);
        dreamRecycleAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        recycleView.setAdapter(dreamRecycleAdapter);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (getLastVisiblePosition() + 1 == dreamRecycleAdapter.getItemCount()) {
                        currentPage ++;
                        getDreams(true);
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        categoryDOList = new ArrayList<>();
        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(mContext, categoryDOList);
        dreamTitleRecycleAdapter.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        titleRv.setLayoutManager(layoutManager);
        titleRv.setAdapter(dreamTitleRecycleAdapter);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    dragLayout.setTouchMode(isTop(recyclerView));
                }else{
                    dragLayout.setTouchMode(false);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        publishLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        dreamSquareLayout.setOnClickListener(this);
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
        bannerViewpager.setIndexIndicatorView(indicatorLayout, true);
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
                if(dreamsResultDO != null && !DataUtils.isListEmpty(dreamsResultDO.getList())){
                    if(currentPage == 1){
                        dreamDOList.clear();
                    }
                    dreamDOList.addAll(dreamsResultDO.getList());
                    dreamRecycleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isTop(RecyclerView view){
        View topChildView = view.getChildAt(0);
        boolean isFirstVisibleItemPosition = false;
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            isFirstVisibleItemPosition = linearManager.findFirstVisibleItemPosition() == 0;
        }

        return (isFirstVisibleItemPosition && topChildView != null && topChildView.getTop() == 0);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(R.id.itemLayout == viewId){
            Object object = view.getTag();
            if(object != null ){
                int pos = (Integer) object;
                Bundle params = new Bundle();
                params.putString("dreamId", dreamDOList.get(pos).getDream_id());
                Intent intent = new Intent(getActivity(), DreamDetailActivity.class);
                intent.putExtras(params);
                startActivityForResult(intent, 8888);
            }
        }else if(viewId == R.id.titleTv){
            Object tag = view.getTag();
            if(tag instanceof Integer){
                int position = (Integer) tag;
                if(dreamTitleRecycleAdapter.getSelectPosition() == position){
                    return;
                }
                dreamTitleRecycleAdapter.setSelectPosition(position);
                dreamTitleRecycleAdapter.notifyDataSetChanged();
                currentPage = 1;
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
