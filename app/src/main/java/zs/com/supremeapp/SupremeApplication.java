package zs.com.supremeapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

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

        //友盟
//        UMConfigure.init(this,"5bc55761f1f5560826000214"
//                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,null);
        UMShareAPI.get(this);
        UMConfigure.setLogEnabled(true);
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxc8bdf92a27854741", "37605cf7fbf4d417aca40b04f4c6e44f");


        //微信支付
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, "wxc8bdf92a27854741", true);


        RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
        RongIM.init(this);

    }

    public static Application getInstance(){
        return supremeApplication;
    }
}
