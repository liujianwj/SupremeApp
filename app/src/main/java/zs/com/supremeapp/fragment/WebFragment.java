package zs.com.supremeapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.BuildConfig;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.DreamDetailActivity;
import zs.com.supremeapp.activity.FriendStatusListActivity;
import zs.com.supremeapp.activity.LoginActivity;
import zs.com.supremeapp.manager.ActivityStackManager;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.utils.ShareUtils;
import zs.com.supremeapp.utils.UMHelpUtils;
import zs.com.supremeapp.widget.webview.BridgeHandler;
import zs.com.supremeapp.widget.webview.BridgeWebView;
import zs.com.supremeapp.widget.webview.CallBackFunction;

/**
 * Created by liujian on 2018/10/15.
 */

public class WebFragment extends BaseFragment{

    private String url;

    @BindView(R.id.webView)
    BridgeWebView webView;

    public static WebFragment newInstance(Bundle params) {
        WebFragment webFragment = new WebFragment();
        webFragment.setArguments(params);
        return webFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null){
            super.initFragment(R.layout.fragment_mine);

            Bundle params = getArguments();
            if(params != null){
                url = params.getString("url", "");
            }
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    void initView() {
        webView.setWebViewClientListener(new BridgeWebView.WebViewClientListener() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                registerHandler();
            }
        });
    }

    @Override
    void initData() {
        synCookies(url);
        webView.getSettings().setUserAgentString(formatWebViewUserAgent(webView));
        webView.loadUrl(url);
    }

    private void registerHandler(){
        //调用聊天接口  chatView(userId, title)
        webView.registerHandler("chatView", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d("chatView-data", data);
                JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
                String userId = jsonObject.get("userId").getAsString();
                String title = jsonObject.get("title").getAsString();
                RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.PRIVATE, userId, title);
            }
        });

        //调用分享接口  share(title, message)
        webView.registerHandler("share", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d("chatView-data", data);
                JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
                String title = jsonObject.get("title").getAsString();
                String message = jsonObject.get("message").getAsString();
                UMHelpUtils.shareWebToWX(getActivity(),url, title, message);
            }
        });

        //调用退出登录接口   loginOut()
        webView.registerHandler("loginOut", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                Platform.getInstance().setUsrId(null);
                Platform.getInstance().setMobile(null);
                Platform.getInstance().setImToken(null);
                Platform.getInstance().setZone_pic(null);
                Platform.getInstance().setAvatar(null);

                SharedPreferences sp = ShareUtils.getSP(ShareUtils.SHARE_PARAMS, getActivity());
                ShareUtils.updateValue(sp, "userId", "");
                ShareUtils.updateValue(sp, "mobile", "");
                ShareUtils.updateValue(sp, "im_token","");

                ActivityStackManager.getInstance().finishAllActivity();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        //调用Dream详情   dreamDetail(dreamId)
        webView.registerHandler("dreamDetail", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d("dreamDetail-data", data);
                JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
                String dreamId = jsonObject.get("dreamId").getAsString();
                Bundle params = new Bundle();
                params.putString("dreamId", dreamId);
                Intent intent = new Intent(getActivity(), DreamDetailActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        //调用朋友圈   zoneList()
        webView.registerHandler("zoneList", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                startActivity(new Intent(getActivity(), FriendStatusListActivity.class));
            }
        });
    }

    public void synCookies(String url) {
        // Uri uri = Uri.parse(url);
        if (url.startsWith("http")) {  // 仅http类型需要埋cookie
            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();

            cookieManager.setCookie(url,"userid=" + Platform.getInstance().getUsrId());
            cookieManager.setCookie(url, "mobile=" + Platform.getInstance().getMobile());
            CookieSyncManager.getInstance().sync();
        }
    }

    private String formatWebViewUserAgent(WebView webView) {
        return "cw2009/" + BuildConfig.VERSION_NAME + " " + webView.getSettings().getUserAgentString();
    }
}

