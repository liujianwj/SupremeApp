package zs.com.supremeapp.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import zs.com.supremeapp.activity.WebActivity;

public class HomeWebFragment extends WebFragment{

    @Override
    protected void handlerView() {
        super.handlerView();
        Bundle params = getArguments();
        if(params != null){
            boolean showTitleBar = params.getBoolean("showTitleBar");
            titleBar.setVisibility(showTitleBar ? View.VISIBLE : View.GONE);
        }
        backLayout.setVisibility(View.INVISIBLE);

        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    titleTv.setText(title);
                }
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
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);
    }
}
