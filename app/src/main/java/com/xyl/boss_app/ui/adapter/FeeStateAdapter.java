package com.xyl.boss_app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xyl.boss_app.bean.FeeState;

import java.util.List;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class FeeStateAdapter extends FragmentPagerAdapter {
    private List<FeeState> mStateList = null;

    public FeeStateAdapter(FragmentManager fm, List<FeeState> stateList) {
        super(fm);
        mStateList = stateList;
    }

    @Override
    public Fragment getItem(int position) {
        return mStateList.get(position).getState();
    }

    @Override
    public int getCount() {
        return mStateList != null ? mStateList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStateList != null ? mStateList.get(position).getName() : null;
    }
}
