package zs.com.supremeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.DreamRecycleAdapter;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.CategoryDO;
import zs.com.supremeapp.model.CategoryResultDO;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.model.DreamsResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.observer.Observer;
import zs.com.supremeapp.observer.ObserverKey;
import zs.com.supremeapp.observer.SupplySubject;
import zs.com.supremeapp.utils.DataUtils;

/**
 * Created by liujian on 2018/10/15.
 */

public class MineDreamActivity extends BaseActivity implements View.OnClickListener, Observer{

    @BindView(R.id.titleRv)
    RecyclerView titleRv;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    private DreamApi dreamApi;
    private List<CategoryDO> categoryDOList;
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;
    private List<DreamDO> dreamDOList;
    private DreamRecycleAdapter dreamRecycleAdapter;
    private int currentPage = 1;
    private boolean isNeedRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_mine_dream);
        super.onCreate(savedInstanceState);
        SupplySubject.getInstance().attach(this);
        dreamApi = new DreamApi();

        dreamDOList = new ArrayList<>();
        dreamRecycleAdapter = new DreamRecycleAdapter(this, dreamDOList);
        dreamRecycleAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
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
        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(this, categoryDOList);
        dreamTitleRecycleAdapter.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        titleRv.setLayoutManager(layoutManager);
        titleRv.setAdapter(dreamTitleRecycleAdapter);

        getDreamCategory();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isNeedRefresh){
            currentPage = 1;
            isNeedRefresh = false;
            getDreams(true);
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
                Toast.makeText(MineDreamActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDreams(boolean showLoading){
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(currentPage));
        params.put("cateid", dreamTitleRecycleAdapter.getSelectId());
        params.put("userid", Platform.getInstance().getUsrId());
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
                Toast.makeText(MineDreamActivity.this, message, Toast.LENGTH_SHORT).show();
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
                getDreams(true);
            }
        }else if(R.id.itemLayout == viewId){
            Object object = view.getTag();
            if(object != null ){
                int pos = (Integer) object;
                Bundle params = new Bundle();
                params.putString("dreamId", dreamDOList.get(pos).getDream_id());
                Intent intent = new Intent(this, DreamDetailActivity.class);
                intent.putExtras(params);
                startActivityForResult(intent, 8888);
            }
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
