package zs.com.supremeapp.page;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;

/**
 * 互动界面
 * Created by liujian on 2018/8/6.
 */

public class InteractListPage extends BasePage implements View.OnClickListener{

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.titleRv)
    RecyclerView titleRv;

    private List<String> data;
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;

    public InteractListPage(Context context) {
        super(context);
    }

    @Override
    public View initView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_interact_list, null);
        ButterKnife.bind(this, view);
        data = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            data.add("item" + i);
        }
        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(context, null);
        dreamTitleRecycleAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        titleRv.setLayoutManager(layoutManager);
        titleRv.setAdapter(dreamTitleRecycleAdapter);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.titleTv){
            Object tag = view.getTag();
            if(tag instanceof Integer){
                int position = (Integer) tag;
                dreamTitleRecycleAdapter.setSelectPosition(position);
                dreamTitleRecycleAdapter.notifyDataSetChanged();
            }
        }
    }
}
