package zs.com.supremeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
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
    @BindView(R.id.myListView)
    ListView myListView;
    @BindView(R.id.backLayout)
    View backLayout;

    private DreamApi dreamApi;
    private List<CategoryDO> categoryDOList;
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;
    private List<DreamDO> dreamDOList;
    private DreamRecycleAdapter dreamRecycleAdapter;
    private int currentPage = 1;
    private boolean isNeedRefresh;
    private boolean isLoadAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_mine_dream);
        super.onCreate(savedInstanceState);
        SupplySubject.getInstance().attach(this);
        dreamApi = new DreamApi();
        backLayout.setOnClickListener(this);
        dreamDOList = new ArrayList<>();
        dreamRecycleAdapter = new DreamRecycleAdapter(this, dreamDOList);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle params = new Bundle();
                params.putString("dreamId", dreamDOList.get(i).getDream_id());
                Intent intent = new Intent(MineDreamActivity.this, DreamDetailActivity.class);
                intent.putExtras(params);
                startActivityForResult(intent, 8888);
            }
        });
        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if(view.getLastVisiblePosition() == view.getCount() - 1){
                        currentPage++;
                        getDreams(true);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        myListView.setAdapter(dreamRecycleAdapter);

        categoryDOList = new ArrayList<>();
        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(this, categoryDOList);
        dreamTitleRecycleAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
            isLoadAll = false;
            isNeedRefresh = false;
            getDreams(true);
        }
    }

    @Override
    public void onDestroy() {
        SupplySubject.getInstance().detach(this);
        super.onDestroy();
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
        if(isLoadAll){
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(currentPage));
        params.put("cateid", dreamTitleRecycleAdapter.getSelectId());
        params.put("userid", Platform.getInstance().getUsrId());
        if(showLoading){
            showProcessDialog(true);
        }
        dreamApi.getMyDreams(params, new INetWorkCallback<DreamsResultDO>() {
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
                isLoadAll = false;
                getDreams(true);
            }
        }else if(viewId == R.id.backLayout){
            finish();
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
