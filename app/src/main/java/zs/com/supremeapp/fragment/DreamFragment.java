package zs.com.supremeapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.DreamDetailActivity;
import zs.com.supremeapp.adapter.DreamRecycleAdapter;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;
import zs.com.supremeapp.widget.WidgetDragTopLayout;

/**
 * Dream
 * Created by liujian on 2018/8/4.
 */

public class DreamFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.titleRv)
    RecyclerView titleRv;
    @BindView(R.id.dragLayout)
    WidgetDragTopLayout dragLayout;

    private DreamRecycleAdapter dreamRecycleAdapter;
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;
    private List<String> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null){
            super.initFragment(R.layout.fragment_dream);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    void initView() {
        for(int i = 0; i < 20; i++){
            data.add("item" + i);
        }
        dreamRecycleAdapter = new DreamRecycleAdapter(mContext, data);
        dreamRecycleAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        recycleView.setAdapter(dreamRecycleAdapter);

        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(mContext, data);
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
    }

    @Override
    void initData() {

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
        if(R.id.itemLayout == view.getId()){
            startActivity(new Intent(getActivity(), DreamDetailActivity.class));
        }
    }
}
