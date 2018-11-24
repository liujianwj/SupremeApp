package zs.com.supremeapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputType;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.ResponseBody;
import zs.com.supremeapp.R;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.LoginResultDO;
import zs.com.supremeapp.model.UserResultDO;
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
    @BindView(R.id.loginTypeTv)
    TextView loginTypeTv;
    @BindView(R.id.passwordEt)
    EditText passwordEt;
    @BindView(R.id.passwordIv)
    ImageView passwordIv;
    @BindView(R.id.phoneNumberEt)
    EditText phoneNumberEt;
    @BindView(R.id.passwordEyeIv)
    ImageView passwordEyeIv;
    @BindView(R.id.voiceCodeTv)
    TextView voiceCodeTv;
    @BindView(R.id.voiceCodeLayout)
    View voiceCodeLayout;

    private Handler secondHandler = new Handler();
    private SecondRunnable secondRunnable;
    private int secondNum = 60;
    private final static long SECOND_TIME = 1000;
    private int loginType = 1;   //1:短信   -1：密码
    private int passwordEyeType = 1; //1:不可见  -1：可见

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        SharedPreferences sp = ShareUtils.getSP(ShareUtils.SHARE_PARAMS, LoginActivity.this);
        String userId = ShareUtils.getValue(sp, "userId", "");
