package com.xyl.boss_app.ui.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.hxl.universallibrary.eventbus.EventCenter;
import com.hxl.universallibrary.widgets.XViewPager;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.CabinetState;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.ui.activity.base.BaseFragment;
import com.xyl.boss_app.ui.adapter.CabinetAdapter;
import com.xyl.boss_app.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * @ClassName BussinessContainerFragment
 * @Description 业务柜量
 * Created by yx on 2015/12/15 0015.
 */
public class BussinessCabinetFragment extends BaseFragment {

    private List<CabinetState> resultData;

    @InjectView(R.id.tabs1)
    TabLayout tabLayout;

    @InjectView(R.id.fragment_goods_state_pager1)
    XViewPager mViewPager;

    @Override
    protected void onFirstUserVisible() {
        resultData = new ArrayList<>();
        String[] goodsStateArray = UIUtils.getStringArray(R.array.goods_state);
        resultData.add(new CabinetState(goodsStateArray[0], "startOnEndBiz"));
        resultData.add(new CabinetState(goodsStateArray[1], "startBiz"));
        resultData.add(new CabinetState(goodsStateArray[2], "onBiz"));
        resultData.add(new CabinetState(goodsStateArray[3], "endBiz"));
        if (null != resultData && !resultData.isEmpty()) {
            mViewPager.setOffscreenPageLimit(resultData.size());
            mViewPager.setAdapter(new CabinetAdapter(getSupportFragmentManager(), resultData));
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabTextColors(UIUtils.getColor(R.color.primary_text), UIUtils.getColor(R.color.orange));//设置文本在选中和为选中时候的颜色
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    CabinetFragment fragment = (CabinetFragment) mViewPager.getAdapter().instantiateItem(mViewPager, tab.getPosition());
                    fragment.onPageSelected(tab.getPosition(), resultData.get(tab.getPosition()).getState());
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_bussiness_cabinet;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        CabinetFragment fragment;
        switch (eventCenter.getEventCode()) {
            case Constants.EVENT_START_BIZ:
                fragment = (CabinetFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 1);
                fragment.onPageSelected(1, resultData.get(1).getState());
                mViewPager.setCurrentItem(1, false);
                break;
            case Constants.EVENT_ON_BIZ:
                fragment = (CabinetFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 2);
                fragment.onPageSelected(2, resultData.get(2).getState());
                mViewPager.setCurrentItem(2, false);
                break;
            case Constants.EVENT_END_BIZ:
                fragment = (CabinetFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 3);
                fragment.onPageSelected(3, resultData.get(3).getState());
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }
}
