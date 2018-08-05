package zs.com.supremeapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.DreamRecycleAdapter;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;

/**
 * Dream
 * Created by liujian on 2018/8/4.
 */

public class DreamFragment extends BaseFragment {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.titleRv)
    RecyclerView titleRv;

    private DreamRecycleAdapter dreamRecycleAdapter;
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;
    private List<String> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(R.layout.fragment_dream);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    void initView() {
        for(int i = 0; i < 20; i++){
            data.add("item" + i);
        }
        dreamRecycleAdapter = new DreamRecycleAdapter(mContext, data);
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
    }
}