//        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
//            @Override
//            public void onChanged(ConnectionStatus status) {
//                if (status == ConnectionStatus.TOKEN_INCORRECT) {
//                    imConnect();
//                }
//            }
//        });
        if(!TextUtils.isEmpty(userId)){
            Platform.getInstance().setUsrId(userId);
            Platform.getInstance().setMobile(ShareUtils.getValue(sp, "mobile", ""));
            Platform.getInstance().setImToken(ShareUtils.getValue(sp, "im_token", ""));
            imConnect();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginTv.setOnClickListener(this);
        secondTv.setOnClickListener(this);
        loginTypeTv.setOnClickListener(this);
        passwordEyeIv.setOnClickListener(this);
        voiceCodeTv.setOnClickListener(this);
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
    private void loginByMessageCode() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumberEt.getText().toString());
        params.put("code", passwordEt.getText().toString());
        showProcessDialog(true);
        new LoginApi().login(params, new INetWorkCallback<LoginResultDO>() {
            @Override
            public void success(LoginResultDO loginResultDO, Object... objects) {
                showProcessDialog(false);
                handlerLoginResult(loginResultDO);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginByPassword(){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumberEt.getText().toString());
        params.put("password", passwordEt.getText().toString());
        showProcessDialog(true);
        new LoginApi().loginPwd(params, new INetWorkCallback<LoginResultDO>() {
            @Override
            public void success(LoginResultDO loginResultDO, Object... objects) {
                showProcessDialog(false);
                handlerLoginResult(loginResultDO);
            }

            @Override
            public void failure(int errorCode, String message) {
                showProcessDialog(false);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlerLoginResult(LoginResultDO loginResultDO){
        try {
            Log.d("data", loginResultDO.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.getInstance().setUsrId(loginResultDO.getMember().getId());
        Platform.getInstance().setMobile(loginResultDO.getMember().getMobile());
        Platform.getInstance().setImToken(loginResultDO.getMember().getIm_token());
        Platform.getInstance().setAvatar(loginResultDO.getMember().getAvatar());
        Platform.getInstance().setZone_pic(loginResultDO.getMember().getZone_pic());
        SharedPreferences sp = ShareUtils.getSP(ShareUtils.SHARE_PARAMS, LoginActivity.this);
        ShareUtils.updateValue(sp, "userId", loginResultDO.getMember().getId());
        ShareUtils.updateValue(sp, "mobile", loginResultDO.getMember().getMobile());
        ShareUtils.updateValue(sp, "im_token", loginResultDO.getMember().getIm_token());
        imConnect();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void imConnect(){
        RongIM.connect(Platform.getInstance().getImToken(), new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
//                Toast.makeText(LoginActivity.this.getApplicationContext(), "Token 错误", Toast.LENGTH_SHORT).show();
                Log.d("rongyun_im", "Token 错误");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
            //    Toast.makeText(LoginActivity.this.getApplicationContext(), userid + "连接成功", Toast.LENGTH_SHORT).show();
                Log.d("rongyun_im", userid + "连接成功");
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
             //   Toast.makeText(LoginActivity.this.getApplicationContext(), "连接失败" + errorCode, Toast.LENGTH_SHORT).show();
                Log.d("rongyun_im", "连接失败" + errorCode);
            }
        });
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                getUser(s);
                Log.d("UserInfoProvider", s);
                return null;
            }
        }, true);
    }

    private void getUser(String s){
        Map<String, String> params = new HashMap<>();
        params.put("userid", s);
        new LoginApi().getUser(params, new INetWorkCallback<UserResultDO>() {
            @Override
            public void success(UserResultDO userResultDO, Object... objects) {
                if(userResultDO != null && userResultDO.getUserInfo() != null){
                    Uri uri = null;
                    if(!TextUtils.isEmpty(userResultDO.getUserInfo().getAvatar())){
                        uri = Uri.parse(userResultDO.getUserInfo().getAvatar());
                    }
                    UserInfo userInfo = new UserInfo(userResultDO.getUserInfo().getId(), userResultDO.getUserInfo().getName(), uri);
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            }

            @Override
            public void failure(int errorCode, String message) {

            }
        });
    }

    private void getVoiceCode(){
        showProcessDialog(true);
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumberEt.getText().toString());
        new LoginApi().getVoiceCode(params, new INetWorkCallback<ResponseBody>() {
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
              //  Toast.makeText(this, getString(R.string.message_code_blank_tips), Toast.LENGTH_SHORT).show();
                if(loginType == 1){
                    Toast.makeText(this, getString(R.string.message_code_blank_tips), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, getString(R.string.password_blank_tips), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if(loginType == 1){
                loginByMessageCode();
            }else {
                loginByPassword();
            }
        } else if (R.id.secondTv == view.getId()) { //发送读秒
            if (TextUtils.isEmpty(phoneNumberEt.getText().toString())) {
                Toast.makeText(this, getString(R.string.phone_blank_tips), Toast.LENGTH_SHORT).show();
                return;
            }
            secondNum = 60;
            if (secondRunnable == null) {
                secondRunnable = new SecondRunnable(this);
            }
            secondHandler.postDelayed(secondRunnable, SECOND_TIME);
            secondTv.setClickable(false);
            getMessageCode();
        } else if(R.id.loginTypeTv == view.getId()){ //切换登陆方式
            loginType = loginType * -1;
            if(loginType == 1){ //短信登录
                passwordEt.setHint(R.string.message_auth_code);
                passwordIv.setImageResource(R.drawable.smscode_gray);
                loginTypeTv.setText(R.string.mobile_password_login);
                voiceCodeLayout.setVisibility(View.VISIBLE);
                secondTv.setVisibility(View.VISIBLE);
                passwordEyeIv.setVisibility(View.GONE);
                secondTv.setClickable(true);
            }else { //密码登录
                passwordEt.setHint(R.string.password);
                passwordIv.setImageResource(R.drawable.password_gray);
                loginTypeTv.setText(R.string.mobile_message_login);
                voiceCodeLayout.setVisibility(View.GONE);
                secondTv.setVisibility(View.GONE);
                passwordEyeIv.setVisibility(View.VISIBLE);
                if(secondRunnable != null){
                    secondHandler.removeCallbacks(secondRunnable);
                }
            }
        } else if(R.id.passwordEyeIv == view.getId()){
            passwordEyeType = passwordEyeType * -1;
            if(passwordEyeType == 1){ //密码不可见
                passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        } else if(R.id.voiceCodeTv == view.getId()){
            if (TextUtils.isEmpty(phoneNumberEt.getText().toString())) {
                Toast.makeText(this, getString(R.string.phone_blank_tips), Toast.LENGTH_SHORT).show();
                return;
            }
            getVoiceCode();
        }
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
