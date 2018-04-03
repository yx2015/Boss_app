package com.xyl.boss_app.ui.activity;

import android.view.View;
import android.widget.RadioButton;

import com.hxl.universallibrary.base.BaseLazyFragment;
import com.hxl.universallibrary.eventbus.EventCenter;
import com.hxl.universallibrary.widgets.XViewPager;
import com.xyl.boss_app.R;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.ui.adapter.VPFragmentAdapter;
import com.xyl.boss_app.ui.fragment.BussinessCabinetFragment;
import com.xyl.boss_app.ui.fragment.BussinessFeeFragment;
import com.xyl.boss_app.ui.fragment.MainFragment;
import com.xyl.boss_app.ui.fragment.UserCenterFragment;
import com.xyl.boss_app.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.home_container)
    XViewPager mViewPager;

    @InjectView(R.id.rb_main)
    RadioButton mMainViewBtn;

    @InjectView(R.id.rb_bussiness_fee)
    RadioButton mBussinessFee;

    @InjectView(R.id.rb_goods_state)
    RadioButton mGoodsStateBtn;

    @InjectView(R.id.rb_user_center)
    RadioButton mUserCenterBtn;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(this, R.id.home_container);
    }

    @Override
    protected void initViewsAndEvents() {
        mTopBar.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        List<BaseLazyFragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new BussinessFeeFragment());
        fragments.add(new BussinessCabinetFragment());
        fragments.add(new UserCenterFragment());

        mMainViewBtn.setOnClickListener(this);
        mGoodsStateBtn.setOnClickListener(this);
        mUserCenterBtn.setOnClickListener(this);
        mBussinessFee.setOnClickListener(this);


        if (!fragments.isEmpty()) {
            mViewPager.setEnableScroll(false);
            mViewPager.setOffscreenPageLimit(fragments.size());
            mViewPager.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_main:
                //首页
                mTopBar.setVisibility(View.GONE);
                mToolbar.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0, false);
                break;

            case R.id.rb_bussiness_fee:
                //业务费用
                mTitle.setText(UIUtils.getString(R.string.tab_bussiness_fee));
                mToolbar.setVisibility(View.VISIBLE);
                mTopBar.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(1, false);
                break;

            case R.id.rb_goods_state:
                //业务柜量
                mTitle.setText(UIUtils.getString(R.string.tab_concrete_state));
                mToolbar.setVisibility(View.VISIBLE);
                mTopBar.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(2, false);
                break;

            case R.id.rb_user_center:
                //我的
                mTitle.setText(UIUtils.getString(R.string.tab_my));
                mToolbar.setVisibility(View.VISIBLE);
                mTopBar.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        super.onEventComming(eventCenter);
        switch (eventCenter.getEventCode()) {
            case Constants.EVENT_YINGSHOU:
            case Constants.EVENT_YINGFU:
            case Constants.EVENT_PROFITS:
                //业务费用
                mTitle.setText(UIUtils.getString(R.string.tab_bussiness_fee));
                mToolbar.setVisibility(View.VISIBLE);
                mTopBar.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(1, false);
                mBussinessFee.setChecked(true);
                break;
            case Constants.EVENT_START_BIZ:
            case Constants.EVENT_ON_BIZ:
            case Constants.EVENT_END_BIZ:
                mGoodsStateBtn.setChecked(true);
                //业务柜量
                mTitle.setText(UIUtils.getString(R.string.tab_concrete_state));
                mToolbar.setVisibility(View.VISIBLE);
                mTopBar.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(2, false);
                break;
        }
    }
}
