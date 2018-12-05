package zs.com.supremeapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.BuildConfig;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.DreamDetailActivity;
import zs.com.supremeapp.activity.FriendStatusListActivity;
import zs.com.supremeapp.activity.LoginActivity;
import zs.com.supremeapp.event.NavigationControlEvent;
import zs.com.supremeapp.manager.ActivityStackManager;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.utils.ShareUtils;
import zs.com.supremeapp.utils.UMHelpUtils;

/**
 * Created by liujian on 2018/10/15.
 */

public class WebFragment extends BaseFragment{

    private String url;

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.backLayout)
    View backLayout;
    @BindView(R.id.titleBar)
    View titleBar;

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

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    void initView() {
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    if(getActivity() != null){
                        getActivity().finish();
                    }
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavascriptInterfaceListener(getContext()),
                "android");
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//        });
        WebViewClient webViewClient = new WebViewClient() {

//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                if(TextUtils.equals("http://app.cw2009.com/", url)
//                        || TextUtils.equals("http://app.cw2009.com/finder.html", url)
//                        || TextUtils.equals("http://app.cw2009.com/choosemyidentity.html", url) ){
//                    EventBus.getDefault().post(new NavigationControlEvent(true));
//                }else {
//                    EventBus.getDefault().post(new NavigationControlEvent(false));
//                }
//                super.onPageStarted(view, url, favicon);
//            }
//
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    titleTv.setText(title);
                }
             //   backLayout.setVisibility(webView.canGoBack() ? View.VISIBLE : View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, String url) {
                if(url == null) return false;

                try {
                    if(url.startsWith("weixin://") //微信
                            || url.startsWith("alipays://") //支付宝
                            || url.startsWith("mailto://") //邮件
                            || url.startsWith("tel:")//电话
                            || url.startsWith("dianping://")//大众点评
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
                //处理http和https开头的url
                wv.loadUrl(url);
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);
        handlerView();
    }

    protected void handlerView(){

    }


    public class JavascriptInterfaceListener {
        Context context;

        JavascriptInterfaceListener(Context c) {
            context= c;
        }

        //调用朋友圈   zoneList()
        @JavascriptInterface
        public void zoneList() {

            startActivity(new Intent(getActivity(), FriendStatusListActivity.class));
        }

        //调用聊天接口  chatView(userId, title)
        @JavascriptInterface
        public void chatView(String userId, String title){
            RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.PRIVATE, userId, title);
        }

        //调用分享接口  share(title, message)
        @JavascriptInterface
        public void share(String title, String message){
            UMHelpUtils.shareWebToWX(getActivity(),url, title, message);
        }

        //调用退出登录接口   loginOut()
        @JavascriptInterface
        public void loginOut(){
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

        //调用Dream详情   dreamDetail(dreamId)
        @JavascriptInterface
        public void dreamDetail(String dreamId){
            Bundle params = new Bundle();
            params.putString("dreamId", dreamId);
            Intent intent = new Intent(getActivity(), DreamDetailActivity.class);
            intent.putExtras(params);
            startActivity(intent);
        }
    }

    @Override
    void initData() {
        synCookies(url);
        webView.getSettings().setUserAgentString(formatWebViewUserAgent(webView));
        webView.loadUrl(url);
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
            cookieManager.setCookie(url, "uid=" + Platform.getInstance().getUid());
            cookieManager.setCookie(url, "umobile=" + Platform.getInstance().getUmobile());
            CookieSyncManager.getInstance().sync();
        }
    }

    private String formatWebViewUserAgent(WebView webView) {
        return "cw2009/" + BuildConfig.VERSION_NAME + " " + webView.getSettings().getUserAgentString();
    }
}

