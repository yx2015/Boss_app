package com.xyl.boss_app.ui.activity.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxl.universallibrary.base.BaseAppCompatActivity;
import com.hxl.universallibrary.base.BaseAppManager;
import com.hxl.universallibrary.eventbus.EventCenter;
import com.hxl.universallibrary.netstatus.NetUtils;
import com.hxl.universallibrary.utils.TLog;
import com.umeng.analytics.MobclickAgent;
import com.xyl.boss_app.R;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.ui.activity.LoginActivity;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.UIUtils;

import butterknife.ButterKnife;


public abstract class BaseActivity extends BaseAppCompatActivity implements BaseView {

    public Toolbar mToolbar;
    public TextView mTitle;
    public RelativeLayout mTopBar;
    /**
     * 记录处于前台的Activity
     */
    private static BaseActivity mForegroundActivity = null;
    private static BaseActivity mCurrentActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isApplyKitKatTranslucency()) {
            setSystemBarTintDrawable(getResources().getDrawable(R.drawable.sr_primary));
        }
        mForegroundActivity = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        mTopBar = ButterKnife.findById(this, R.id.rl_title_bar);
        mTitle = ButterKnife.findById(this, R.id.toolbar_title);
        if (null != mToolbar) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            if (setToolBar()) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
        }
    }

    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mForegroundActivity = this;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mForegroundActivity = null;
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCurrentActivity = null;
    }

    @Override
    public void showError(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, null);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case Constants.EVENT_AUTHENTICATION_FAILURE:
                TLog.d("error", "EVENT_AUTHENTICATION_FAILURE");
                finishActivities();
                UIUtils.showToast("您的账号信息有变动，请重新登录！");
                SPUtil.putBoolean("isLogin", false);
                readyGoThenKill(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType netType) {
    }

    @Override
    protected void onNetworkDisConnected() {
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    /**
     * 获取当前处于前台的activity
     */
    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    public static BaseActivity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void finishActivities() {
        BaseAppManager.getInstance().clear();
    }
}
