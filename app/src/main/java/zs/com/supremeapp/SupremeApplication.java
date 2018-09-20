package zs.com.supremeapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by liujian on 2018/8/5.
 */

public class SupremeApplication extends MultiDexApplication{

    private static SupremeApplication supremeApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        supremeApplication = this;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        //百度地图
        SDKInitializer.initialize(getApplicationContext());

        RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
        RongIM.init(this);

    }

    public static Application getInstance(){
        return supremeApplication;
    }
}
