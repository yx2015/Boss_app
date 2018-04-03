package com.xyl.boss_app.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.xyl.boss_app.R;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.TelephonyUtils;
import com.xyl.boss_app.utils.UIUtils;

/**
 * Created by yx on 2015/12/14 0014.
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mHowToRegister, mForgetPwd, mCustom;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_help;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        initView();//初始化控件
        setClickListener();
    }

    private void setClickListener() {
        mHowToRegister.setOnClickListener(this);
        mForgetPwd.setOnClickListener(this);
        mCustom.setOnClickListener(this);
    }

    private void initView() {
        mHowToRegister = (LinearLayout) findViewById(R.id.ll_how_to_register);
        mForgetPwd = (LinearLayout) findViewById(R.id.ll_forget_pwd);
        mCustom = (LinearLayout) findViewById(R.id.ll_custom);
    }

    @Override
    protected boolean setToolBar() {
        mTitle.setText(UIUtils.getString(R.string.help));
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_how_to_register:
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra(Constants.Extra.WEBVIEW_TITLE, UIUtils.getString(R.string.how_to_register));
                intent.putExtra(Constants.Extra.WEBVIEW_URL, UrlConstant.HOW_TO_REGISTER);
                startActivity(intent);

                break;
            case R.id.ll_forget_pwd:
                Intent intent1 = new Intent(this, WebviewActivity.class);
                intent1.putExtra(Constants.Extra.WEBVIEW_TITLE, UIUtils.getString(R.string.forget_pwd));
                intent1.putExtra(Constants.Extra.WEBVIEW_URL, UrlConstant.FORGET_PWD);
                startActivity(intent1);
                break;
            case R.id.ll_custom:
                TelephonyUtils.CallSysDial(this, Constants.CUSTOM_PHONE);
                break;

        }

    }
}
