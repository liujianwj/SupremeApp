package zs.com.supremeapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import zs.com.supremeapp.R;

/**
 * dream详情页面
 * Created by liujian on 2018/8/5.
 */

public class DreamDetailActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.backLayout)
    LinearLayout backLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_dream_detail);
        super.onCreate(savedInstanceState);

        backLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(R.id.backLayout == view.getId()){
            finish();
        }
    }
}
