package zs.com.supremeapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.LoginResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.api.LoginApi;
import zs.com.supremeapp.utils.ShareUtils;
import zs.com.supremeapp.utils.ToolUtils;

/**
 * 登陆界面
 * 暂时只支持短信+验证码登录
 * <p>
 * APPID : cw2009com
 * 加密串：E350D68BCD46861FEEFB3EFEEAE9F936
 * 时间戳：time()
 * sha1(md5('cw2009com'.'E350D68BCD46861FEEFB3EFEEAE9F936'.time()));
 * Created by liujian on 2018/8/5.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.loginTv)
    TextView loginTv;
    @BindView(R.id.secondTv)
    TextView secondTv;
    //    @BindView(R.id.loginTypeTv)
//    TextView loginTypeTv;
    @BindView(R.id.passwordEt)
    EditText passwordEt;
    @BindView(R.id.passwordIv)
    ImageView passwordIv;
    @BindView(R.id.phoneNumberEt)
    EditText phoneNumberEt;
//    @BindView(R.id.passwordEyeIv)
//    ImageView passwordEyeIv;

    private Handler secondHandler = new Handler();
    private SecondRunnable secondRunnable;
    private int secondNum = 60;
    private final static long SECOND_TIME = 1000;
    // private int loginType = 1;   //1:短信   -1：密码
    // private int passwordEyeType = 1; //1:不可见  -1：可见

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        SharedPreferences sp = ShareUtils.getSP(ShareUtils.SHARE_PARAMS, LoginActivity.this);
        String userId = ShareUtils.getValue(sp, "userId", "");
        if(!TextUtils.isEmpty(userId)){
            Platform.getInstance().setUsrId(userId);
            Platform.getInstance().setMobile(ShareUtils.getValue(sp, "mobile", ""));
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginTv.setOnClickListener(this);
        secondTv.setOnClickListener(this);
        //   loginTypeTv.setOnClickListener(this);
        //   passwordEyeIv.setOnClickListener(this);
    }

    private void getMessageCode() {
        showProcessDialog(true);
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumberEt.getText().toString());
        new LoginApi().getMessageCode(params, new INetWorkCallback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Object... objects) {
                showProcessDialog(false);
                try {
                    Log.d("data", responseBody.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //登录
    private void login() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumberEt.getText().toString());
        params.put("code", passwordEt.getText().toString());
        showProcessDialog(true);
        new LoginApi().login(params, new INetWorkCallback<LoginResultDO>() {
            @Override
            public void success(LoginResultDO loginResultDO, Object... objects) {
                showProcessDialog(false);
                try {
                    Log.d("data", loginResultDO.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Platform.getInstance().setUsrId(loginResultDO.getMember().getId());
                Platform.getInstance().setMobile(loginResultDO.getMember().getMobile());
                SharedPreferences sp = ShareUtils.getSP(ShareUtils.SHARE_PARAMS, LoginActivity.this);
                ShareUtils.updateValue(sp, "userId", loginResultDO.getMember().getId());
                ShareUtils.updateValue(sp, "mobile", loginResultDO.getMember().getMobile());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (R.id.loginTv == view.getId()) {
            if (TextUtils.isEmpty(phoneNumberEt.getText().toString())) {
                Toast.makeText(this, getString(R.string.phone_blank_tips), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!ToolUtils.isMobileNO(phoneNumberEt.getText().toString())) {
                Toast.makeText(this, getString(R.string.phone_error_tips), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passwordEt.getText().toString())) {
                Toast.makeText(this, getString(R.string.message_code_blank_tips), Toast.LENGTH_SHORT).show();
//                if(loginType == 1){
//                    Toast.makeText(this, getString(R.string.message_code_blank_tips), Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(this, getString(R.string.password_blank_tips), Toast.LENGTH_SHORT).show();
//                }
                return;
            }
            login();

        } else if (R.id.secondTv == view.getId()) { //发送读秒
            secondNum = 60;
            if (secondRunnable == null) {
                secondRunnable = new SecondRunnable(this);
            }
            secondHandler.postDelayed(secondRunnable, SECOND_TIME);
            secondTv.setClickable(false);
            getMessageCode();
        }
//        else if(R.id.loginTypeTv == view.getId()){ //切换登陆方式
//            loginType = loginType * -1;
//            if(loginType == 1){ //短信登录
//                passwordEt.setHint(R.string.message_auth_code);
//                passwordIv.setImageResource(R.drawable.smscode_gray);
//                loginTypeTv.setText(R.string.mobile_password_login);
//                secondTv.setClickable(true);
//            }else { //密码登录
//                passwordEt.setHint(R.string.password);
//                passwordIv.setImageResource(R.drawable.password_gray);
//                loginTypeTv.setText(R.string.mobile_message_login);
//                if(secondRunnable != null){
//                    secondHandler.removeCallbacks(secondRunnable);
//                }
//            }
//        }
//        else if(R.id.passwordEyeIv == view.getId()){
//            passwordEyeType = passwordEyeType * -1;
//            if(passwordEyeType == 1){ //密码不可见
//                passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            }else {
//                passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            }
//        }
    }

    @Override
    public void onDestroy() {
        if (secondRunnable != null) {
            secondHandler.removeCallbacks(secondRunnable);
        }
        super.onDestroy();
    }

    public void secondNext() {
        if (secondNum > 0) {
            secondNum--;
            if (secondRunnable == null) {
                secondRunnable = new SecondRunnable(this);
            }
            secondTv.setText(secondNum + "s");
            secondHandler.postDelayed(secondRunnable, SECOND_TIME);
        } else {
            //显示重试
            secondTv.setText("重发");
            secondNum = 60;
            secondHandler.removeCallbacks(secondRunnable);
            secondTv.setClickable(true);
        }
    }

    private static class SecondRunnable implements Runnable {

        WeakReference<LoginActivity> weakReference;

        public SecondRunnable(LoginActivity activity) {
            this.weakReference = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void run() {
            LoginActivity loginActivity = weakReference.get();
            if (loginActivity == null) {
                return;
            }
            loginActivity.secondNext();
        }
    }


}
