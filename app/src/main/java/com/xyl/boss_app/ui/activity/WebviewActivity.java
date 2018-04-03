package com.xyl.boss_app.ui.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xyl.boss_app.R;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.ui.activity.base.BaseActivity;


/**
 * Created by yx on 2015/11/17 0017.
 */
public class WebviewActivity extends BaseActivity implements View.OnClickListener {
    private ViewGroup mViewGroup;

    private WebView mWebView;

    private String mUrl;

    private String mTitleStr;

    WebChromeClient webChromeClient = new WebChromeClient();
    WebViewClient webViewClient = new WebViewClient();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_support;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        mUrl = getIntent().getStringExtra(Constants.Extra.WEBVIEW_URL);
        mTitleStr = getIntent().getStringExtra(Constants.Extra.WEBVIEW_TITLE);
        mTitle.setText(mTitleStr);
        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }

        initializeViews();

    }

    protected void initializeViews() {

        mViewGroup = (LinearLayout) findViewById(R.id.about_support_view_group);
        mWebView = (WebView) findViewById(R.id.about_support_web_view);
        setUpWebView();
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected boolean setToolBar() {
        mTitle.setText(mTitleStr);
        return true;
    }

    protected void setUpWebView() {
        mWebView.requestFocus(View.FOCUS_DOWN);// 手动加入输入焦点, 有些手机不支持键盘弹出
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        //XXX:有些网页已经做了适配但是适配的有问题,
        //XXX:在某些机型上会自动放大页面, 所以设置自动缩小页面到屏幕宽度,
        //XXX:需要网页做更详细的适配调整
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setBuiltInZoomControls(true);// 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        // 隐藏HTML页面中因表单提交而产生提示用户是否保存密码的默认弹出框
        // settings.setSavePassword(false);
        // settings.setSaveFormData(false);
//        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mViewGroup.removeView(mWebView);
            mViewGroup.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    @Override
    //设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
        } else {
            finish();
        }
    }
}
