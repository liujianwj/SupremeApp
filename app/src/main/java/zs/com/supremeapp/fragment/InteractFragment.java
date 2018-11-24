package zs.com.supremeapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.DreamTitleRecycleAdapter;
import zs.com.supremeapp.adapter.InteractListAdapter;
import zs.com.supremeapp.api.ChatApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.CategoryDO;
import zs.com.supremeapp.model.DynamicsResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.DataUtils;

/**
 * Created by liujian on 2018/10/16.
 */

public class InteractFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.titleRv)
    RecyclerView titleRv;

    private String[] titles = {"看过我", "点过赞", "评论过", "投诉过"};
    private DreamTitleRecycleAdapter dreamTitleRecycleAdapter;
    private int currentPage = 1;
    private InteractListAdapter interactListAdapter;
    private List<DynamicsResultDO.DynamicsDO> dynamicsDOList;
    private boolean isFirst = true;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null){
            super.initFragment(R.layout.fragment_interact);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    void initView() {
        dynamicsDOList = new ArrayList<>();
        interactListAdapter = new InteractListAdapter(mContext, dynamicsDOList);
        interactListAdapter.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        recycleView.setAdapter(interactListAdapter);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (getLastVisiblePosition() + 1 == interactListAdapter.getItemCount()) {
                        currentPage ++;
                        getDynamics();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        List<CategoryDO> categoryDOList = new ArrayList<>();
        int id = 1;
        for(String title : titles){
            CategoryDO categoryDO = new CategoryDO();
            categoryDO.setId(String.valueOf(id++));
            categoryDO.setCateName(title);
            categoryDOList.add(categoryDO);
        }
        dreamTitleRecycleAdapter = new DreamTitleRecycleAdapter(mContext, categoryDOList);
        dreamTitleRecycleAdapter.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        titleRv.setLayoutManager(layoutManager);
        titleRv.setAdapter(dreamTitleRecycleAdapter);
    }

    @Override
    void initData() {
        if(!isFirst){
            getDynamics();
        }
    }

    private void getDynamics(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("type", dreamTitleRecycleAdapter.getSelectId());
        params.put("p", String.valueOf(currentPage));
        showProcessDialog(true);
        new ChatApi().getDynamics(params, new INetWorkCallback<DynamicsResultDO>() {
            @Override
            public void success(DynamicsResultDO dynamicsResultDO, Object... objects) {
                showProcessDialog(false);
                if(dynamicsResultDO != null && !DataUtils.isListEmpty(dynamicsResultDO.getList())){
                    if(currentPage == 1){
                        dynamicsDOList.clear();
                    }
                    dynamicsDOList.addAll(dynamicsResultDO.getList());
                    interactListAdapter.notifyDataSetChanged();
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
                getDynamics();
            }
        }else if(viewId == R.id.itemLayout){
            Object object = view.getTag();
            if(object != null ){
                int pos = (Integer) object;
                RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.PRIVATE, dynamicsDOList.get(pos).getUser_id(), dynamicsDOList.get(pos).getUser_name());
            }
        }
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
}
