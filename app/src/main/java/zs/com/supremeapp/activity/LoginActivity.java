package zs.com.supremeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import zs.com.supremeapp.R;

/**
 * 登陆界面
 * Created by liujian on 2018/8/5.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.loginTv)
    TextView loginTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(-1, R.layout.activity_login);
        super.onCreate(savedInstanceState);

        loginTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (R.id.loginTv == view.getId()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
