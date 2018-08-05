package zs.com.supremeapp.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Iterator;

import zs.com.supremeapp.R;
import zs.com.supremeapp.fragment.BaseFragment;

public class TabManager implements TabHost.OnTabChangeListener {
    private FragmentActivity mActivity;
    private TabHost mTabHost;
    private final int mContainerId;
    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo mLastTab;
    private OnTabChangListener mTabChangListener;

    private FragmentManager mFragmentManager;


    public static final class TabInfo {
        public final String tag;
        public int position;
        private Class<?> clss = null;
        public Bundle args;
        public BaseFragment fragment;

        public TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }


    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
        mActivity = activity;
        mTabHost = tabHost;
        mContainerId = containerId;
        mTabHost.setOnTabChangedListener(this);
        mFragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * @param tabSpec
     * @param clss
     * @param args
     */
    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mActivity));
        String tag = tabSpec.getTag();
        TabInfo info = new TabInfo(tag, clss, args);
        info.position = mTabs.size();
        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state. If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        info.fragment = (BaseFragment) mFragmentManager.findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.detach(info.fragment);
            // ft.commit();
            ft.commitAllowingStateLoss();
        }
        mTabs.put(tag, info);
        mTabHost.addTab(tabSpec);
    }


    public TabInfo getTab(String tab) {
        return mTabs.get(tab);
    }

    public void updateSquareTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, String title) {
        try {

            String tag = tabSpec.getTag();
            TabInfo oldTab = mTabs.get(tag);

            oldTab.fragment = (BaseFragment) mFragmentManager.findFragmentByTag(tag);
            if (oldTab.fragment != null && !oldTab.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.detach(oldTab.fragment);
                // ft.commit();
                ft.commitAllowingStateLoss();
            }

            TabInfo newTab = new TabInfo(tag, clss, args);
            mTabs.put(tag, newTab);
            TextView tabText = (TextView) mTabHost.getTabWidget().
                    getChildAt(1).findViewById(R.id.tabText);
            tabText.setText(title);
            if (mLastTab != null && tag.equals(mLastTab.tag) && mLastTab != newTab) {
                mLastTab.fragment = null;
                mLastTab.args = null;
                mLastTab.clss = null;
                mLastTab = null;
                setTab(newTab);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        TabInfo newTab = mTabs.get(tabId);
        if (null != mTabChangListener && this.mTabChangListener.change(tabId, newTab)) {
            return;
        }

        if (mLastTab != newTab) {
            setTab(newTab);
        }
    }

    /**
     * @param newTab
     */
    private void setTab(TabInfo newTab) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mLastTab != null) {
            if (mLastTab.fragment != null) {
                ft.detach(mLastTab.fragment);
            }
        }
        if (newTab != null) {
            if (null == newTab.fragment) {
                newTab.fragment = (BaseFragment) Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
                ft.add(mContainerId, newTab.fragment, newTab.tag);
            } else {
                ft.attach(newTab.fragment);
            }
        }
        if (newTab.fragment.isAdded())
            return;
        mLastTab = newTab;
        // ft.commit();
        ft.commitAllowingStateLoss();
//        mFragmentManager.executePendingTransactions();
    }

    public void setOnTabChangListener(OnTabChangListener l) {
        this.mTabChangListener = l;
    }


    public TabInfo getCurrentTab() {
        return mLastTab;
    }


    static public interface OnTabChangListener {
        public abstract boolean change(String tabId, TabInfo tabInfo);
    }

    public void onDestroy() {
        Iterator<String> keys = mTabs.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            TabInfo tab = mTabs.get(key);
            BaseFragment f = tab.fragment;
            if (f != null) {
                f.onDetach();
            }
            tab.fragment = null;
            tab.clss = null;
            tab.args = null;
        }
        mTabs.clear();
        mTabChangListener = null;
        if (mLastTab != null) {
            mLastTab.fragment = null;
            mLastTab.args = null;
            mLastTab.clss = null;
            mLastTab = null;
        }
        mActivity = null;
        if (mTabHost != null) {
            mTabHost.setOnTabChangedListener(null);
            mTabHost.removeAllViewsInLayout();
            mTabHost = null;
        }
    }
}
