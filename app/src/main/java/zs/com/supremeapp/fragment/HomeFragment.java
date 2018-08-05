package zs.com.supremeapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import zs.com.supremeapp.R;
import zs.com.supremeapp.model.ContributorDO;
import zs.com.supremeapp.network.HomeApi;
import zs.com.supremeapp.network.INetWorkCallback;

/**
 * 主页
 * Created by liujian on 2018/8/4.
 */

public class HomeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(R.layout.fragment_home);
        initData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    void initView() {

    }

    private void initData(){
        new HomeApi().getContributor(new INetWorkCallback<List<ContributorDO>>() {
            @Override
            public void success(List<ContributorDO> contributorDOS, Object... objects) {
                for(ContributorDO contributor : contributorDOS){
                    Log.d("login",contributor.getLogin());
                    Log.d("contributions",contributor.getContributions());
                }

            }

            @Override
            public void failure(int errorCode, String message) {
                Log.d("failure", message);
            }
        });
    }

}
