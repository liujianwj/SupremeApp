package zs.com.supremeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/10/12.
 */

public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_splash);
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
