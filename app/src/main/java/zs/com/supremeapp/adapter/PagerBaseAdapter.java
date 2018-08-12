package zs.com.supremeapp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zs.com.supremeapp.page.BasePage;

/**
 * Created by liujian on 2018/8/6.
 */

public class PagerBaseAdapter extends PagerAdapter {
    private List<BasePage> list;

    public PagerBaseAdapter(List<BasePage> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView(list.get(position).getRootView());

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position).getRootView(), 0);
        return list.get(position).getRootView();
    }
}
