package com.xyl.boss_app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xyl.boss_app.bean.CabinetState;
import com.xyl.boss_app.ui.fragment.CabinetFragment;

import java.util.List;

/**
 * Created by ahuhxl on 2015/11/10.
 *
 * @Description: TODO (描述这个类的作用)
 */
public class CabinetAdapter extends FragmentPagerAdapter {
    private List<CabinetState> mStateList = null;

    public CabinetAdapter(FragmentManager fm, List<CabinetState> stateList) {
        super(fm);
        mStateList = stateList;
    }

    @Override
    public Fragment getItem(int position) {
        return new CabinetFragment();
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