package zs.com.supremeapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import zs.com.supremeapp.R;
import zs.com.supremeapp.fragment.WebFragment;

/**
 * Created by liujian on 2018/10/15.
 */

public class WebActivity extends BaseActivity{

    private WebFragment webFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_web);
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            webFragment = (WebFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "WebFragment");
        } else {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                bundle.putBoolean("showTitle", true);
            }
            webFragment = WebFragment.newInstance(bundle);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, webFragment)
                .commit();
    }

    /**
     * 当活动被回收时，存储当前的状态。
     *
     * @param outState 状态数据。
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 存储 Fragment 的状态。
        getSupportFragmentManager().putFragment(outState, "WebFragment", webFragment);
    }
}
