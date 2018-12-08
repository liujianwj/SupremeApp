package zs.com.supremeapp.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import zs.com.supremeapp.R;
import zs.com.supremeapp.model.PayResult;
import zs.com.supremeapp.model.WXPayInfoDO;

/**
 *  支付工具类
 */

public class PayUtils {
    public static final String ACTION_PAY_RESULT = "zs.com.supremeapp.ACTION_PAY_RESULT";

    public static final int PAY_WAY_NONE = 0;
    public static final int PAY_WAY_WECHAT = 1;
    public static final int PAY_WAY_ALIPAY = 2;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    //支付方式 1 支付宝 2 微信
    public static final String TAG_PAY_WAY = "payway";
    public static final String TAG_ERROR_CODE = "errCode";

    public static String IWXAPPID = "wxc8bdf92a27854741"; // 微信 APPID

    private static Context mContext;
    /**
     * 支付宝支付
     * @param activity
     * @param payInfo
     * @param callback
     */
    public static void payWithAlipay(final Activity activity, final String payInfo, final PayCallback callback) {
        mContext=activity.getApplicationContext();
        final PayHandler payHandler = new PayHandler(callback);
        Thread payThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo,true);

                Log.d("pay", result);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        });
        payThread.start();
    }

    public static void setIWXAPPID(String iwxappid){
        IWXAPPID=iwxappid;
    }

    /**
     * 处理微信回调事件
     * @param baseResp
     */
    public static void handlerWxPayResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent();
            intent.setAction(PayUtils.ACTION_PAY_RESULT);
            intent.putExtra(PayUtils.TAG_PAY_WAY, PayUtils.PAY_WAY_WECHAT);
            intent.putExtra(PayUtils.TAG_ERROR_CODE, baseResp.errCode);

            if (baseResp.errCode != BaseResp.ErrCode.ERR_OK &&
                    baseResp.errCode != BaseResp.ErrCode.ERR_USER_CANCEL) {
                Toast.makeText(mContext, mContext.getString(R.string.pay_pay_failure) + baseResp.errCode + mContext.getString(R.string.pay_failure_message) +
                        baseResp.errStr, Toast.LENGTH_SHORT).show();
            }

            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }
    /**
     * 微信支付 需在onCreate中调用registerPayReceiver（）
     * onDestroy中调用unregisterPayReceiver()
     * @param context
     * @param wxPayInfoDO
     */
    public static void payWithWXpay(Activity context, WXPayInfoDO wxPayInfoDO){
        if(wxPayInfoDO == null){
            return;
        }
        mContext=context.getApplicationContext();
        if (!isPackageExist(context, "com.tencent.mm")) {
            Toast.makeText(context.getApplicationContext(),context.getApplicationContext().getString(R.string.pay_wx_no_install), Toast.LENGTH_SHORT).show();
            return;
        }
        IWXAPI api = WXAPIFactory.createWXAPI(context, wxPayInfoDO.getAppid());
        PayReq req = new PayReq();
        IWXAPPID=wxPayInfoDO.getAppid();
        req.appId = wxPayInfoDO.getAppid();
        req.partnerId = wxPayInfoDO.getPartnerid();
        req.prepayId = wxPayInfoDO.getPrepayid();
        req.packageValue = wxPayInfoDO.getPackagestr();
        req.nonceStr = wxPayInfoDO.getNoncestr();
        req.timeStamp = wxPayInfoDO.getTimestamp();
        req.sign = wxPayInfoDO.getSign();
        api.registerApp(wxPayInfoDO.getAppid());
        api.sendReq(req);
    }

    /**
     * 注册监听微信支付回调
     * @param activity
     * @param payResultReceiver
     */
    public static void registerPayReceiver(Activity activity, PayResultBroadcastReceiver payResultReceiver){
        //注册监听外部支付回调
        IntentFilter filter = new IntentFilter();
        filter.addAction(PayUtils.ACTION_PAY_RESULT);
        LocalBroadcastManager.getInstance(activity.getApplication()).registerReceiver(payResultReceiver, filter);
    }

    public static void unregisterPayReceiver(Activity activity, PayResultBroadcastReceiver payResultReceiver){
        LocalBroadcastManager.getInstance(activity.getApplication()).unregisterReceiver(payResultReceiver);
        disMemory();
    }

    public interface PayCallback {
        void success();

        void failure(Error error, boolean isSync);
    }

    public static class PayResultBroadcastReceiver extends BroadcastReceiver{
        private PayCallback callback;

        public PayResultBroadcastReceiver(PayCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int payWay = intent.getIntExtra(PayUtils.TAG_PAY_WAY, 0);
            if (payWay == PayUtils.PAY_WAY_WECHAT) {
                int resultCode = intent.getIntExtra(PayUtils.TAG_ERROR_CODE, Integer.MAX_VALUE);
                if (resultCode == BaseResp.ErrCode.ERR_OK) {
                    callback.success();
                } else {
                    callback.failure(new Error(context.getString(R.string.pay_pay_failure_retry)), false);
                }
            }
        }
    }

    private static class PayHandler extends Handler{
        private PayCallback callback;

        public PayHandler(PayCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            if (callback == null) {
                return;
            }
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    Log.e("payResultStatus", resultStatus+"");
                    if (TextUtils.equals(resultStatus, "9000")) {
                        callback.success();
                        disMemory();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000") || TextUtils.equals(resultStatus, "6004")) {
                            callback.failure(new Error(mContext.getString(R.string.pay_result_in_confirm)), true);
                            disMemory();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            callback.failure(new Error(mContext.getString(R.string.pay_cancel_payment)), false);
                            disMemory();
                        } else {
                            callback.failure(new Error(mContext.getString(R.string.pay_pay_failure_retry)), false);
                            disMemory();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    callback.failure(new Error((String) msg.obj), false);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private static boolean isPackageExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageList = packageManager.getInstalledPackages(0);
        for (PackageInfo info : packageList) {
            if (packageName.equals(info.packageName)) {
                return true;
            }
        }
        return false;
    }

    private static void disMemory(){
        mContext=null;
    }
}
