package zs.com.supremeapp.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.api.DreamApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.DreamPayResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.utils.PayUtils;
import zs.com.supremeapp.utils.TDFPermissionUtils;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private static final int PAY_PERMISSION_REQUEST_CODE = 99;

    @BindView(R.id.backLayout)
    LinearLayout backLayout;
    @BindView(R.id.orderMoneyTv)
    TextView orderMoneyTv;
    @BindView(R.id.aliPayLayout)
    View aliPayLayout;
    @BindView(R.id.wxPayLayout)
    View wxPayLayout;
    @BindView(R.id.payTv)
    TextView payTv;
    @BindView(R.id.aliPayChecked)
    View aliPayChecked;
    @BindView(R.id.wxPayChecked)
    View wxPayChecked;

    private String orderId;
    private double total;
    private int payType = 1; //1:支付宝   2：微信

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_pay);
        super.onCreate(savedInstanceState);

        PayUtils.registerPayReceiver(this, payResultBroadcastReceiver);

        total = getIntent().getDoubleExtra("total", 0);
        orderId = getIntent().getStringExtra("orderId");

        backLayout.setOnClickListener(this);
        wxPayLayout.setOnClickListener(this);
        aliPayLayout.setOnClickListener(this);
        payTv.setOnClickListener(this);
        orderMoneyTv.setText("¥ "+total);
    }

    @Override
    protected void onDestroy() {
        PayUtils.unregisterPayReceiver(this, payResultBroadcastReceiver);
        super.onDestroy();
    }

    private PayUtils.PayResultBroadcastReceiver payResultBroadcastReceiver = new PayUtils.PayResultBroadcastReceiver(new PayUtils.PayCallback() {
        @Override
        public void success() {
            PayUtils.unregisterPayReceiver(PayActivity.this, payResultBroadcastReceiver);
            Toast.makeText(PayActivity.this, "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(Error error, boolean isSync) {
            Toast.makeText(PayActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            if (isSync) {  //未知错误，需要同步服务端
//                syncSuccess(true);
//            } else {
//                btnPayNow.setEnabled(true);
//                TDFDialogUtils.showInfo(PurchaseOrderPayModeActivity.this, error.getMessage(), true);
//            }
        }
    });

    private void payWx(){
        Map<String, String> params = new HashMap<>();
        params.put("userid", Platform.getInstance().getUsrId());
        params.put("dream_order_id", orderId);
        showProcessDialog(true);
        new DreamApi().dreamWXPay(params, new INetWorkCallback<DreamPayResultDO>() {
            @Override
            public void success(DreamPayResultDO dreamPayResultDO, Object... objects) {
                showProcessDialog(false);
                if(dreamPayResultDO != null){
                    //唤起微信
                    PayUtils.payWithWXpay(PayActivity.this, dreamPayResultDO.getList());
                }

            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(PayActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backLayout){
            finish();
        }else if(viewId == R.id.wxPayLayout){
            payType = 2;
            aliPayChecked.setVisibility(View.GONE);
            wxPayChecked.setVisibility(View.VISIBLE);
        }else if(viewId == R.id.aliPayLayout){
            payType = 1;
            aliPayChecked.setVisibility(View.VISIBLE);
            wxPayChecked.setVisibility(View.GONE);
        }else if(viewId == R.id.payTv){
            if(payType == 1){
                showProcessDialog(true);
                checkPermission();
            }else if(payType == 2){
                payWx();
            }
        }
    }

    private String[] needPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void checkPermission(){
        TDFPermissionUtils.needPermission(this, PAY_PERMISSION_REQUEST_CODE, needPermissions, new TDFPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                PayUtils.payWithAlipay(PayActivity.this, "", new PayUtils.PayCallback() {
                    @Override
                    public void success() {
                        showProcessDialog(false);
                    }

                    @Override
                    public void failure(Error error, boolean isSync) {
                        showProcessDialog(false);
                        Toast.makeText(PayActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionDenied() {
                showProcessDialog(false);
                Toast.makeText(PayActivity.this, "缺少权限，请在系统设置中开启所需权限！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
