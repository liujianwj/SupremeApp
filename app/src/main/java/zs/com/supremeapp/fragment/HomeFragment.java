package zs.com.supremeapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;


import java.net.URLEncoder;

import butterknife.BindView;
import zs.com.supremeapp.BuildConfig;
import zs.com.supremeapp.R;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.widget.webview.BridgeWebView;

/**
 * 主页
 * Created by liujian on 2018/8/4.
 */

public class HomeFragment extends BaseFragment {

    private final String url = "http://app.cw2009.com/";

    @BindView(R.id.webView)
    BridgeWebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null){
            super.initFragment(R.layout.fragment_home);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    void initView() {

    }

    @Override
    void initData(){
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
            CookieSyncManager.getInstance().sync();
        }
    }

    private String formatWebViewUserAgent(WebView webView) {
        return "cw2009/" + BuildConfig.VERSION_NAME + " " + webView.getSettings().getUserAgentString();
    }

}
