package zs.com.supremeapp.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by liujian on 2018/8/6.
 */

public abstract class BasePage {
    private View mView;
    protected Context context;

    public BasePage(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mView = initView(layoutInflater);
    }

    public abstract View initView(LayoutInflater layoutInflater);

    public abstract void initData();

    public View getRootView(){
        return mView;
    }
}

