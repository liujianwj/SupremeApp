package zs.com.supremeapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import zs.com.supremeapp.utils.ProcessDialogUtils;

/**
 * fragment基类
 * Created by liujian on 2018/8/4.
 */

public abstract class BaseFragment extends Fragment {

    private int contentId = -1;

    protected View mContentView;
    protected Context mContext;

    protected ProcessDialogUtils processDialogUtils;

    protected void initFragment(int contentId){
        this.contentId = contentId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentId != -1){
            mContext = this.getContext();
            mContentView = inflater.inflate(contentId, container, false);
            ButterKnife.bind(this, mContentView);
            processDialogUtils = new ProcessDialogUtils(mContext);
            initView();
            initData();
        }
        return mContentView;
    }

    abstract void initView();

    abstract void initData();

    protected void showProcessDialog(boolean isShow){
        showProcessDialog(isShow, ProcessDialogUtils.LOAD_TYPE_COMMON);
    }

    protected void showProcessDialog(boolean isShow, Integer processType){
        if(isShow){
            processDialogUtils.createAndShow("正在加载", processType, ProcessDialogUtils.DEFAULT_SHOW_TIME, false, false);
        }else {
            processDialogUtils.dismissDialog();
        }

    }
}
