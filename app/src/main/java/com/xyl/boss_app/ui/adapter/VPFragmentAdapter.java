package com.xyl.boss_app.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hxl.universallibrary.base.BaseLazyFragment;

import java.util.List;

/**
 * Created by ahuhxl on 2015/11/4.
 *
 * @Description: ViewPager的fragment适配器
 */
public class VPFragmentAdapter extends FragmentPagerAdapter {
    List<BaseLazyFragment> mFragmentList;

    public VPFragmentAdapter(FragmentManager fm, List<BaseLazyFragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    @Override
    public BaseLazyFragment getItem(int position) {
        if (mFragmentList != null && position >= 0 && position < mFragmentList.size()) {
            return mFragmentList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return null != mFragmentList ? mFragmentList.size() : 0;
    }
}