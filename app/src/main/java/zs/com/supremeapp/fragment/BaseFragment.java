package zs.com.supremeapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * fragment基类
 * Created by liujian on 2018/8/4.
 */

public abstract class BaseFragment extends Fragment {

    private int contentId = -1;

    private View contentView;
    protected Context mContext;

    protected void initFragment(int contentId){
        this.contentId = contentId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentId != -1){
            mContext = this.getContext();
            contentView = inflater.inflate(contentId, container, false);
            ButterKnife.bind(this, contentView);
            initView();
        }
        return contentView;
    }

    abstract void initView();
}
