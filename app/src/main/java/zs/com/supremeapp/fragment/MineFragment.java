package zs.com.supremeapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zs.com.supremeapp.R;

/**
 * 我的
 * Created by liujian on 2018/8/4.
 */

public class MineFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(R.layout.fragment_mine);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    void initView() {

    }
}
